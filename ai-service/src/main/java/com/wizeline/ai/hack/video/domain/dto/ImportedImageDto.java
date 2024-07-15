package com.wizeline.ai.hack.video.domain.dto;

import com.wizeline.ai.hack.video.domain.data.MetaData;
import java.io.ByteArrayInputStream;
import java.util.UUID;
import lombok.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportedImageDto {
  private byte[] content;
  private MetaData.ImageType imageType;
  private long contentSize;
  private String originalName;
  private UUID externalId;
  private String description;

  public Resource getResource() {
    return new InputStreamResource(new ByteArrayInputStream(content));
  }
}
