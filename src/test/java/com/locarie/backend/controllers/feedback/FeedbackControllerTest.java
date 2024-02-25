package com.locarie.backend.controllers.feedback;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FeedbackControllerTest {
  private static String ENDPOINT = "/api/v1/feedback";

  @Autowired private MockMvc mockMvc;
  @Autowired private UserTestsDataCreator dataCreator;
  @Autowired private ResultExpectUtil expectUtil;

  @Test
  void testSendFeedbackShouldSucceed() throws Exception {
    Long userId = dataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    String content = "This is my feedback.";
    MockHttpServletRequestBuilder request = givenSendFeedbackRequest(userId, content);
    ResultActions result = mockMvc.perform(request);
    expectUtil.thenResultShouldBeCreated(result);
  }

  private MockHttpServletRequestBuilder givenSendFeedbackRequest(Long userId, String content) {
    return MockMvcRequestBuilders.post(ENDPOINT)
        .params(prepareSendFeedbackRequestParams(userId, content));
  }

  private MultiValueMap<String, String> prepareSendFeedbackRequestParams(
      Long userId, String content) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("userId", userId.toString());
    params.add("content", content);
    return params;
  }
}
