package com.wizeline.ai.hack.video.core.service.impl;

import com.wizeline.ai.hack.video.core.service.AIService;
import com.wizeline.ai.hack.video.core.utils.PromptUtils;
import com.wizeline.ai.hack.video.domain.data.DocumentAIResultData;
import com.wizeline.ai.hack.video.domain.data.MediaAIResultData;
import com.wizeline.ai.hack.video.domain.dto.ImportedImageDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

@Service
@AllArgsConstructor
public class DefaultAIService implements AIService {
  private final ChatClient chatClient;

  @Override
  public MediaAIResultData getImageContent(ImportedImageDto imageBodyRequest) {
    var prompt =
        new Prompt(
            new UserMessage(
                "Explain what do you see on this picture?",
                List.of(
                    new Media(
                        MimeType.valueOf(imageBodyRequest.getImageType().getMediaType()),
                        imageBodyRequest.getResource()))));
    var response = this.chatClient.prompt(prompt).call().chatResponse();
    return new MediaAIResultData(response.getResult().getOutput().getContent(), imageBodyRequest);
  }

  @Override
  public List<DocumentAIResultData> chatWithAI(
      final String question, final List<Document> documents) {
    // Create a list of CompletableFutures
    List<CompletableFuture<DocumentAIResultData>> completableFutures =
        documents.stream().map(document -> chatWithAIAsync(question, document)).toList();

    // Combine all CompletableFutures into one
    CompletableFuture<Void> allFutures =
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));

    // After all futures are complete, gather the results
    CompletableFuture<List<DocumentAIResultData>> allResultsFuture =
        allFutures.thenApply(
            v ->
                completableFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));

    // Return the result list
    return allResultsFuture.join();
  }

  public CompletableFuture<DocumentAIResultData> chatWithAIAsync(
      final String question, Document document) {
    return CompletableFuture.supplyAsync(
        () -> {
          var prompt =
              new Prompt(new UserMessage(PromptUtils.createAnswersPrompt(question, document)));
          ChatResponse chatResponse = this.chatClient.prompt(prompt).call().chatResponse();
          return new DocumentAIResultData(
              chatResponse.getResult().getOutput().getContent(), document);
        });
  }
}
