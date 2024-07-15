package com.wizeline.ai.hack.video.domain.api.request;

import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VideoBodyRequest {

  private List<SegmentBodyRequest> segments;
  private String originalFile;
}
