package com.locarie.backend.services.feedback;

public interface FeedbackService {
  boolean sendFeedback(Long userId, String content);
}
