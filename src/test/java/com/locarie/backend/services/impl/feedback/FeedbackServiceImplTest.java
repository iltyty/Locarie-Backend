package com.locarie.backend.services.impl.feedback;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.services.feedback.impl.FeedbackServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class FeedbackServiceImplTest {
  @Autowired private FeedbackServiceImpl underTests;
  @Autowired private UserTestsDataCreator dataCreator;

  @Test
  void testSendFeedbackShouldSucceed() {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    assertDoesNotThrow(() -> underTests.sendFeedback(userId, "Hello, this is my feedback!"));
  }
}
