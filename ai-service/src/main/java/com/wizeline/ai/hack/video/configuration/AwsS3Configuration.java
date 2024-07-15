package com.wizeline.ai.hack.video.configuration;

import com.wizeline.ai.hack.video.configuration.properties.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Configuration {

  @Bean
  @Autowired
  public S3Client createS3Client(final AppProperties appProperties) {
    return S3Client.builder()
        .region(Region.of(appProperties.getAws().getRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    appProperties.getAws().getAccessKey(), appProperties.getAws().getSecretKey())))
        .build();
  }
}
