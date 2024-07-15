package com.wizeline.ai.hack.video.core.utils;

import java.util.Map;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.ai.document.Document;

public class PromptUtils {
  static final String PROMPT_ANSWER_BASED_ON_DESCRIPTION =
      """
      Frame 1:
      ${frameExplanationList}

      Answer the question '${question}' based on summarizing the preceding frame answers.
      """;

  /*public static String createFrameExplanationList(List<Document> documents) {
    AtomicInteger counter = new AtomicInteger(1);
    return documents.stream()
        .map(
            document ->
                "Frame " + counter.getAndIncrement() + ": \"" + document.getContent() + "\"")
        .collect(Collectors.joining("\n"));
  }*/

  public static String createAnswersPrompt(String question, Document document) {
    Map<String, String> values =
        Map.of("frameExplanationList", document.getContent(), "question", question);
    StringSubstitutor sub = new StringSubstitutor(values);
    return sub.replace(PROMPT_ANSWER_BASED_ON_DESCRIPTION);
  }
}
