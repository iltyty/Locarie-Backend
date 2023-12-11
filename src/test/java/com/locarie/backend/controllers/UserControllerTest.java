package com.locarie.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.ResponseDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import jakarta.transaction.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired private MockMvc mockMvc;

    private static MockPart createUserPart(UserRegistrationDto dto) throws JsonProcessingException {
        String userJson = mapper.writeValueAsString(dto);
        MockPart userPart = new MockPart("user", userJson.getBytes());
        userPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return userPart;
    }

    private UserRegistrationDto registerPlainUser() throws Exception {
        UserRegistrationDto dto = TestDataUtil.newPlainUserRegistrationDto();
        dto.setId(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/users/register")
                                .part(createUserPart(dto))
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(
                        result -> {
                            String content = result.getResponse().getContentAsString();
                            UserDto savedUserDto = mapper.readValue(content, UserDto.class);
                            dto.setId(savedUserDto.getId());
                        });
        return dto;
    }

    private UserRegistrationDto registerBusinessUserJoleneHornsey() throws Exception {
        UserRegistrationDto dto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        dto.setId(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/users/register")
                                .part(createUserPart(dto))
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(
                        result -> {
                            String content = result.getResponse().getContentAsString();
                            JsonNode dataNode = mapper.readTree(content).get("data");
                            UserDto savedUserDto = mapper.treeToValue(dataNode, UserDto.class);
                            dto.setId(savedUserDto.getId());
                        });
        return dto;
    }

    @Test
    void testUserSerialization() throws JsonProcessingException {
        UserEntity user = TestDataUtil.newBusinessUserEntityJoleneHornsey();
        String userJson = mapper.writeValueAsString(user);
        UserEntity deserializedUser = mapper.readValue(userJson, UserEntity.class);
        assertThat(user).isEqualTo(deserializedUser);
    }

    @Test
    void testRegisterBusinessUserReturnsHttpCreated() throws Exception {
        registerBusinessUserJoleneHornsey();
    }

    @Test
    void testRegisterPlainUserReturnsHttpCreated() throws Exception {
        registerPlainUser();
    }

    @Test
    void testRegisterReturnsValidationError() throws Exception {
        UserRegistrationDto dto = TestDataUtil.newPlainUserRegistrationDto();
        dto.setUsername(null);
        dto.setPassword(null);
        dto.setEmail(null);
        dto.setType(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/users/register")
                                .part(createUserPart(dto))
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(new StringContains("username is mandatory")))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(new StringContains("password is mandatory")))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(new StringContains("email is mandatory")))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(new StringContains("type = PLAIN/BUSINESS is mandatory")));

        dto.setUsername("TempUsername");
        dto.setPassword("12");
        dto.setEmail("TempEmail@Unknown.com");
        dto.setType(UserDto.Type.PLAIN);

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/users/register")
                                .part(createUserPart(dto))
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(
                                        new StringContains(
                                                "password must be between 6 and 20 characters")));
    }

    @Test
    void testRegisterReturnsUser() throws Exception {
        UserRegistrationDto dto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        dto.setId(null);
        String userJson = mapper.writeValueAsString(dto);

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/users/register")
                                .part(createUserPart(dto))
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ResponseDto.StatusCode.SUCCESS.getCode()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ResponseDto.StatusCode.SUCCESS.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.username").value(dto.getUsername()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.location.latitude")
                                .value(is(dto.getLocation().getY()), Double.class))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.location.longitude")
                                .value(is(dto.getLocation().getX()), Double.class));
    }

    @Test
    void testLoginReturnsHttpOk() throws Exception {
        UserRegistrationDto dto = registerPlainUser();
        MultiValueMap<String, String> params = TestDataUtil.objectToMultiValueMap(dto);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .params(params)
                )
                .andExpect(MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    void testLoginReturnsJwtToken() throws Exception {
        UserRegistrationDto dto = registerBusinessUserJoleneHornsey();
        MultiValueMap<String, String> params = TestDataUtil.objectToMultiValueMap(dto);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .params(params))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ResponseDto.StatusCode.SUCCESS.getCode()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ResponseDto.StatusCode.SUCCESS.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    void testListUsersReturnsUserList() throws Exception {
        UserRegistrationDto userDto1 = registerPlainUser();
        UserRegistrationDto userDto2 = registerBusinessUserJoleneHornsey();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ResponseDto.StatusCode.SUCCESS.getCode()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ResponseDto.StatusCode.SUCCESS.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[0].username")
                                .value(userDto1.getUsername()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[0].email")
                                .value(userDto1.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[1].username")
                                .value(userDto2.getUsername()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[1].location.latitude")
                                .value(is(userDto2.getLocation().getY()), Double.class))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data[1].location.longitude")
                                .value(is(userDto2.getLocation().getX()), Double.class));
    }

    @Test
    void testGetUserReturnsHttpOk() throws Exception {
        UserRegistrationDto dto = registerPlainUser();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + dto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetUserReturnsHttpNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetUserReturnsUser() throws Exception {
        UserRegistrationDto dto = registerBusinessUserJoleneHornsey();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + dto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ResponseDto.StatusCode.SUCCESS.getCode()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ResponseDto.StatusCode.SUCCESS.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.username").value(dto.getUsername()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.location.latitude")
                                .value(is(dto.getLocation().getY()), Double.class))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.data.location.longitude")
                                .value(is(dto.getLocation().getX()), Double.class));
    }
}
