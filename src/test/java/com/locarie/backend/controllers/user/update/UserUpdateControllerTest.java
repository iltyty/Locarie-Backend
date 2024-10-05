package com.locarie.backend.controllers.user.update;

import static com.locarie.backend.utils.matchers.UserControllerResultMatcherUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.datacreators.user.UserUpdateDtoCreator;
import com.locarie.backend.domain.dto.businesshours.BusinessHoursDto;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
import jakarta.transaction.Transactional;
import java.lang.reflect.Field;
import java.time.LocalTime;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserUpdateControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private ResultExpectUtil resultExpectUtil;

  private static String getEndpoint(Long userId) {
    return String.format("/api/v1/users/%d", userId);
  }

  @Test
  void testFullUpdateExistedPlainUserShouldSucceed() throws Exception {
    Long id = givenPlainUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenFullPlainUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(id, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenUpdateResultShouldFullyEqualToPlainUserUpdateDto(result, userUpdateDto);
  }

  @Test
  void testPartialUpdateExistedPlainUserShouldSucceed() throws Exception {
    Long id = givenPlainUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenPartialPlainUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(id, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenUpdateResultShouldPartiallyEqualToUserUpdateDto(result, userUpdateDto);
  }

  @Test
  void testFullUpdateExistedBusinessUserShouldSucceed() throws Exception {
    Long id = givenBusinessUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenFullBusinessUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(id, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenUpdateResultShouldFullyEqualToBusinessUserUpdateDto(result, userUpdateDto);
  }

  @Test
  void testPartiallyUpdateExistedBusinessUserShouldSucceed() throws Exception {
    Long id = givenBusinessUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenPartialBusinessUserUpdateDto();
    MockHttpServletRequestBuilder request = givenUpdateUserRequest(id, userUpdateDto);
    ResultActions result = whenPerformUpdateUserRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
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

  private void thenUpdateResultShouldFullyEqualToPlainUserUpdateDto(
      ResultActions result, UserUpdateDto userUpdateDto) throws Exception {
    result
        .andExpect(jsonPath("$.data.email").value(userUpdateDto.getEmail()))
        .andExpect(jsonPath("$.data.firstName").value(userUpdateDto.getFirstName()))
        .andExpect(jsonPath("$.data.lastName").value(userUpdateDto.getLastName()))
        .andExpect(jsonPath("$.data.username").value(userUpdateDto.getUsername()))
        .andExpect(jsonPath("$.data.birthday").value(userUpdateDto.getBirthday().toString()));
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
        result.andExpect(jsonPath("$.data." + field.getName()).value(value));
      }
    }
  }

  private void expectBirthdayEquals(ResultActions result, UserUpdateDto userUpdateDto)
      throws Exception {
    result.andExpect(jsonPath("$.data.birthday").value(userUpdateDto.getBirthday().toString()));
  }

  private void expectBusinessHoursEquals(ResultActions result, UserUpdateDto userUpdateDto)
      throws Exception {
    result.andExpect(
        jsonPath("$.data.businessHours", hasSize(userUpdateDto.getBusinessHours().size())));
    List<BusinessHoursDto> businessHoursDtos = userUpdateDto.getBusinessHours();
    for (int i = 0; i < businessHoursDtos.size(); i++) {
      BusinessHoursDto businessHours = businessHoursDtos.get(i);
      List<LocalTime> openingTime = businessHours.getOpeningTime();
      List<LocalTime> closingTime = businessHours.getClosingTime();
      result
          .andExpect(
              jsonPath("$.data.businessHours[" + i + "].dayOfWeek")
                  .value(businessHours.getDayOfWeek().getValue()))
          .andExpect(
              jsonPath("$.data.businessHours[" + i + "].closed").value(businessHours.getClosed()));
      if (!businessHours.getClosed()) {
        result.andExpect(jsonPath("$.data.businessHours[" + i + "].openingTime", hasSize(openingTime.size())));
        result.andExpect(jsonPath("$.data.businessHours[" + i + "].closingTime", hasSize(closingTime.size())));
        for (int j = 0; j < openingTime.size(); j++) {
          result.andExpect(jsonPath("$.data.businessHours[" + i + "].openingTime[" + j + "].hour").value(openingTime.get(j).getHour()));
          result.andExpect(jsonPath("$.data.businessHours[" + i + "].openingTime[" + j + "].hour").value(openingTime.get(j).getMinute()));
          result.andExpect(jsonPath("$.data.businessHours[" + i + "].closingTime[" + j + "].hour").value(closingTime.get(j).getHour()));
          result.andExpect(jsonPath("$.data.businessHours[" + i + "].closingTime[" + j + "].hour").value(closingTime.get(j).getMinute()));
        }
      }
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
          result.andExpect(jsonPath("$.data." + field.getName()).value(value.toString()));
        }
      }
    }
  }

  private void thenUpdateResultShouldBeNotFound(ResultActions result) throws Exception {
    result.andExpect(status().isNotFound());
  }

  private String convertUpdateDtoToJsonString(UserUpdateDto dto) throws JsonProcessingException {
    return objectMapper.writeValueAsString(dto);
  }
}
