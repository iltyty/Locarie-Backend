package com.locarie.backend.services.impl.user.delete;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.impl.UserDeleteServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserDeleteServiceImplTest {
  @Autowired private UserDeleteServiceImpl underTests;
  @Autowired private UserRepository repository;
  @Autowired private PostRepository postRepository;
  @Autowired private UserTestsDataCreator userDataCreator;
  @Autowired private PostTestsDataCreator postTestsDataCreator;

  @Test
  void testDeleteUserAfterCreatedShouldSucceed() {
    Long userId = userDataCreator.givenPlainUserAfterCreated().getId();
    underTests.delete(userId);
    assertThat(repository.findAll()).isEmpty();
  }

  @Test
  void testDeleteUserWithPostsShouldSucceed() {
    Long userId = userDataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    postTestsDataCreator.givenPostDtoJoleneHornsey1AfterCreated();
    underTests.delete(userId);
    assertThat(repository.findAll()).isEmpty();
  }
}
