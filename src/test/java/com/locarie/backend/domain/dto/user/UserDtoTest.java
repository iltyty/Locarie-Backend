package com.locarie.backend.domain.dto.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.locarie.backend.datacreators.user.UserDtoCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDtoTest {
  public final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void registerTimeModule() {
    mapper.registerModule(new JavaTimeModule());
  }

  @Test
  void testBirthdaySerialization() throws JsonProcessingException {
    UserDto dto = UserDtoCreator.businessUserDtoShreeji();
    System.out.println(mapper.writeValueAsString(dto));
  }
}