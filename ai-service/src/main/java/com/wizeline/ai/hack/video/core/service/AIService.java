package com.wizeline.ai.hack.video.core.service;

import com.wizeline.ai.hack.video.domain.data.DocumentAIResultData;
import com.wizeline.ai.hack.video.domain.data.MediaAIResultData;
import com.wizeline.ai.hack.video.domain.dto.ImportedImageDto;
import java.util.List;
import org.springframework.ai.document.Document;

public interface AIService {
  MediaAIResultData getImageContent(ImportedImageDto imageBodyRequest);

  List<DocumentAIResultData> chatWithAI(String question, List<Document> documents);
}
