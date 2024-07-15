package com.wizeline.ai.hack.video.domain.repository;

import com.wizeline.ai.hack.video.domain.data.MetaData;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.document.Document;

public interface DocumentVectorRepository {
  void add(List<Document> documents);

  List<Document> retrieve(String query, MetaData.DataType dataType, int size);

  List<Document> retrieve(String query, List<UUID> uuids, MetaData.DataType dataType, int size);
}
