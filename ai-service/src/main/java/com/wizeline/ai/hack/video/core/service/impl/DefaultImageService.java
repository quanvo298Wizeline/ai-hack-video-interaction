package com.wizeline.ai.hack.video.core.service.impl;

import com.wizeline.ai.hack.video.configuration.properties.AppProperties;
import com.wizeline.ai.hack.video.core.service.AIService;
import com.wizeline.ai.hack.video.core.service.ImageService;
import com.wizeline.ai.hack.video.core.utils.ImageUtils;
import com.wizeline.ai.hack.video.domain.data.DocumentAIResultData;
import com.wizeline.ai.hack.video.domain.data.MediaAIResultData;
import com.wizeline.ai.hack.video.domain.data.MetaData;
import com.wizeline.ai.hack.video.domain.dto.EmbeddingImageResult;
import com.wizeline.ai.hack.video.domain.dto.ImportedImageDto;
import com.wizeline.ai.hack.video.domain.dto.ImportedResultImageDto;
import com.wizeline.ai.hack.video.domain.dto.QueriedImageResult;
import com.wizeline.ai.hack.video.domain.entity.Image;
import com.wizeline.ai.hack.video.domain.repository.DocumentVectorRepository;
import com.wizeline.ai.hack.video.domain.repository.ImageRepository;
import com.wizeline.ai.hack.video.mapper.ImageMapper;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@AllArgsConstructor
public class DefaultImageService implements ImageService {
  private final AIService aiService;
  private final AppProperties appProperties;
  private final ImageRepository imageRepository;
  private final DocumentVectorRepository documentVectorRepository;

  @Override
  @Transactional
  public ImportedResultImageDto importImage(ImportedImageDto request) {
    Image image =
        Image.builder()
            .imageName(request.getOriginalName())
            .imageType(request.getImageType())
            .content(request.getContent())
            .build();
    Image saved = this.imageRepository.save(image);
    request.setExternalId(saved.getId());
    MediaAIResultData mediaAIResultData = getDescription(request);
    request.setDescription(mediaAIResultData.aiResponse());
    EmbeddingImageResult imageEmbeddingResult = this.createEmbeddingDescription(request);
    return new ImportedResultImageDto(
        imageEmbeddingResult.getAnswered(),
        request.getContent(),
        request.getImageType(),
        request.getExternalId());
  }

  @Override
  public MediaAIResultData getDescription(final ImportedImageDto image) {
    ImportedImageDto cloned = ImageUtils.resizeImage(image);
    return this.aiService.getImageContent(cloned);
  }

  @Override
  @Transactional
  public EmbeddingImageResult createEmbeddingDescription(final ImportedImageDto image) {
    String description = image.getDescription();
    var aiDocument = new Document(description);
    aiDocument.getMetadata().put(MetaData.ID, image.getExternalId());
    aiDocument.getMetadata().put(MetaData.DATATYPE, MetaData.DataType.IMAGE.toString());
    this.documentVectorRepository.add(List.of(aiDocument));
    return new EmbeddingImageResult(description, image.getExternalId());
  }

  @Override
  public Map<UUID, Document> queryImageDocuments(final String query) {
    List<Document> aiDocuments =
        documentVectorRepository
            .retrieve(query, MetaData.DataType.IMAGE, appProperties.getImage().getResultSize())
            .stream()
            .sorted(Comparator.comparing(doc -> (Float) doc.getMetadata().get(MetaData.DISTANCE)))
            .toList();
    if (CollectionUtils.isEmpty(aiDocuments)) {
      return Collections.emptyMap();
    }

    return aiDocuments.stream()
        .collect(
            Collectors.toMap(
                doc -> UUID.fromString(doc.getMetadata().get(MetaData.ID).toString()), doc -> doc));
  }

  @Override
  public Map<UUID, Document> queryImageDocuments(final String query, List<UUID> uuids) {
    List<Document> aiDocuments =
        documentVectorRepository
            .retrieve(
                query, uuids, MetaData.DataType.IMAGE, appProperties.getImage().getResultSize())
            .stream()
            .sorted(Comparator.comparing(doc -> (Float) doc.getMetadata().get(MetaData.DISTANCE)))
            .toList();
    if (CollectionUtils.isEmpty(aiDocuments)) {
      return Collections.emptyMap();
    }

    return aiDocuments.stream()
        .collect(
            Collectors.toMap(
                doc -> UUID.fromString(doc.getMetadata().get(MetaData.ID).toString()), doc -> doc));
  }

  @Override
  public List<DocumentAIResultData> queryDocumentAIList(final String query) {
    List<Document> retrievedDocuments = this.queryImageDocuments(query).values().stream().toList();

    if (CollectionUtils.isEmpty(retrievedDocuments)) {
      return Collections.emptyList();
    }

    return aiService.chatWithAI(query, retrievedDocuments);
  }

  @Override
  public List<QueriedImageResult> queryImage(final String query) {
    List<DocumentAIResultData> aiDocumentResultData = queryDocumentAIList(query);
    if (CollectionUtils.isEmpty(aiDocumentResultData)) {
      return Collections.emptyList();
    }

    Map<Document, String> answersMap =
        aiDocumentResultData.stream()
            .collect(
                Collectors.toMap(DocumentAIResultData::document, DocumentAIResultData::aiResponse));

    Map<UUID, Document> documentMap =
        answersMap.keySet().stream()
            .collect(
                Collectors.toMap(
                    doc -> UUID.fromString(doc.getMetadata().get(MetaData.ID).toString()),
                    doc -> doc));

    List<Image> images = imageRepository.findAllById(documentMap.keySet());

    return images.stream()
        .map(
            image -> {
              UUID uuid = image.getId();
              Document document = documentMap.get(uuid);
              String aiResponse = answersMap.get(document);
              return QueriedImageResult.builder()
                  .aiResponse(aiResponse)
                  .document(document)
                  .image(ImageMapper.convert(image))
                  .build();
            })
        .toList();
  }
}
