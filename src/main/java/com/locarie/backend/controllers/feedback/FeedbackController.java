package com.locarie.backend.controllers.feedback;

import com.locarie.backend.services.feedback.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {
  private final FeedbackService feedbackService;

  public FeedbackController(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }

  @PostMapping
  public ResponseEntity<Void> sendFeedback(
      @RequestParam("userId") Long userId, @RequestParam("content") String content) {
    feedbackService.sendFeedback(userId, content);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
