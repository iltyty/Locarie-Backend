package com.locarie.backend.controllers.user.update;

import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.datacreators.user.UserUpdateDtoCreator;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.utils.UserControllerResultMatcherUtil;
import jakarta.transaction.Transactional;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
@Transactional
@AutoConfigureMockMvc
public class UserUpdateControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private ObjectMapper objectMapper;

  private static String getEndpoint(Long userId) {
    return String.format("/api/v1/users/%d", userId);
  }

  @Test
  void testFullUpdateExistedPlainUserShouldSucceed() throws Exception {
    Long id = givenPlainUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenFullPlainUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(id, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    thenUpdateResultShouldBeSuccess(result);
    thenUpdateResultShouldFullyEqualToPlainUserUpdateDto(result, userUpdateDto);
  }

  @Test
  void testPartialUpdateExistedPlainUserShouldSucceed() throws Exception {
    Long id = givenPlainUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenPartialPlainUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(id, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    thenUpdateResultShouldBeSuccess(result);
    thenUpdateResultShouldPartiallyEqualToUserUpdateDto(result, userUpdateDto);
  }

  @Test
  void testFullUpdateExistedBusinessUserShouldSucceed() throws Exception {
    Long id = givenBusinessUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenFullBusinessUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(id, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    thenUpdateResultShouldBeSuccess(result);
    thenUpdateResultShouldFullyEqualToBusinessUserUpdateDto(result, userUpdateDto);
  }

  @Test
  void testPartiallyUpdateExistedBusinessUserShouldSucceed() throws Exception {
    Long id = givenBusinessUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenPartialBusinessUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(id, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    thenUpdateResultShouldBeSuccess(result);
    thenUpdateResultShouldPartiallyEqualToUserUpdateDto(result, userUpdateDto);
  }

  @Test
  void testFullUpdateNonExistedUserShouldFail() throws Exception {
    UserUpdateDto userUpdateDto = givenFullBusinessUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(0L, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    thenUpdateResultShouldBeNotFound(result);
  }

  private Long givenPlainUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity).getId();
  }

  private Long givenBusinessUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    return userRepository.save(userEntity).getId();
  }

  private UserUpdateDto givenFullPlainUserUpdateDto() {
    return UserUpdateDtoCreator.fullPlainUserUpdateDto();
  }

  private UserUpdateDto givenPartialPlainUserUpdateDto() {
    return UserUpdateDtoCreator.partialPlainUserUpdateDto();
  }

  private UserUpdateDto givenFullBusinessUserUpdateDto() {
    return UserUpdateDtoCreator.fullBusinessUserUpdateDto();
  }

  private UserUpdateDto givenPartialBusinessUserUpdateDto() {
    return UserUpdateDtoCreator.partialBusinessUserUpdateDto();
  }

  private MockHttpServletRequestBuilder givenUpdateUserRequest(Long id, UserUpdateDto dto)
      throws JsonProcessingException {
    String jsonContent = convertUpdateDtoToJsonString(dto);
    String endpoint = getEndpoint(id);
    return MockMvcRequestBuilders.post(endpoint)
        .content(jsonContent)
        .contentType(MediaType.APPLICATION_JSON);
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

  private void thenUpdateResultShouldFullyEqualToPlainUserUpdateDto(
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
                .value(userUpdateDto.getBirthday().toString()));
  }

  private void thenUpdateResultShouldFullyEqualToBusinessUserUpdateDto(
      ResultActions result, UserUpdateDto userUpdateDto) throws Exception {
    expectStringPropertiesEquals(result, userUpdateDto);
    expectBirthdayEquals(result, userUpdateDto);
    expectBusinessHoursEquals(result, userUpdateDto);
  }

  private void expectStringPropertiesEquals(ResultActions result, UserUpdateDto userUpdateDto)
      throws Exception {
    for (Field field : userUpdateDto.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      Object value = field.get(userUpdateDto);
      if (value instanceof String) {
        result.andExpect(MockMvcResultMatchers.jsonPath("$.data." + field.getName()).value(value));
      }
    }
  }

  private void expectBirthdayEquals(ResultActions result, UserUpdateDto userUpdateDto)
      throws Exception {
    result.andExpect(
        MockMvcResultMatchers.jsonPath("$.data.birthday")
            .value(userUpdateDto.getBirthday().toString()));
  }

  private void expectBusinessHoursEquals(ResultActions result, UserUpdateDto userUpdateDto)
      throws Exception {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    result.andExpect(
        MockMvcResultMatchers.jsonPath(
            "$.data.businessHours", hasSize(userUpdateDto.getBusinessHours().size())));
    for (int i = 0; i < userUpdateDto.getBusinessHours().size(); i++) {
      LocalTime openingTime = userUpdateDto.getBusinessHours().get(i).getOpeningTime();
      LocalTime closingTime = userUpdateDto.getBusinessHours().get(i).getClosingTime();
      String openingTimeString = openingTime == null ? null : openingTime.format(formatter);
      String closingTimeString = closingTime == null ? null : closingTime.format(formatter);
      result
          .andExpect(
              MockMvcResultMatchers.jsonPath("$.data.businessHours[" + i + "].dayOfWeek")
                  .value(userUpdateDto.getBusinessHours().get(i).getDayOfWeek().toString()))
          .andExpect(
              MockMvcResultMatchers.jsonPath("$.data.businessHours[" + i + "].closed")
                  .value(userUpdateDto.getBusinessHours().get(i).getClosed()))
          .andExpect(
              MockMvcResultMatchers.jsonPath("$.data.businessHours[" + i + "].openingTime")
                  .value(openingTimeString))
          .andExpect(
              MockMvcResultMatchers.jsonPath("$.data.businessHours[" + i + "].closingTime")
                  .value(closingTimeString));
    }
  }

  private void thenUpdateResultShouldPartiallyEqualToUserUpdateDto(
      ResultActions result, UserUpdateDto userUpdateDto) throws Exception {
    for (Field field : userUpdateDto.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      Object value = field.get(userUpdateDto);
      if (value != null) {
        if (value instanceof List) {
          expectBusinessHoursEquals(result, userUpdateDto);
        } else {
          result.andExpect(
              MockMvcResultMatchers.jsonPath("$.data." + field.getName()).value(value.toString()));
        }
      }
    }
  }

  private void thenUpdateResultShouldBeNotFound(ResultActions result) throws Exception {
    result.andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  private String convertUpdateDtoToJsonString(UserUpdateDto dto) throws JsonProcessingException {
    return objectMapper.writeValueAsString(dto);
  }
}
