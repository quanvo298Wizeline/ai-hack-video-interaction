package com.wizeline.ai.hack.video.api.controller;

import com.wizeline.ai.hack.video.core.service.ImageService;
import com.wizeline.ai.hack.video.domain.api.request.QueryRequest;
import com.wizeline.ai.hack.video.domain.api.response.BodyResponse;
import com.wizeline.ai.hack.video.domain.api.response.ImageResponse;
import com.wizeline.ai.hack.video.domain.dto.ImportedResultImageDto;
import com.wizeline.ai.hack.video.mapper.ImageMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/image")
@RestController
@RequiredArgsConstructor
public class ImageController {
  private final ImageService imageService;

  @PostMapping("/query")
  public BodyResponse<List<ImageResponse>> query(@RequestBody QueryRequest queryRequest) {
    assert queryRequest.getQuery() != null;
    return BodyResponse.body(
        this.imageService.queryImage(queryRequest.getQuery()).stream()
            .map(ImageMapper::convert)
            .toList());
  }

  @PostMapping
  public BodyResponse<ImportedResultImageDto> importImage(@RequestParam("file") MultipartFile file)
      throws IOException {
    return BodyResponse.body(this.imageService.importImage(ImageMapper.convert(file)));
  }
}
