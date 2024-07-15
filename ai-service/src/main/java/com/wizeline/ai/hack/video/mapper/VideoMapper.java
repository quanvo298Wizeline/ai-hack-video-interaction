package com.wizeline.ai.hack.video.mapper;

import com.wizeline.ai.hack.video.domain.api.request.SegmentBodyRequest;
import com.wizeline.ai.hack.video.domain.api.request.VideoBodyRequest;
import com.wizeline.ai.hack.video.domain.api.response.AIResponse;
import com.wizeline.ai.hack.video.domain.api.response.SegmentResponse;
import com.wizeline.ai.hack.video.domain.api.response.VideoResponse;
import com.wizeline.ai.hack.video.domain.data.MediaDocumentAIResultData;
import com.wizeline.ai.hack.video.domain.data.MetaData;
import com.wizeline.ai.hack.video.domain.entity.Image;
import com.wizeline.ai.hack.video.domain.entity.Segment;
import com.wizeline.ai.hack.video.domain.entity.Video;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.compress.utils.FileNameUtils;

public class VideoMapper {
  public static Video convert(VideoBodyRequest videoBodyRequest) {
    Video result = Video.builder().location(videoBodyRequest.getOriginalFile()).build();
    result.setSegments(videoBodyRequest.getSegments().stream().map(VideoMapper::convert).toList());
    return result;
  }

  public static Segment convert(SegmentBodyRequest segmentBodyRequest) {
    Segment segment =
        Segment.builder()
            .startTimestampMillis(segmentBodyRequest.getStartTime())
            .endTimestampMillis(segmentBodyRequest.getEndTime())
            .durationMillis(segmentBodyRequest.getDuration())
            .position(segmentBodyRequest.getIndex())
            .location(segmentBodyRequest.getVideo())
            .build();
    Image defaultImage = convert(segmentBodyRequest.getFrame());
    defaultImage.setDefaultImage(true);
    segment.setImages(List.of(defaultImage));
    return segment;
  }

  public static Image convert(String location) {
    String fileName = location.substring(location.lastIndexOf("/") + 1);
    String extension = FileNameUtils.getExtension(fileName);
    return Image.builder()
        .imageType(MetaData.ImageType.convertFrom(extension))
        .imageName(fileName)
        .location(location)
        .defaultImage(false)
        .build();
  }

  public static VideoResponse convert(
      Video video, Map<UUID, MediaDocumentAIResultData> mediaDocumentAIResultDataMap) {
    return VideoResponse.builder()
        .originalFile(video.getLocation())
        .id(video.getId())
        .segments(
            video.getSegments().stream()
                .map(segment -> convert(segment, mediaDocumentAIResultDataMap))
                .sorted(Comparator.comparingInt(SegmentBodyRequest::getIndex))
                .collect(Collectors.toList()))
        .build();
  }

  public static SegmentResponse convert(
      Segment segment, Map<UUID, MediaDocumentAIResultData> mediaDocumentAIResultDataMap) {
    Image defaultImage =
        segment.getImages().stream().filter(Image::getDefaultImage).findFirst().orElse(null);

    MediaDocumentAIResultData mediaDocumentAIResultData =
        defaultImage != null ? mediaDocumentAIResultDataMap.get(defaultImage.getId()) : null;

    return SegmentResponse.builder()
        .id(segment.getId())
        .index(segment.getPosition())
        .startTime(segment.getStartTimestampMillis())
        .endTime(segment.getEndTimestampMillis())
        .duration(segment.getDurationMillis())
        .video(segment.getLocation())
        .frame(defaultImage == null ? null : defaultImage.getLocation())
        .aiResponse(
            mediaDocumentAIResultData == null
                ? null
                : AIResponse.builder().response(mediaDocumentAIResultData.aiResponse()).build())
        .build();
  }
}
