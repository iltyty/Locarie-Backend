package com.locarie.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.TestDataUtil;
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

    @Test
    void testUserSerialization() throws JsonProcessingException {
        UserEntity user = TestDataUtil.newBusinessUserJoleneHornsey();
        String userJson = mapper.writeValueAsString(user);
        UserEntity deserializedUser = mapper.readValue(userJson, UserEntity.class);
        assertThat(user).isEqualTo(deserializedUser);
    }

    @Test
    void testRegisterReturnsHttpCreated() throws Exception {
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
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isString()
        );
    }
}