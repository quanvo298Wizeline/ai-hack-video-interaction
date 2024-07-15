package com.wizeline.ai.hack.video.core.service.impl;

import com.wizeline.ai.hack.video.configuration.properties.AppProperties;
import com.wizeline.ai.hack.video.core.service.StorageService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Service
@RequiredArgsConstructor
public class AWSS3StorageService implements StorageService {
  private final AppProperties appProperties;

  private final S3Client s3Client;

  @Override
  public CompletionStage<byte[]> getContentAsync(final String location) {
    return CompletableFuture.supplyAsync(
        () -> {
          final GetObjectRequest getObjectRequest =
              GetObjectRequest.builder()
                  .bucket(this.appProperties.getAws().getS3Bucket())
                  .key(location)
                  .build();

          return s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asByteArray();
        });
  }
}
