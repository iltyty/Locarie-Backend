package com.locarie.backend.controllers.user.login;

import static com.locarie.backend.utils.matchers.UserControllerResultMatcherUtil.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.datacreators.user.UserLoginRequestDtoCreator;
import com.locarie.backend.domain.dto.user.UserLoginRequestDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.utils.DataFormatConverter;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
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
import org.springframework.util.MultiValueMap;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserLoginControllerTest {
  private static final String ENDPOINT = "/api/v1/users/login";

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private ResultExpectUtil resultExpectUtil;

  @Test
  void testLoginShouldSucceed() throws Exception {
    UserLoginRequestDto userRegistrationDto = givenUserLoginRequestDtoAfterCreated();
    MockHttpServletRequestBuilder request = givenLoginRequest(userRegistrationDto);
    ResultActions result = whenPerformLoginRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenLoginResultShouldContainValidData(result);
  }

  private UserLoginRequestDto givenUserLoginRequestDtoAfterCreated() {
    createBusinessUserInRepository();
    return UserLoginRequestDtoCreator.businessUserLoginRequestDtoJoleneHornsey();
  }

  private MockHttpServletRequestBuilder givenLoginRequest(UserLoginRequestDto dto)
      throws IllegalAccessException {
    MultiValueMap<String, String> params = convertLoginDtoToMap(dto);
    return MockMvcRequestBuilders.post(ENDPOINT)
        .params(params)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  private ResultActions whenPerformLoginRequest(MockHttpServletRequestBuilder request)
      throws Exception {
    return mockMvc.perform(request);
  }

  private void thenLoginResultShouldContainValidData(ResultActions result) throws Exception {
    result
        .andExpect(jsonPath("$.data.id").isNumber())
        .andExpect(jsonPath("$.data.username").isString())
        .andExpect(jsonPath("$.data.username").isNotEmpty())
        .andExpect(jsonPath("$.data.jwtToken").isString())
        .andExpect(jsonPath("$.data.jwtToken").isNotEmpty());
  }

  private void createBusinessUserInRepository() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    userRepository.save(userEntity);
  }

  private MultiValueMap<String, String> convertLoginDtoToMap(UserLoginRequestDto dto)
      throws IllegalAccessException {
    return DataFormatConverter.objectToMultiValueMap(dto);
  }
}
