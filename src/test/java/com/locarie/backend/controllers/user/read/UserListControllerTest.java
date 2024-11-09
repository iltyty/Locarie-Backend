package com.locarie.backend.controllers.user.read;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.user.UserEntityDtoMapperImpl;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.utils.expecters.ResultExpectUtil;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserListControllerTest {
  private static final String LIST_ALL_ENDPOINT = "/api/v1/users";
  private static final String LIST_BUSINESSES_ENDPOINT = "/api/v1/users/businesses";
  private static final String LIST_ALL_BUSINESSES_ENDPOINT = "/api/v1/users/businesses/all";
  private static final Mapper<UserEntity, UserDto> mapper = new UserEntityDtoMapperImpl();

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private ResultExpectUtil resultExpectUtil;

  @Test
  void testList() throws Exception {
    List<UserDto> dtos = givenUserRegistrationDtosAfterCreated();
    MockHttpServletRequestBuilder request = givenListRequest(0, 2);
    ResultActions result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListUsersResultShouldContainDtos(result, dtos.subList(0, 2));

    request = givenListRequest(1, 1);
    result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListUsersResultShouldContainDtos(result, dtos.subList(1, 2));
  }

  @Test
  void testListBusinessesShouldSucceed() throws Exception {
    List<UserDto> dtos = givenUserRegistrationDtosAfterCreated();
    MockHttpServletRequestBuilder request = givenListBusinessesRequest();
    ResultActions result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListBusinessesResultShouldBeExact(result, dtos.subList(1, 3));
  }

  @Test
  void testListAllBusinesses() throws Exception {
    List<UserDto> dtos = givenUserRegistrationDtosAfterCreated();
    MockHttpServletRequestBuilder request = givenListAllBusinessesRequest();
    ResultActions result = whenPerformRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
    thenListAllBusinessesResultShouldBeExact(result, dtos.subList(1, 3));
  }

  List<UserDto> givenUserRegistrationDtosAfterCreated() {
    List<UserEntity> userEntities = createUserEntitiesInRepository();
    return userEntities.stream().map(mapper::mapTo).toList();
  }

  MockHttpServletRequestBuilder givenListRequest(int page, int pageSize) {
    return MockMvcRequestBuilders.get(LIST_ALL_ENDPOINT)
        .param("page", String.valueOf(page))
        .param("size", String.valueOf(pageSize));
  }

  MockHttpServletRequestBuilder givenListBusinessesRequest() {
    return MockMvcRequestBuilders.get(LIST_BUSINESSES_ENDPOINT);
  }

  MockHttpServletRequestBuilder givenListAllBusinessesRequest() {
    return MockMvcRequestBuilders.get(LIST_ALL_BUSINESSES_ENDPOINT);
  }

  ResultActions whenPerformRequest(MockHttpServletRequestBuilder request) throws Exception {
    return mockMvc.perform(request);
  }

  List<UserEntity> createUserEntitiesInRepository() {
    List<UserEntity> userEntities =
        Arrays.asList(
            UserEntityCreator.plainUserEntity(),
            UserEntityCreator.businessUserEntityShreeji(),
            UserEntityCreator.businessUserEntityJoleneHornsey());
    userEntities.forEach(
        userEntity -> {
          UserEntity savedUserEntity = userRepository.save(userEntity);
          userEntity.setId(savedUserEntity.getId());
        });
    return userEntities;
  }

  private void thenListUsersResultShouldContainDtos(ResultActions result, List<UserDto> dtos)
      throws Exception {
    resultBeOfSize(result, "$.data.content", dtos.size());
    for (int i = 0; i < dtos.size(); i++) {
      UserDto dto = dtos.get(i);
      result.andExpect(jsonPath("$.data.content[" + i + "].id").value(dto.getId()));
    }
  }

  private void thenListBusinessesResultShouldBeExact(ResultActions result, List<UserDto> dtos)
      throws Exception {
    resultBeOfSize(result, "$.data.content", dtos.size());
    for (int i = 0; i < dtos.size(); i++) {
      UserDto dto = dtos.get(i);
      result.andExpect(jsonPath("$.data.content[" + i + "].id").value(dto.getId()));
      result.andExpect(jsonPath("$.data.content[" + i + "].avatarUrl").value(dto.getAvatarUrl()));
      result.andExpect(
          jsonPath("$.data.content[" + i + "].businessName").value(dto.getBusinessName()));
    }
  }

  private void thenListAllBusinessesResultShouldBeExact(ResultActions result, List<UserDto> dtos)
      throws Exception {
    resultBeOfSize(result, "$.data", dtos.size());
    for (int i = 0; i < dtos.size(); i++) {
      UserDto dto = dtos.get(i);
      result.andExpect(jsonPath("$.data.[" + i + "].id").value(dto.getId()));
      result.andExpect(jsonPath("$.data.[" + i + "].avatarUrl").value(dto.getAvatarUrl()));
      result.andExpect(
          jsonPath("$.data.[" + i + "].location.latitude").value(dto.getLocation().getY()));
      result.andExpect(
          jsonPath("$.data.[" + i + "].location.longitude").value(dto.getLocation().getX()));
    }
  }

  private void resultBeOfSize(ResultActions result, String jsonPath, int size) throws Exception {
    result
        .andExpect(jsonPath(jsonPath).isArray())
        .andExpect(jsonPath(jsonPath + ".length()").value(size));
  }
}
