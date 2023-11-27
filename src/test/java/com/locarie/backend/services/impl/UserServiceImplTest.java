package com.locarie.backend.services.impl;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserServiceImpl underTests;

    @Test
    void testRegistration() {
        UserRegistrationDto dto = TestDataUtil.newPlainUserRegistrationDto();
        UserDto user = underTests.register(dto);
        Optional<UserEntity> result = repository.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void testLogin() {
        UserRegistrationDto registrationDto = TestDataUtil.newPlainUserRegistrationDto();
        UserLoginDto loginDto = TestDataUtil.newPlainUserLoginDto();
        underTests.register(registrationDto);
        String token = underTests.login(loginDto);
        assertThat(token).isNotEmpty();
        System.out.println(token);
    }

    @Test
    void testList() {
        UserRegistrationDto registrationDto1 =
                TestDataUtil.newPlainUserRegistrationDto();
        UserRegistrationDto registrationDto2 =
                TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        UserDto userDto1 = underTests.register(registrationDto1);
        UserDto userDto2 = underTests.register(registrationDto2);
        assertThat(underTests.listUsers()).contains(userDto1);
        assertThat(underTests.listUsers()).contains(userDto2);
    }

    @Test
    void testGet() {
        UserRegistrationDto registrationDto =
                TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        UserDto user = underTests.register(registrationDto);
        Optional<UserDto> result = underTests.getUser(user.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }
}