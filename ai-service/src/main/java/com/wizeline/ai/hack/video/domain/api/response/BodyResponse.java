package com.wizeline.ai.hack.video.domain.api.response;

import lombok.Getter;

@Getter
public class BodyResponse<T> {

  private static final BodyResponse<?> SUCCESS = new BodyResponse<>();
  private final String status;
  private T body;

  protected BodyResponse(T body, String status) {
    this.body = body;
    this.status = status;
  }

  protected BodyResponse() {
    status = "success";
  }

  protected BodyResponse(T body) {
    this.body = body;
    status = "success";
  }

  public static <E> BodyResponse<E> body(E body) {
    return new BodyResponse<>(body);
  }

  public static <E> BodyResponse<E> success() {
    @SuppressWarnings("unchecked")
    BodyResponse<E> successResponse = (BodyResponse<E>) SUCCESS;
    return successResponse;
  }
}
