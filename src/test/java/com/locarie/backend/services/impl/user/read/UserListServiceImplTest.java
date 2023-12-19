package com.locarie.backend.services.impl.user.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.services.impl.user.UserServiceImpl;
import com.locarie.backend.utils.user.UserRegistrationDtoCreator;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserListServiceImplTest {
    @Autowired private UserServiceImpl underTests;

    @Test
    void testListAfterRegisteredShouldReturnAllUsers() {
        List<UserRegistrationDto> userRegistrationDtos = givenUserRegistrationDtos();
        List<UserDto> result = whenListUsersAfterRegistered(userRegistrationDtos);
        thenListResultShouldContainsAllDtos(result, userRegistrationDtos);
    }

    private List<UserRegistrationDto> givenUserRegistrationDtos() {
        return Arrays.asList(
                UserRegistrationDtoCreator.plainUserRegistrationDto(),
                UserRegistrationDtoCreator.businessUserRegistrationDtoJoleneHornsey());
    }

    private List<UserDto> whenListUsersAfterRegistered(
            List<UserRegistrationDto> userRegistrationDtos) {
        userRegistrationDtos.forEach(
                userRegistrationDto -> underTests.register(userRegistrationDto, null));
        return underTests.list();
    }

    private void thenListResultShouldContainsAllDtos(
            List<UserDto> result, List<UserRegistrationDto> dtos) {
        assertThat(result.size()).isEqualTo(dtos.size());
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i))
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(dtos.get(i));
        }
    }
}
