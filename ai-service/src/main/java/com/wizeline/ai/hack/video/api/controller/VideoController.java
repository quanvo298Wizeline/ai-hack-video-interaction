package com.wizeline.ai.hack.video.api.controller;

import com.wizeline.ai.hack.video.core.service.VideoService;
import com.wizeline.ai.hack.video.domain.api.request.QueryRequest;
import com.wizeline.ai.hack.video.domain.api.request.VideoBodyRequest;
import com.wizeline.ai.hack.video.domain.api.response.BodyResponse;
import com.wizeline.ai.hack.video.domain.api.response.VideoResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/video")
@RestController
@RequiredArgsConstructor
public class VideoController {
  private final VideoService videoService;

  @PostMapping
  public BodyResponse<String> videoWebhook(@RequestBody VideoBodyRequest videoBodyRequest) {
    this.videoService.processVideo(videoBodyRequest);
    return BodyResponse.success();
  }

  @PostMapping("/query")
  public BodyResponse<List<VideoResponse>> query(@RequestBody QueryRequest queryRequest) {
    assert queryRequest.getQuery() != null;
    return BodyResponse.body(this.videoService.queryVideo(queryRequest.getQuery()));
  }

  @PostMapping("/{id}/segment/{segmentId}/query")
  public BodyResponse<VideoResponse> queryInSegment(
      @PathVariable("id") String videoId,
      @PathVariable("segmentId") String segmentId,
      @RequestBody QueryRequest queryRequest) {
    assert queryRequest.getQuery() != null;
    return BodyResponse.body(
        this.videoService.queryInSegment(
            UUID.fromString(videoId), UUID.fromString(segmentId), queryRequest.getQuery()));
  }

  @PostMapping("/{id}/query")
  public BodyResponse<VideoResponse> queryInVideo(
      @PathVariable("id") String videoId, @RequestBody QueryRequest queryRequest) {
    assert queryRequest.getQuery() != null;
    return BodyResponse.body(
        this.videoService.queryInVideo(UUID.fromString(videoId), queryRequest.getQuery()));
  }
}
