package com.locarie.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private String registerPlainUser() throws Exception {
        UserRegistrationDto dto = TestDataUtil.newPlainUserRegistrationDto();
        dto.setId(null);
        String userJson = mapper.writeValueAsString(dto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
        return userJson;
    }

    private String registerBusinessUserJoleneHornsey() throws Exception {
        UserRegistrationDto dto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        dto.setId(null);
        String userJson = mapper.writeValueAsString(dto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
        return userJson;
    }

    @Test
    void testUserSerialization() throws JsonProcessingException {
        UserEntity user = TestDataUtil.newBusinessUserEntityJoleneHornsey();
        String userJson = mapper.writeValueAsString(user);
        UserEntity deserializedUser = mapper.readValue(userJson, UserEntity.class);
        assertThat(user).isEqualTo(deserializedUser);
    }

    @Test
    void testRegisterReturnsHttpCreated() throws Exception {
        registerPlainUser();
    }

    @Test
    void testRegisterReturnsUser() throws Exception {
        UserRegistrationDto dto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        dto.setId(null);
        String userJson = mapper.writeValueAsString(dto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("Jolene Hornsey")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location.latitude")
                        .value(is(dto.getLocation().getY()), Double.class)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location.longitude")
                        .value(is(dto.getLocation().getX()), Double.class)
        );
    }

    @Test
    void testLoginReturnsHttpOk() throws Exception {
        String userJson = registerPlainUser();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testLoginReturnsJwtToken() throws Exception {
        String userJson = registerBusinessUserJoleneHornsey();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isString()
        );
    }

    @Test
    void testListUsersReturnsHttpOk() throws Exception {
        String userJson1 = registerPlainUser();
        String userJson2 = registerBusinessUserJoleneHornsey();
        UserDto userDto1 = mapper.readValue(userJson1, UserDto.class);
        UserDto userDto2 = mapper.readValue(userJson2, UserDto.class);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].username").value(userDto1.getUsername())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value(userDto1.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].password").value(userDto1.getPassword())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].avatarUrl").value(userDto1.getAvatarUrl())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].username").value(userDto2.getUsername())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].location.latitude")
                        .value(is(userDto2.getLocation().getY()), Double.class)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].location.longitude")
                        .value(is(userDto2.getLocation().getX()), Double.class)
        );
    }

    @Test
    void testGetUserReturnsHttpOk() throws Exception {
        registerPlainUser();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/1")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void testGetUserReturnsHttpNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void testGetUserReturnsUser() throws Exception {
        String userJson = registerBusinessUserJoleneHornsey();
        UserRegistrationDto dto = mapper.readValue(userJson, UserRegistrationDto.class);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/1")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value(dto.getUsername())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location.latitude")
                        .value(is(dto.getLocation().getY()), Double.class)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location.longitude")
                        .value(is(dto.getLocation().getX()), Double.class)
        );
    }
}