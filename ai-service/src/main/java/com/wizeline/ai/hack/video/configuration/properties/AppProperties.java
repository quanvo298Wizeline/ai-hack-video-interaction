package com.wizeline.ai.hack.video.configuration.properties;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties("aivideo")
@Getter
@Component
public class AppProperties {

  private final Aws aws = new Aws();

  private final Image image = new Image();

  @Getter
  @Setter
  public static class Image {
    private int resultSize = 20;
  }

  @Getter
  @Setter
  public static class Aws {
    private String region;
    private String s3Bucket;
    private String accessKey;
    private String secretKey;
  }
}
