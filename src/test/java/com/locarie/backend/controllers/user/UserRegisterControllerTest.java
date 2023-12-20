package com.locarie.backend.controllers.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.datacreators.user.UserRegistrationDtoCreator;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.global.ResultCode;
import com.locarie.backend.utils.UserControllerResultMatcherUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserRegisterControllerTest {
  private static final String ENDPOINT = "/api/v1/users/register";

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired private MockMvc mockMvc;

  @Test
  void testRegisterPlainUserShouldSucceed() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    MockMultipartHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeCreated(resultActions);
    thenRegisterResultShouldContainUser(resultActions, userRegistrationDto);
  }

  @Test
  void testRegisterBusinessUserShouldSucceed() throws Exception {
    UserRegistrationDto userRegistrationDto = givenBusinessUserRegistrationDto();
    MockMultipartHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeCreated(resultActions);
    thenRegisterResultShouldContainUser(resultActions, userRegistrationDto);
  }

  @Test
  void testRegisterDuplicateUserShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    MockMultipartHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeCreated(resultActions);
    thenRegisterResultShouldContainUser(resultActions, userRegistrationDto);

    resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultShouldBeUserAlreadyExists(resultActions);
  }

  @Test
  void testRegisterUserWithInvalidTypeShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDtoWithInvalidType();
    MockMultipartHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeBadRequest(resultActions);
    thenRegisterResultStatusShouldBeInvalidParameters(resultActions);
    thenRegisterResultMessageContainType(resultActions);
  }

  @Test
  void testRegisterUserWithInvalidUsernameShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDtoWithInvalidUsername();
    MockMultipartHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeBadRequest(resultActions);
    thenRegisterResultStatusShouldBeInvalidParameters(resultActions);
    thenRegisterResultMessageContainUsername(resultActions);
  }

  @Test
  void testRegisterUserWithInvalidEmailShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDtoWithInvalidEmail();
    MockMultipartHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeBadRequest(resultActions);
    thenRegisterResultStatusShouldBeInvalidParameters(resultActions);
    thenRegisterResultMessageContainEmail(resultActions);
  }

  @Test
  void testRegisterUserWithInvalidPasswordShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDtoWithInvalidPassword();
    MockMultipartHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeBadRequest(resultActions);
    thenRegisterResultStatusShouldBeInvalidParameters(resultActions);
    thenRegisterResultMessageContainPassword(resultActions);
  }

  private UserRegistrationDto givenPlainUserRegistrationDto() {
    UserRegistrationDto userRegistrationDto = UserRegistrationDtoCreator.plainUserRegistrationDto();
    userRegistrationDto.setId(null);
    return userRegistrationDto;
  }

  private UserRegistrationDto givenBusinessUserRegistrationDto() {
    UserRegistrationDto userRegistrationDto =
        UserRegistrationDtoCreator.businessUserRegistrationDtoJoleneHornsey();
    userRegistrationDto.setId(null);
    return userRegistrationDto;
  }

  private UserRegistrationDto givenPlainUserRegistrationDtoWithInvalidType() {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    userRegistrationDto.setType(null);
    return userRegistrationDto;
  }

  private UserRegistrationDto givenPlainUserRegistrationDtoWithInvalidUsername() {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    userRegistrationDto.setUsername(null);
    return userRegistrationDto;
  }

  private UserRegistrationDto givenPlainUserRegistrationDtoWithInvalidEmail() {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    userRegistrationDto.setEmail(null);
    return userRegistrationDto;
  }

  private UserRegistrationDto givenPlainUserRegistrationDtoWithInvalidPassword() {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    userRegistrationDto.setPassword(null);
    return userRegistrationDto;
  }

  private MockMultipartHttpServletRequestBuilder givenUserRegisterRequest(
      UserRegistrationDto userRegistrationDto) {
    MockPart userPart = createUserPartInRegisterRequest(userRegistrationDto);
    return MockMvcRequestBuilders.multipart(ENDPOINT).part(userPart);
  }

  private static MockPart createUserPartInRegisterRequest(UserRegistrationDto userRegistrationDto) {
    try {
      String userJson = objectMapper.writeValueAsString(userRegistrationDto);
      MockPart mockPart = new MockPart("user", userJson.getBytes());
      mockPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);
      return mockPart;
    } catch (JsonProcessingException e) {
      return new MockPart("user", new byte[0]);
    }
  }

  private ResultActions whenPerformUserRegisterRequest(
      MockMultipartHttpServletRequestBuilder request) throws Exception {
    return mockMvc.perform(request);
  }

  private void thenRegisterResultStatusShouldBeCreated(ResultActions result) throws Exception {
    result.andExpect(MockMvcResultMatchers.status().isCreated());
  }

  private void thenRegisterResultStatusShouldBeOk(ResultActions result) throws Exception {
    result.andExpect(MockMvcResultMatchers.status().isOk());
  }

  private void thenRegisterResultStatusShouldBeBadRequest(ResultActions result) throws Exception {
    result.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  private void thenRegisterResultShouldContainUser(ResultActions result, UserRegistrationDto dto)
      throws Exception {
    result
        .andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess())
        .andExpect(UserControllerResultMatcherUtil.resultMessageShouldBeSuccess())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.type").value(dto.getType().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(dto.getUsername()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(dto.getEmail()));
  }

  private void thenRegisterResultShouldBeUserAlreadyExists(ResultActions result) throws Exception {
    result
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.RC201.getCode()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message").value(ResultCode.RC201.getMessage()));
  }

  private void thenRegisterResultStatusShouldBeInvalidParameters(ResultActions result)
      throws Exception {
    result.andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeInvalidParameters());
  }

  private void thenRegisterResultMessageContainType(ResultActions resultActions) throws Exception {
    resultActions.andExpect(UserControllerResultMatcherUtil.resultMessageShouldContainType());
  }

  private void thenRegisterResultMessageContainUsername(ResultActions resultActions)
      throws Exception {
    resultActions.andExpect(UserControllerResultMatcherUtil.resultMessageShouldContainUsername());
  }

  private void thenRegisterResultMessageContainEmail(ResultActions resultActions) throws Exception {
    resultActions.andExpect(UserControllerResultMatcherUtil.resultMessageShouldContainEmail());
  }

  private void thenRegisterResultMessageContainPassword(ResultActions resultActions)
      throws Exception {
    resultActions.andExpect(UserControllerResultMatcherUtil.resultMessageShouldContainPassword());
  }
}
