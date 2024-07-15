package com.wizeline.ai.hack.video.domain.entity;

import com.wizeline.ai.hack.video.domain.data.MetaData;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends Persistable {

  private String imageName;

  @Enumerated(EnumType.STRING)
  private MetaData.ImageType imageType;

  private String location;

  @Lob private byte[] content;

  @ManyToOne private Segment segment;

  private Boolean defaultImage;
}
