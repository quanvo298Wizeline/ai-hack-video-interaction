package com.wizeline.ai.hack.video.domain.dto;

import lombok.*;
import org.springframework.ai.document.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueriedImageResult {
  ImageDto image;
  Document document;
  String aiResponse;
}
