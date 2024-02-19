package com.locarie.backend.controllers.user.images;

import com.locarie.backend.datacreators.image.MockImageCreator;
import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.utils.matchers.ControllerResultMatcherUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserProfileImagesControllerTest {
  @Autowired private UserTestsDataCreator userDataCreator;
  @Autowired private MockMvc mockMvc;

  private static String getEndpoint(Long userId) {
    return String.format("/api/v1/users/%d/profile-images", userId);
  }

  @Test
  void testUploadProfileImagesShouldSucceed() throws Exception {
    Long userId = userDataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    MockMultipartFile[] images = MockImageCreator.profileImages();
    MockMultipartHttpServletRequestBuilder request =
        givenProfileImagesUploadRequest(userId, images);
    ResultActions result = whenPerformRequest(request);
    thenResultShouldBeOk(result);
  }

  private MockMultipartHttpServletRequestBuilder givenProfileImagesUploadRequest(
      Long userId, MockMultipartFile[] images) {
    String endpoint = getEndpoint(userId);
    MockMultipartHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart(endpoint);
    for (MockMultipartFile image : images) {
      request.file(image);
    }
    return request;
  }

  private ResultActions whenPerformRequest(MockMultipartHttpServletRequestBuilder request)
      throws Exception {
    return mockMvc.perform(request);
  }

  private void thenResultShouldBeOk(ResultActions result) throws Exception {
    result
        .andExpect(ControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess())
        .andExpect(ControllerResultMatcherUtil.resultMessageShouldBeSuccess());
  }
}
