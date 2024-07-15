package com.wizeline.ai.hack.video.core.service;

import java.util.concurrent.CompletionStage;

public interface StorageService {
  CompletionStage<byte[]> getContentAsync(String location);
}
