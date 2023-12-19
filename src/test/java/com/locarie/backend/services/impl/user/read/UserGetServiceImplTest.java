package com.locarie.backend.services.impl.user.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.services.impl.user.UserServiceImpl;
import com.locarie.backend.utils.user.UserRegistrationDtoCreator;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserGetServiceImplTest {
    @Autowired private UserServiceImpl underTests;

    @Test
    void testGetShouldReturnUser() {
        UserRegistrationDto userRegistrationDto = givenUser();
        Optional<UserDto> result = whenGetUserAfterRegistered(userRegistrationDto);
        assertThat(result.isPresent()).isTrue();
        thenGetResultShouldEqualToRegistrationDto(result.get(), userRegistrationDto);
    }

    private UserRegistrationDto givenUser() {
        UserRegistrationDto dto = UserRegistrationDtoCreator.plainUserRegistrationDto();
        dto.setId(null);
        return dto;
    }

    private Optional<UserDto> whenGetUserAfterRegistered(UserRegistrationDto userRegistrationDto) {
        UserDto savedUserDto = underTests.register(userRegistrationDto, null);
        return underTests.get(savedUserDto.getId());
    }

    private void thenGetResultShouldEqualToRegistrationDto(
            UserDto result, UserRegistrationDto userRegistrationDto) {
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(userRegistrationDto);
    }
}
