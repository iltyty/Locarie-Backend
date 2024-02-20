package com.locarie.backend.controllers.user.read;

import static com.locarie.backend.utils.matchers.UserControllerResultMatcherUtil.*;
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
  private static final String ENDPOINT = "/api/v1/users";
  private static final Mapper<UserEntity, UserDto> mapper = new UserEntityDtoMapperImpl();

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private ResultExpectUtil resultExpectUtil;

  @Test
  void testListShouldSucceed() throws Exception {
    List<UserDto> dtos = givenUserRegistrationDtosAfterCreated();
    MockHttpServletRequestBuilder request = givenListRequest();
    ResultActions result = whenPerformListRequest(request);
    resultExpectUtil.thenResultShouldBeOk(result);
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

  private void thenListResultShouldContainDtos(ResultActions result, List<UserDto> dtos)
      throws Exception {
    result
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data.length()").value(dtos.size()));

    for (int i = 0; i < dtos.size(); i++) {
      UserDto dto = dtos.get(i);
      result.andExpect(jsonPath("$.data[" + i + "].id").value(dto.getId()));
    }
  }
}
