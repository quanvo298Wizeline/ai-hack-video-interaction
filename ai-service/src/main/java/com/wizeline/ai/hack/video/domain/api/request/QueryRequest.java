package com.wizeline.ai.hack.video.domain.api.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QueryRequest {

  private String query;
}
