package com.locarie.backend;

import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TestDataUtilUnitTests {

    @Test
    void testNewUserRegistrationDtoGeneratedCorrectly() {
        UserEntity user = TestDataUtil.newPlainUserEntity();
        UserRegistrationDto dto = TestDataUtil.newPlainUserRegistrationDto();
        assertThat(dto.getPassword()).isEqualTo(user.getPassword());

        user = TestDataUtil.newBusinessUserEntityJoleneHornsey();
        dto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        assertThat(dto.getPassword()).isEqualTo(user.getPassword());
    }
}
