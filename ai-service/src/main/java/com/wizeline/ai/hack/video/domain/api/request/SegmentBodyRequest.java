package com.wizeline.ai.hack.video.domain.api.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SegmentBodyRequest {

  private long startTime;
  private long endTime;
  private long duration;
  private int index;
  private String frame;
  private String video;
}
