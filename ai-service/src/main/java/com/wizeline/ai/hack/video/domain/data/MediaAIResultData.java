package com.wizeline.ai.hack.video.domain.data;

import com.wizeline.ai.hack.video.domain.dto.ImportedImageDto;

public record MediaAIResultData(String aiResponse, ImportedImageDto imageBodyRequest) {}
