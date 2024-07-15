package com.wizeline.ai.hack.video.domain.entity;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Video extends Persistable {
  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "video",
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  @Builder.Default
  private Set<Segment> segments = new HashSet<>();

  private String location;

  public void setSegments(List<Segment> newSegments) {
    this.segments.clear();
    newSegments.forEach(
        segment -> {
          segment.setVideo(this);
          this.segments.add(segment);
        });
  }
}
