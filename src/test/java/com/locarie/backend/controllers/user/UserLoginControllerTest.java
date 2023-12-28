package com.locarie.backend.controllers.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.datacreators.user.UserLoginRequestDtoCreator;
import com.locarie.backend.domain.dto.UserLoginRequestDto;
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
public class UserLoginControllerTest {
  private static final String ENDPOINT = "/api/v1/users/login";

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;

  @Test
  void testLoginShouldSucceed() throws Exception {
    UserLoginRequestDto userRegistrationDto = givenUserLoginRequestDtoAfterCreated();
    MockHttpServletRequestBuilder request = givenLoginRequest(userRegistrationDto);
    ResultActions result = whenPerformLoginRequest(request);
    thenLoginResultShouldBeSuccess(result);
    thenLoginResultShouldContainValidData(result);
  }

  private UserLoginRequestDto givenUserLoginRequestDtoAfterCreated() {
    createBusinessUserInRepository();
    return UserLoginRequestDtoCreator.businessUserLoginRequestDtoJoleneHornsey();
  }

  private MockHttpServletRequestBuilder givenLoginRequest(UserLoginRequestDto dto)
      throws IllegalAccessException, JsonProcessingException {
    MultiValueMap<String, String> params = convertLoginDtoToMap(dto);
    return MockMvcRequestBuilders.post(ENDPOINT)
        .params(params)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  private ResultActions whenPerformLoginRequest(MockHttpServletRequestBuilder request)
      throws Exception {
    return mockMvc.perform(request);
  }

  private void thenLoginResultShouldBeSuccess(ResultActions result) throws Exception {
    result
        .andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess())
        .andExpect(UserControllerResultMatcherUtil.resultMessageShouldBeSuccess());
  }

  private void thenLoginResultShouldContainValidData(ResultActions result) throws Exception {
    result
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.jwtToken").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.jwtToken").isNotEmpty());
  }

  private void createBusinessUserInRepository() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    userRepository.save(userEntity);
  }

  private MultiValueMap<String, String> convertLoginDtoToMap(UserLoginRequestDto dto)
      throws IllegalAccessException, JsonProcessingException {
    return DataFormatConverter.objectToMultiValueMap(dto);
  }
}
