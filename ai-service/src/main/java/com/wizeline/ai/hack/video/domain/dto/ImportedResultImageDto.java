package com.wizeline.ai.hack.video.domain.dto;

import com.wizeline.ai.hack.video.domain.data.MetaData;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportedResultImageDto {
  String answered;
  byte[] content;
  MetaData.ImageType imageType;
  UUID externalId;
}
