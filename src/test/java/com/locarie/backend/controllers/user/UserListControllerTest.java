package com.locarie.backend.controllers.user;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.UserEntityDtoMapperImpl;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.utils.UserControllerResultMatcherUtil;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserListControllerTest {
  private static final String ENDPOINT = "/api/v1/users";
  private static final Mapper<UserEntity, UserDto> mapper = new UserEntityDtoMapperImpl();

  @Autowired private MockMvc mockMvc;

  @Autowired UserRepository userRepository;

  @Test
  void testListShouldSucceed() throws Exception {
    List<UserDto> dtos = givenUserRegistrationDtosAfterCreated();
    MockHttpServletRequestBuilder request = givenListRequest();
    ResultActions result = whenPerformListRequest(request);
    thenListResultShouldBeSuccess(result);
    thenListResultShouldContainDtos(result, dtos);
  }

  List<UserDto> givenUserRegistrationDtosAfterCreated() {
    List<UserEntity> userEntities = createUserEntitiesInRepository();
    return userEntities.stream().map(mapper::mapTo).toList();
  }

  MockHttpServletRequestBuilder givenListRequest() {
    return MockMvcRequestBuilders.get(ENDPOINT);
  }

  ResultActions whenPerformListRequest(MockHttpServletRequestBuilder request) throws Exception {
    return mockMvc.perform(request);
  }

  List<UserEntity> createUserEntitiesInRepository() {
    List<UserEntity> userEntities =
        Arrays.asList(
            UserEntityCreator.plainUserEntity(),
            UserEntityCreator.businessUserEntityJoleneHornsey());
    userEntities.forEach(
        userEntity -> {
          UserEntity savedUserEntity = userRepository.save(userEntity);
          userEntity.setId(savedUserEntity.getId());
        });
    return userEntities;
  }

  private void thenListResultShouldBeSuccess(ResultActions result) throws Exception {
    result
        .andExpect(UserControllerResultMatcherUtil.resultStatusCodeShouldBeSuccess())
        .andExpect(UserControllerResultMatcherUtil.resultMessageShouldBeSuccess());
  }

  private void thenListResultShouldContainDtos(ResultActions result, List<UserDto> dtos)
      throws Exception {
    result
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(dtos.size()));

    for (int i = 0; i < dtos.size(); i++) {
      UserDto dto = dtos.get(i);
      result
          .andExpect(MockMvcResultMatchers.jsonPath("$.data[" + i + "].id").value(dto.getId()));
    }
  }
}
