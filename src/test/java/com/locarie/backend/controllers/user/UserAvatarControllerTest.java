package com.locarie.backend.controllers.user;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.utils.user.UserControllerResultMatcherUtil;
import com.locarie.backend.utils.user.UserEntityCreator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserAvatarControllerTest {
  private static final MockMultipartFile AVATAR =
      new MockMultipartFile("avatar", "avatar.png", "image/png", new byte[1]);

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;

  private static String getEndpoint(Long userId) {
    return String.format("/api/v1/users/%d/avatar", userId);
  }

  @Test
  @Disabled
  void testUploadAvatarShouldSucceed() throws Exception {
    Long userId = givenUserIdAfterCreated();
    MockMultipartFile avatar = givenAvatar();
    MockHttpServletRequestBuilder request = givenRequest(userId, avatar);
    ResultActions result = whenPerformUpdateAvatarRequest(request);
    thenResultShouldBeOk(result);
  }

  private Long givenUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity).getId();
  }

  private MockMultipartFile givenAvatar() {
    return AVATAR;
  }

  private MockHttpServletRequestBuilder givenRequest(Long userId, MockMultipartFile avatar) {
    String endpoint = getEndpoint(userId);
    return MockMvcRequestBuilders.multipart(endpoint).file(avatar);
  }

  private ResultActions whenPerformUpdateAvatarRequest(MockHttpServletRequestBuilder request)
      throws Exception {
    return mockMvc.perform(request);
  }

  private void thenResultShouldBeOk(ResultActions result) throws Exception {
    result
        .andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess())
        .andExpect(UserControllerResultMatcherUtil.resultMessageShouldBeSuccess());
  }
}
