package com.locarie.backend.controllers.user.avatar;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserAvatarService;
import com.locarie.backend.utils.UserControllerResultMatcherUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

  @Qualifier("UserAvatarService") @Autowired
  private UserAvatarService userAvatarService;

  private static String getEndpoint(Long userId) {
    return String.format("/api/v1/users/%d/avatar", userId);
  }

  @Test
  void testUploadAvatarShouldSucceed() throws Exception {
    Long userId = givenUserEntityAfterCreated().getId();
    MockMultipartFile avatar = givenAvatar();
    MockHttpServletRequestBuilder request = givenUploadAvatarRequest(userId, avatar);
    ResultActions result = whenPerformHttpRequest(request);
    thenResultShouldBeOk(result);
  }

  private UserEntity givenUserEntityAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity);
  }

  private MockMultipartFile givenAvatar() {
    return AVATAR;
  }

  private MockHttpServletRequestBuilder givenUploadAvatarRequest(
      Long userId, MockMultipartFile avatar) {
    String endpoint = getEndpoint(userId);
    return MockMvcRequestBuilders.multipart(endpoint).file(avatar);
  }

  private UserDto whenUploadAvatar(Long userId) {
    return userAvatarService.updateAvatar(userId, UserAvatarControllerTest.AVATAR);
  }

  private ResultActions whenPerformHttpRequest(MockHttpServletRequestBuilder request)
      throws Exception {
    return mockMvc.perform(request);
  }

  private void thenResultShouldBeOk(ResultActions result) throws Exception {
    result
        .andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess())
        .andExpect(UserControllerResultMatcherUtil.resultMessageShouldBeSuccess());
  }
}
