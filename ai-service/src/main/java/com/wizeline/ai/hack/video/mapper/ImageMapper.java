package com.wizeline.ai.hack.video.mapper;

import com.wizeline.ai.hack.video.domain.api.response.ImageResponse;
import com.wizeline.ai.hack.video.domain.data.MetaData;
import com.wizeline.ai.hack.video.domain.dto.ImageDto;
import com.wizeline.ai.hack.video.domain.dto.ImportedImageDto;
import com.wizeline.ai.hack.video.domain.dto.QueriedImageResult;
import com.wizeline.ai.hack.video.domain.entity.Image;
import java.io.IOException;
import java.util.Base64;
import org.springframework.web.multipart.MultipartFile;

public class ImageMapper {
  public static ImportedImageDto convert(MultipartFile multipartFile) throws IOException {
    return ImportedImageDto.builder()
        .imageType(MetaData.ImageType.JPEG)
        .content(multipartFile.getBytes())
        .contentSize(multipartFile.getSize())
        .originalName(multipartFile.getOriginalFilename())
        .build();
  }

  public static ImageDto convert(Image image) {
    return ImageDto.builder()
        .id(image.getId())
        .imageType(image.getImageType())
        .imageName(image.getImageName())
        .location(image.getLocation())
        .content(image.getContent())
        .build();
  }

  public static ImageResponse convert(QueriedImageResult image) {
    return ImageResponse.builder()
        .path(image.getImage().getLocation())
        .b64Image(
            image.getImage().getContent() != null
                ? Base64.getEncoder().encodeToString(image.getImage().getContent())
                : null)
        .aiResponse(image.getAiResponse())
        .imageType(image.getImage().getImageType())
        .build();
  }
}
