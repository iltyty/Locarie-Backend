package com.locarie.backend.services.mail.impl;

import com.locarie.backend.services.mail.MailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MailServiceImpl implements MailService {
  @Value("${spring.mail.username}")
  private String from;

  private final JavaMailSender mailSender;

  public MailServiceImpl(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Async("emailSendingTaskExecutor")
  @Override
  public void sendMail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    try {
      mailSender.send(message);
    } catch (MailException e) {
      log.error(e);
    }
  }
}
