package com.wizeline.ai.hack.video.domain.api.response;

import com.wizeline.ai.hack.video.domain.api.request.SegmentBodyRequest;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SegmentResponse extends SegmentBodyRequest {
  private AIResponse aiResponse;
  private UUID id;
}
