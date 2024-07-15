package com.wizeline.ai.hack.video.domain.data;

import org.springframework.ai.document.Document;

public record DocumentAIResultData(String aiResponse, Document document) {}
