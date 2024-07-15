package com.wizeline.ai.hack.video.domain.api.response;

import com.wizeline.ai.hack.video.domain.api.request.VideoBodyRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class VideoResponse extends VideoBodyRequest {
  private UUID id;
}
