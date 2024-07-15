package com.wizeline.ai.hack.video.core.service.impl;

import com.wizeline.ai.hack.video.core.service.AIService;
import com.wizeline.ai.hack.video.core.service.ImageService;
import com.wizeline.ai.hack.video.core.service.StorageService;
import com.wizeline.ai.hack.video.core.service.VideoService;
import com.wizeline.ai.hack.video.domain.api.request.VideoBodyRequest;
import com.wizeline.ai.hack.video.domain.api.response.VideoResponse;
import com.wizeline.ai.hack.video.domain.data.DocumentAIResultData;
import com.wizeline.ai.hack.video.domain.data.MediaAIResultData;
import com.wizeline.ai.hack.video.domain.data.MediaDocumentAIResultData;
import com.wizeline.ai.hack.video.domain.data.MetaData;
import com.wizeline.ai.hack.video.domain.dto.ImportedImageDto;
import com.wizeline.ai.hack.video.domain.entity.Image;
import com.wizeline.ai.hack.video.domain.entity.Video;
import com.wizeline.ai.hack.video.domain.repository.VideoRepository;
import com.wizeline.ai.hack.video.mapper.VideoMapper;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultVideoService implements VideoService {
  private final ImageService imageService;
  private final AIService aiService;
  private final StorageService awss3StorageService;
  private final VideoRepository videoRepository;

  @Override
  @Transactional
  public void processVideo(final VideoBodyRequest videoBodyRequest) {
    Video video = VideoMapper.convert(videoBodyRequest);
    Video savedVideo = videoRepository.save(video);

    List<Image> imageFrames =
        savedVideo.getSegments().stream().flatMap(segment -> segment.getImages().stream()).toList();

    List<CompletableFuture<ImportedImageDto>> imageProcessingFutures =
        imageFrames.stream()
            .map(
                imageFrame ->
                    awss3StorageService
                        .getContentAsync(imageFrame.getLocation())
                        .thenApply(
                            imageContent -> {
                              return createFromS3Location(imageFrame, imageContent);
                              // return imageService.createDescriptionEmbedding(importedImage);
                            })
                        .exceptionally(
                            ex -> {
                              log.error(
                                  "Error processing image with ID: {}", imageFrame.getId(), ex);
                              throw new RuntimeException(ex);
                            })
                        .toCompletableFuture())
            .toList();

    CompletableFuture.allOf(imageProcessingFutures.toArray(new CompletableFuture[0])).join();
    imageProcessingFutures.forEach(
        importedImageDtoCompletableFuture -> {
          ImportedImageDto importedImageDto = importedImageDtoCompletableFuture.join();
          MediaAIResultData mediaAIResultData = this.imageService.getDescription(importedImageDto);
          importedImageDto.setDescription(mediaAIResultData.aiResponse());
          this.imageService.createEmbeddingDescription(importedImageDto);
        });
  }

  @Override
  public List<VideoResponse> queryVideo(final String query) {
    Map<UUID, Document> documentMap = this.imageService.queryImageDocuments(query);
    if (CollectionUtils.isEmpty(documentMap)) {
      return Collections.emptyList();
    }

    Set<Video> videos = videoRepository.queryByImageList(documentMap.keySet());
    return processVideo(query, videos, documentMap);
  }

  @Override
  public VideoResponse queryInVideo(final UUID videoId, final String query) {
    return queryInSegment(videoId, null, query);
  }

  @Override
  public VideoResponse queryInSegment(
      final UUID videoId, final UUID segmentId, final String query) {
    Video video =
        segmentId == null
            ? videoRepository.queryById(videoId)
            : videoRepository.queryByIdAndSegments(videoId, segmentId);
    if (query == null) {
      return null;
    }
    List<Image> imageFrames =
        video.getSegments().stream().flatMap(segment -> segment.getImages().stream()).toList();
    if (CollectionUtils.isEmpty(imageFrames)) {
      return null;
    }
    Map<UUID, Document> documentMap =
        this.imageService.queryImageDocuments(
            query, imageFrames.stream().map(Image::getId).collect(Collectors.toList()));
    if (CollectionUtils.isEmpty(documentMap)) {
      return null;
    }
    return processVideo(query, Set.of(video), documentMap).get(0);
  }

  private List<VideoResponse> processVideo(
      String query, Set<Video> videos, Map<UUID, Document> documentMap) {
    Map<Document, Image> imageMap =
        videos.stream()
            .flatMap(
                video ->
                    video.getSegments().stream().flatMap(segment -> segment.getImages().stream()))
            .map(
                image ->
                    documentMap.containsKey(image.getId())
                        ? ImmutablePair.of(documentMap.get(image.getId()), image)
                        : null)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(ImmutablePair::getLeft, ImmutablePair::getRight));

    List<DocumentAIResultData> documentAIResultData =
        this.aiService.chatWithAI(query, imageMap.keySet().stream().toList());

    Map<UUID, MediaDocumentAIResultData> mediaDocumentAIResultDataMap =
        documentAIResultData.stream()
            .collect(
                Collectors.toMap(
                    doc ->
                        UUID.fromString(doc.document().getMetadata().get(MetaData.ID).toString()),
                    doc ->
                        new MediaDocumentAIResultData(
                            doc.aiResponse(), imageMap.get(doc.document()), doc.document())));
    return videos.stream()
        .map(video -> VideoMapper.convert(video, mediaDocumentAIResultDataMap))
        .toList();
  }

  private ImportedImageDto createFromS3Location(Image savedImaged, byte[] content) {
    return ImportedImageDto.builder()
        .imageType(savedImaged.getImageType())
        .contentSize(content.length)
        .content(content)
        .externalId(savedImaged.getId())
        .originalName(savedImaged.getImageName())
        .build();
  }
}
