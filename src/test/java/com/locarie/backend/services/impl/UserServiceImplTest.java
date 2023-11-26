package com.locarie.backend.services.impl;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserServiceImpl underTests;

    @Test
    void testUserRegistration() {
        UserRegistrationDto dto = TestDataUtil.newPlainUserRegistrationDto();
        UserDto user = underTests.register(dto);
        Optional<UserEntity> result = repository.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
    }

}