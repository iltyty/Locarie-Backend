package com.locarie.backend.controllers.user.images;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.locarie.backend.datacreators.image.MockImageCreator;
import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.storage.config.StorageConfig;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserProfileImagesControllerTest {
  @Autowired private StorageConfig storageConfig;
  @Autowired private UserTestsDataCreator userDataCreator;
  @Autowired private MockMvc mockMvc;
  @Autowired private ResultExpectUtil resultExpectUtil;

  private static String imageUploadEndpoint(Long userId) {
    return String.format("/api/v1/users/%d/profile-images", userId);
  }

  public static String imageGetEndpoint(Long userId) {
    return String.format("/api/v1/users/%s/profile-images", userId);
  }

  @Test
  void testUploadProfileImagesShouldSucceed() throws Exception {
    Long userId = userDataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    MockMultipartFile[] images = MockImageCreator.profileImages();
    MockMultipartHttpServletRequestBuilder request =
        givenProfileImagesUploadRequest(userId, images);
    ResultActions result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
  }

  @Test
  void testGetProfileImagesShouldSucceed() throws Exception {
    Long userId = userDataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    MockMultipartFile[] images = MockImageCreator.profileImages();
    MockMultipartHttpServletRequestBuilder uploadRequest =
        givenProfileImagesUploadRequest(userId, images);
    whenPerformRequest(uploadRequest);

    MockHttpServletRequestBuilder request = givenProfileImagesGetRequest(userId);
    ResultActions result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
    result.andExpect(jsonPath("$.data").isArray());
    for (int i = 0; i < images.length; i++) {
      String expectedImageUrl =
          String.format(
              "%suser_%d/profile_images/%s",
              storageConfig.getUrlPrefix(), userId, images[i].getOriginalFilename());
      result.andExpect(jsonPath("$.data[" + i + "]").value(expectedImageUrl));
    }
  }

  private MockMultipartHttpServletRequestBuilder givenProfileImagesUploadRequest(
      Long userId, MockMultipartFile[] images) {
    String endpoint = imageUploadEndpoint(userId);
    MockMultipartHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart(endpoint);
    for (MockMultipartFile image : images) {
      request.file(image);
    }
    return request;
  }

  private MockHttpServletRequestBuilder givenProfileImagesGetRequest(Long userId) {
    return MockMvcRequestBuilders.get(imageGetEndpoint(userId));
  }

  private ResultActions whenPerformRequest(MockHttpServletRequestBuilder request) throws Exception {
    return mockMvc.perform(request);
  }
}
