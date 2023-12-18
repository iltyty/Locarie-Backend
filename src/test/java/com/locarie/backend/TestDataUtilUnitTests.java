package com.locarie.backend;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.utils.UserTestsDataCreator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestDataUtilUnitTests {

    @Test
    void testNewUserRegistrationDtoGeneratedCorrectly() {
        UserEntity user = UserTestsDataCreator.newPlainUserEntity();
        UserRegistrationDto dto = TestDataUtil.newPlainUserRegistrationDto();
        assertThat(dto.getPassword()).isEqualTo(user.getPassword());

        user = UserTestsDataCreator.newBusinessUserEntityJoleneHornsey();
        dto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        assertThat(dto.getPassword()).isEqualTo(user.getPassword());
    }
}
