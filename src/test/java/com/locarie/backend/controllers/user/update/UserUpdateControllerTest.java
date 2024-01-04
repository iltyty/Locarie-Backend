package com.locarie.backend.controllers.user.update;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.datacreators.user.UserUpdateDtoCreator;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.utils.DataFormatConverter;
import com.locarie.backend.utils.UserControllerResultMatcherUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserUpdateControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;

  private static String getEndpoint(Long userId) {
    return String.format("/api/v1/users/%d", userId);
  }

  @Test
  void testThatUpdateExistedUserShouldSucceed() throws Exception {
    Long id = givenUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenPlainUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(id, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    thenUpdateResultShouldBeSuccess(result);
    thenUpdateResultShouldEqualToUpdateDto(result, userUpdateDto);
  }

  @Test
  void testThatUpdateNonExistedUserShouldFail() throws Exception {
    UserUpdateDto userUpdateDto = givenBusinessUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(0L, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    thenUpdateResultShouldBeNotFound(result);
  }

  private Long givenUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    return userRepository.save(userEntity).getId();
  }

  private UserUpdateDto givenPlainUserUpdateDto() {
    return UserUpdateDtoCreator.plainUserUpdateDto();
  }

  private UserUpdateDto givenBusinessUserUpdateDto() {
    return UserUpdateDtoCreator.businessUserUpdateDto();
  }

  private MockHttpServletRequestBuilder givenUpdateUserRequest(Long id, UserUpdateDto dto)
      throws IllegalAccessException {
    MultiValueMap<String, String> params = convertUpdateDtoToMap(dto);
    String endpoint = getEndpoint(id);
    return MockMvcRequestBuilders.post(endpoint)
        .params(params)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  private ResultActions whenPerformUpdateUserRequest(MockHttpServletRequestBuilder request)
      throws Exception {
    return mockMvc.perform(request);
  }

  private void thenUpdateResultShouldBeSuccess(ResultActions result) throws Exception {
    result
        .andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess())
        .andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess());
  }

  private void thenUpdateResultShouldEqualToUpdateDto(
      ResultActions result, UserUpdateDto userUpdateDto) throws Exception {
    result
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(userUpdateDto.getEmail()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.firstName").value(userUpdateDto.getFirstName()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.lastName").value(userUpdateDto.getLastName()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.username").value(userUpdateDto.getUsername()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.birthday")
                .value(userUpdateDto.getBirthday().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.businessName")
                .value(userUpdateDto.getBusinessName()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.homepageUrl").value(userUpdateDto.getPhone()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.category").value(userUpdateDto.getCategory()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.introduction")
                .value(userUpdateDto.getIntroduction()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value(userUpdateDto.getPhone()));
  }

  private void thenUpdateResultShouldBeNotFound(ResultActions result) throws Exception {
    result.andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  private MultiValueMap<String, String> convertUpdateDtoToMap(UserUpdateDto dto)
      throws IllegalAccessException {
    return DataFormatConverter.objectToMultiValueMap(dto);
  }
}
