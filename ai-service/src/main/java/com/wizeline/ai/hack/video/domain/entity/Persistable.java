package com.wizeline.ai.hack.video.domain.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Persistable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column
  UUID id;

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Persistable compared
        && this.getId() != null
        && ((Persistable) obj).getId() != null) {
      return this.getId().equals(compared.getId());
    }
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return (this.getId() == null) ? super.hashCode() : this.getId().hashCode();
  }
}
