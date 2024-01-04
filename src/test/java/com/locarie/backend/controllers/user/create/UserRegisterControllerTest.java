package com.locarie.backend.controllers.user.create;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.datacreators.user.UserRegistrationDtoCreator;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;
import com.locarie.backend.global.ResultCode;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserRegisterControllerTest {
  private static final String ENDPOINT = "/api/v1/users/register";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void testRegisterPlainUserShouldSucceed() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    MockHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeCreated(resultActions);
    thenRegisterResultShouldContainUser(resultActions, userRegistrationDto);
  }

  @Test
  void testRegisterBusinessUserShouldSucceed() throws Exception {
    UserRegistrationDto userRegistrationDto = givenBusinessUserRegistrationDto();
    MockHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeCreated(resultActions);
    thenRegisterResultShouldContainUser(resultActions, userRegistrationDto);
  }

  @Test
  void testRegisterDuplicateUserShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    MockHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeCreated(resultActions);
    thenRegisterResultShouldContainUser(resultActions, userRegistrationDto);

    resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultShouldBeUserAlreadyExists(resultActions);
  }

  @Test
  void testRegisterUserWithInvalidTypeShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDtoWithInvalidType();
    MockHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeBadRequest(resultActions);
    thenRegisterResultStatusShouldBeInvalidParameters(resultActions);
    thenRegisterResultMessageContainType(resultActions);
  }

  @Test
  void testRegisterUserWithInvalidUsernameShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDtoWithInvalidUsername();
    MockHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeBadRequest(resultActions);
    thenRegisterResultStatusShouldBeInvalidParameters(resultActions);
    thenRegisterResultMessageContainUsername(resultActions);
  }

  @Test
  void testRegisterUserWithInvalidEmailShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDtoWithInvalidEmail();
    MockHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
    ResultActions resultActions = whenPerformUserRegisterRequest(request);
    thenRegisterResultStatusShouldBeBadRequest(resultActions);
    thenRegisterResultStatusShouldBeInvalidParameters(resultActions);
    thenRegisterResultMessageContainEmail(resultActions);
  }

  @Test
  void testRegisterUserWithInvalidPasswordShouldFail() throws Exception {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDtoWithInvalidPassword();
    MockHttpServletRequestBuilder request = givenUserRegisterRequest(userRegistrationDto);
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

  private MockHttpServletRequestBuilder givenUserRegisterRequest(UserRegistrationDto dto)
      throws JsonProcessingException {
    String jsonContent = convertRegisterDtoToJsonString(dto);
    return MockMvcRequestBuilders.post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonContent);
  }

  private ResultActions whenPerformUserRegisterRequest(MockHttpServletRequestBuilder request)
      throws Exception {
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

  private String convertRegisterDtoToJsonString(UserRegistrationDto dto)
      throws JsonProcessingException {
    return objectMapper.writeValueAsString(dto);
  }
}
