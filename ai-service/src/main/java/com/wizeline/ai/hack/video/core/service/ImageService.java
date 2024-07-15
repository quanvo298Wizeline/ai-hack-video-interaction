package com.wizeline.ai.hack.video.core.service;

import com.wizeline.ai.hack.video.domain.data.DocumentAIResultData;
import com.wizeline.ai.hack.video.domain.data.MediaAIResultData;
import com.wizeline.ai.hack.video.domain.dto.EmbeddingImageResult;
import com.wizeline.ai.hack.video.domain.dto.ImportedImageDto;
import com.wizeline.ai.hack.video.domain.dto.ImportedResultImageDto;
import com.wizeline.ai.hack.video.domain.dto.QueriedImageResult;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.ai.document.Document;

public interface ImageService {
  ImportedResultImageDto importImage(ImportedImageDto image);

  MediaAIResultData getDescription(ImportedImageDto image);

  EmbeddingImageResult createEmbeddingDescription(ImportedImageDto image);

  List<DocumentAIResultData> queryDocumentAIList(String query);

  Map<UUID, Document> queryImageDocuments(String query);

  Map<UUID, Document> queryImageDocuments(String query, List<UUID> ids);

  List<QueriedImageResult> queryImage(String query);
}
