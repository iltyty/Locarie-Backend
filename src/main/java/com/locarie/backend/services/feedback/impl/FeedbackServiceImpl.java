package com.locarie.backend.services.feedback.impl;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.services.feedback.FeedbackService;
import com.locarie.backend.services.mail.MailService;
import com.locarie.backend.services.utils.UserFindUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {
  @Value("${spring.mail.username}")
  private String to;

  private final UserFindUtils userFindUtils;
  private final MailService mailService;

  public FeedbackServiceImpl(UserFindUtils userFindUtils, MailService mailService) {
    this.userFindUtils = userFindUtils;
    this.mailService = mailService;
  }

  @Override
  public boolean sendFeedback(Long userId, String content) {
    UserEntity user = userFindUtils.findUserById(userId);
    String subject = String.format("Feedback from @%s (%s)", user.getUsername(), user.getEmail());
    mailService.sendMail(to, subject, content);
    return true;
  }
}
