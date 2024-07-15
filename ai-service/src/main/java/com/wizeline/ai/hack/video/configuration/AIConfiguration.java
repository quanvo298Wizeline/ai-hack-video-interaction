package com.wizeline.ai.hack.video.configuration;

import lombok.AllArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class AIConfiguration {

  private final OllamaApi ollamaApi;

  @Bean
  public ChatClient chatClient() {
    ChatClient.Builder builder =
        ChatClient.builder(
            new OllamaChatModel(ollamaApi, OllamaOptions.create().withModel("llava")));
    return builder.build();
  }
}
