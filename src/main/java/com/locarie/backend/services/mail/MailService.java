package com.locarie.backend.services.mail;

public interface MailService {
  void sendMail(String to, String subject, String text);
}
