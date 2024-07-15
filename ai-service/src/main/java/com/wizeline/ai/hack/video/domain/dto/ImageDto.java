package com.wizeline.ai.hack.video.domain.dto;

import com.wizeline.ai.hack.video.domain.data.MetaData;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {
  private UUID id;

  private String imageName;

  private MetaData.ImageType imageType;

  private String location;

  private byte[] content;
}
