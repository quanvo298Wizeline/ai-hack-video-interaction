package com.wizeline.ai.hack.video.domain.api.response;

import com.wizeline.ai.hack.video.domain.data.MetaData;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
  String aiResponse;
  String b64Image;
  String path;
  MetaData.ImageType imageType;
}
