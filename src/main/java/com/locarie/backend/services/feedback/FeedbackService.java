package com.locarie.backend.services.feedback;

public interface FeedbackService {
  void sendFeedback(Long userId, String content);
}
