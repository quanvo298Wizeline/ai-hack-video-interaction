package com.wizeline.ai.hack.video.domain.repository;

import com.wizeline.ai.hack.video.domain.entity.Video;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

  @Query(
      "SELECT v FROM Video v JOIN FETCH v.segments vs JOIN vs.images vsi WHERE vsi.id IN (:images)")
  Set<Video> queryByImageList(Set<UUID> images);

  @Query("SELECT v FROM Video v JOIN FETCH v.segments vs JOIN FETCH vs.images vsi WHERE v.id = :id")
  Video queryById(UUID id);

  @Query(
      "SELECT v FROM Video v JOIN FETCH v.segments vs JOIN FETCH vs.images vsi WHERE v.id = :id and vs.id = :segmentId")
  Video queryByIdAndSegments(UUID id, UUID segmentId);
}
