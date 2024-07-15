package com.wizeline.ai.hack.video.domain.data;

import lombok.Getter;
import org.springframework.http.MediaType;

public interface MetaData {

  public static final String ID = "id";
  public static final String DATATYPE = "datatype";
  public static final String DISTANCE = "distance";

  public enum DataType {
    IMAGE
  }

  @Getter
  enum ImageType {
    JPEG("jpg", MediaType.IMAGE_JPEG_VALUE),
    PNG("png", MediaType.IMAGE_JPEG_VALUE),
    SVG("svg", "image/svg+xml"),
    UNKNOWN("unknown", "unknown");

    private final String type;
    private final String mediaType;

    ImageType(String type, String mediaType) {
      this.type = type;
      this.mediaType = mediaType;
    }

    public static ImageType convertFrom(String type) {
      return switch (type.toLowerCase()) {
        case "jpg", "jpeg" -> ImageType.JPEG;
        case "png" -> ImageType.PNG;
        case "svg" -> ImageType.SVG;
        default -> ImageType.UNKNOWN;
      };
    }

    @Override
    public String toString() {
      return this.type;
    }
  }
}
