package com.wizeline.ai.hack.video.core.utils;

import com.wizeline.ai.hack.video.domain.dto.ImportedImageDto;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtils {
  public static ImportedImageDto resizeImage(ImportedImageDto imageRequest) {
    try {
      BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageRequest.getContent()));
      int targetHeight = image.getHeight();
      int targetWidth = image.getWidth();
      if (image.getHeight() > 672 && image.getWidth() > 672) {
        if (image.getHeight() < image.getWidth()) {
          targetHeight = Math.round(image.getHeight() / (image.getHeight() / 672.0f));
          targetWidth = Math.round(image.getWidth() / (image.getHeight() / 672.0f));
        } else {
          targetHeight = Math.round(image.getHeight() / (image.getWidth() / 672.0f));
          targetWidth = Math.round(image.getWidth() / (image.getWidth() / 672.0f));
        }
      }
      var outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
      outputImage
          .getGraphics()
          .drawImage(
              image.getScaledInstance(targetWidth, targetHeight, java.awt.Image.SCALE_SMOOTH),
              0,
              0,
              null);
      var ios = new ByteArrayOutputStream();
      ImageIO.write(outputImage, imageRequest.getImageType().toString(), ios);
      imageRequest.setContent(ios.toByteArray());
      imageRequest.setContentSize(ios.toByteArray().length);
      log.info("Resized image to x: {}, y: {}", targetWidth, targetHeight);
    } catch (IOException e) {
      log.info("Image resize failed.", e);
    }
    return imageRequest;
  }
}
