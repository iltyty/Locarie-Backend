package com.locarie.backend.services.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginRequestDto;
import com.locarie.backend.domain.dto.UserLoginResponseDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class UserServiceImplTest {
    @Autowired private UserRepository repository;

    @Autowired private UserServiceImpl underTests;

    @Test
    void testRegistration() {
        UserRegistrationDto dto = TestDataUtil.newPlainUserRegistrationDto();
        UserDto user = underTests.register(dto, null);
        Optional<UserEntity> result = repository.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void testLogin() {
        UserRegistrationDto registrationDto = TestDataUtil.newPlainUserRegistrationDto();
        UserLoginRequestDto loginDto = TestDataUtil.newPlainUserLoginDto();
        underTests.register(registrationDto, null);
        UserLoginResponseDto result = underTests.login(loginDto);
        assertThat(result.getId()).isPositive();
        assertThat(result.getUsername()).isNotEmpty();
        assertThat(result.getJwtToken()).isNotEmpty();
    }

    @Test
    void testList() {
        UserRegistrationDto registrationDto1 = TestDataUtil.newPlainUserRegistrationDto();
        UserRegistrationDto registrationDto2 =
                TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        UserDto userDto1 = underTests.register(registrationDto1, null);
        UserDto userDto2 = underTests.register(registrationDto2, null);
        assertThat(underTests.listUsers()).contains(userDto1);
        assertThat(underTests.listUsers()).contains(userDto2);
    }

    @Test
    void testGet() {
        UserRegistrationDto registrationDto =
                TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        UserDto user = underTests.register(registrationDto, null);
        Optional<UserDto> result = underTests.getUser(user.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }
}
