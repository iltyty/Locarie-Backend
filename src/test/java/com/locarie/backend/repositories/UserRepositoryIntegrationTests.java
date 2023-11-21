package com.locarie.backend.repositories;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserRepositoryIntegrationTests {

    @Autowired
    private UserRepository underTests;

    @Test
    void testPlainUserCreateAndQuery() {
        User user = TestDataUtil.newPlainUser();
        underTests.save(user);
        Optional<User> result = underTests.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void testBusinessUserCreateAndQuery() {
        User user = TestDataUtil.newBusinessUserJoleneHornsey();
        underTests.save(user);
        Optional<User> result = underTests.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }
}
