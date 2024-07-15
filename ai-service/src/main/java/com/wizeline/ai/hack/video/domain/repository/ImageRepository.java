package com.wizeline.ai.hack.video.domain.repository;

import com.wizeline.ai.hack.video.domain.entity.Image;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {}
