package com.locarie.backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.utils.user.UserEntityCreator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
public class UserRepositoryTest {

    @Autowired private UserRepository underTests;

    @Test
    void testPlainUserCreateAndQuery() {
        UserEntity user = UserEntityCreator.plainUserEntity();
        underTests.save(user);
        Optional<UserEntity> result = underTests.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void testRepeatedPlainUserCreation() {
        UserEntity user = UserEntityCreator.plainUserEntity();
        underTests.save(user);
        underTests.save(user);
        Iterable<UserEntity> result = underTests.findAll();
        List<UserEntity> users = StreamSupport.stream(result.spliterator(), false).toList();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void testBusinessUserCreateAndQuery() {
        UserEntity user = UserEntityCreator.businessUserEntityJoleneHornsey();
        underTests.save(user);
        Optional<UserEntity> result = underTests.findById(user.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void testGetUserByEmail() {
        UserEntity user = UserEntityCreator.businessUserEntityJoleneHornsey();
        underTests.save(user);
        Optional<UserEntity> result = underTests.emailEquals(user.getEmail());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
        result = underTests.emailEquals("email@notExists.com");
        assertThat(result.isPresent()).isFalse();
    }
}
