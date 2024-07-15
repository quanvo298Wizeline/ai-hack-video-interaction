package com.wizeline.ai.hack.video.core.service;

import com.wizeline.ai.hack.video.domain.api.request.VideoBodyRequest;
import com.wizeline.ai.hack.video.domain.api.response.VideoResponse;
import java.util.List;
import java.util.UUID;

public interface VideoService {
  void processVideo(VideoBodyRequest videoBodyRequest);

  List<VideoResponse> queryVideo(String query);

  VideoResponse queryInVideo(UUID videoId, String query);

  VideoResponse queryInSegment(UUID videoId, UUID segmentId, String query);
}
