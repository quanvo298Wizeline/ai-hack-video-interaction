package com.wizeline.ai.hack.video.domain.data;

import com.wizeline.ai.hack.video.domain.entity.Image;
import org.springframework.ai.document.Document;

public record MediaDocumentAIResultData(String aiResponse, Image image, Document document) {}
