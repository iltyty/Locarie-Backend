package com.locarie.backend.repositories;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
public class UserEntityRepositoryTests {

    @Autowired
    private UserRepository underTests;

    @Test
    void testPlainUserCreateAndQuery() {
        UserEntity user = TestDataUtil.newPlainUser();
        underTests.save(user);
        Optional<UserEntity> result = underTests.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void testBusinessUserCreateAndQuery() {
        UserEntity user = TestDataUtil.newBusinessUserJoleneHornsey();
        underTests.save(user);
        Optional<UserEntity> result = underTests.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void testGetUserByEmail() {
        UserEntity user = TestDataUtil.newBusinessUserJoleneHornsey();
        underTests.save(user);
        Optional<UserEntity> result = underTests.emailEquals(user.getEmail());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
        result = underTests.emailEquals("email@notExists.com");
        assertThat(result.isPresent()).isFalse();
    }
}
