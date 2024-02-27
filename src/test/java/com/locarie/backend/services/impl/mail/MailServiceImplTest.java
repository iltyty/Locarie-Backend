package com.locarie.backend.services.impl.mail;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.locarie.backend.services.mail.MailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailServiceImplTest {
  @Value("${spring.mail.username}")
  private String from;

  @Autowired private MailService underTests;

  @Test
  @Disabled
  void testSendEmailShouldSucceed() {
    assertDoesNotThrow(
        () ->
            underTests.sendMail(
                from, "Test title from Locarie!", "Hello, this is locarie community!"));
  }
}
