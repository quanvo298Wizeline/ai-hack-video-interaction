package com.wizeline.ai.hack.video.domain.entity;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Segment extends Persistable {
  private Long startTimestampMillis;

  private Long endTimestampMillis;

  private Long durationMillis;

  private Integer position;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "segment",
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  @Builder.Default
  private Set<Image> images = new HashSet<>();

  @ManyToOne private Video video;

  private String location;

  public void setImages(List<Image> newImages) {
    this.images.clear();
    newImages.forEach(
        image -> {
          image.setSegment(this);
          this.images.add(image);
        });
  }
}
