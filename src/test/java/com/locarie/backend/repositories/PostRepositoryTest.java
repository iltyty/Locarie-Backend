package com.locarie.backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.post.PostEntityCreator;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class PostRepositoryTest {

  @Autowired private UserRepository userRepository;

  @Autowired private PostRepository underTests;

  @Test
  void testPostCreateAndQuery() {
    UserEntity user = UserEntityCreator.businessUserEntityJoleneHornsey();
    UserEntity savedUser = userRepository.save(user);
    PostEntity post = PostEntityCreator.newPostEntityJoleneHornsey1(savedUser);
    underTests.save(post);

    Optional<PostEntity> result = underTests.findById(post.getId());
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getTitle()).isEqualTo(post.getTitle());
    assertThat(result.get().getContent()).isEqualTo(post.getContent());
  }

  @Test
  void testFindNearbyPosts() {
    UserEntity user1 = UserEntityCreator.businessUserEntityJoleneHornsey();
    UserEntity savedUser = userRepository.save(user1);
    PostEntity post = PostEntityCreator.newPostEntityJoleneHornsey1(savedUser);
    underTests.save(post);

    Optional<PostEntity> result1 = underTests.findById(post.getId());
    assertThat(result1.isPresent()).isTrue();
    assertThat(result1.get().getTitle()).isEqualTo(post.getTitle());
    assertThat(result1.get().getContent()).isEqualTo(post.getContent());

    List<PostEntity> result2 = underTests.findNearby(50, 0.1, 0);
    assertThat(result2.isEmpty()).isTrue();

    UserEntity user2 = UserEntityCreator.businessUserEntityJoleneHornsey();
    result2 = underTests.findNearby(user2.getLocation().getY(), user2.getLocation().getX(), 0);
    assertThat(result2.size()).isEqualTo(1);
  }
}
