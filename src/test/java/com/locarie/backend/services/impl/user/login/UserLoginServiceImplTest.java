package com.locarie.backend.services.impl.user.login;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.domain.dto.UserLoginRequestDto;
import com.locarie.backend.domain.dto.UserLoginResponseDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.services.impl.user.UserServiceImpl;
import com.locarie.backend.utils.user.UserLoginRequestDtoCreator;
import com.locarie.backend.utils.user.UserRegistrationDtoCreator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserLoginServiceImplTest {
    @Autowired private UserServiceImpl underTests;

    @Test
    void loginWithCorrectCredentialShouldSucceed() {
        UserLoginRequestDto loginDto = givenCorrectLoginCredentialAfterRegistered();
        UserLoginResponseDto result = whenLogin(loginDto);
        thenLoginResultShouldBeSuccess(result);
    }

    @Test
    void loginWithIncorrectCredentialShouldFail() {
        UserLoginRequestDto loginDto = givenIncorrectLoginCredential();
        UserLoginResponseDto result = whenLogin(loginDto);
        thenLoginResultShouldBeFailure(result);
    }

    private UserLoginRequestDto givenCorrectLoginCredentialAfterRegistered() {
        UserRegistrationDto userRegistrationDto =
                UserRegistrationDtoCreator.plainUserRegistrationDto();
        underTests.register(userRegistrationDto, null);
        return UserLoginRequestDtoCreator.plainUserLoginRequestDto();
    }

    private UserLoginRequestDto givenIncorrectLoginCredential() {
        return UserLoginRequestDtoCreator.incorrectUserLoginRequestDto();
    }

    private UserLoginResponseDto whenLogin(UserLoginRequestDto loginDto) {
        return underTests.login(loginDto);
    }

    private void thenLoginResultShouldBeSuccess(UserLoginResponseDto result) {
        assertThat(result).isNotNull();
        assertThat(result.getId()).isPositive();
        assertThat(result.getUsername()).isNotEmpty();
        assertThat(result.getJwtToken()).isNotEmpty();
    }

    private void thenLoginResultShouldBeFailure(UserLoginResponseDto result) {
        assertThat(result).isNull();
    }
}
