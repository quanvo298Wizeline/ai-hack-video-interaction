package com.wizeline.ai.hack.video.domain.dto;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmbeddingImageResult {
  String answered;
  UUID externalId;
}
