package com.locarie.backend.services.impl.post.create;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.user.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.image.MockImageCreator;
import com.locarie.backend.datacreators.post.PostDtoCreator;
import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.services.post.impl.PostCreateServiceImpl;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class PostCreateServiceImplTest {
  @Autowired private PostCreateServiceImpl underTests;
  @Autowired private PostRepository postRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private UserTestsDataCreator userTestsDataCreator;

  @Test
  void testCreate() {
    PostDto postDto = givenPostDto();
    MultipartFile[] postImages = givenPostImages();
    PostDto result = whenCreatePost(postDto, postImages);
    Optional<PostEntity> postEntity = whenFindPostById(result.getId());
    assertThat(postEntity.isPresent()).isTrue();
    thenResultIdShouldEqualsToPostId(result.getId(), postEntity.get().getId());
    thenUserLatestUpdateShouldBeNow(postDto.getUser().getId());
  }

  private PostDto givenPostDto() {
    long userId = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    PostDto postDto = PostDtoCreator.postDtoShreeji1();
    postDto.setId(null);
    postDto.getUser().setId(userId);
    return postDto;
  }

  private MultipartFile[] givenPostImages() {
    return new MultipartFile[] {MockImageCreator.pngImage(), MockImageCreator.jpgImage()};
  }

  private PostDto whenCreatePost(PostDto postDto, MultipartFile[] postImages) {
    return underTests.create(postDto, postImages);
  }

  private Optional<PostEntity> whenFindPostById(Long id) {
    return postRepository.findById(id);
  }

  private void thenResultIdShouldEqualsToPostId(Long resultId, Long postId) {
    assertThat(resultId).isEqualTo(postId);
  }

  private void thenUserLatestUpdateShouldBeNow(Long userId) {
    Optional<UserEntity> userOptional = userRepository.findById(userId);
    assertThat(userOptional.isPresent()).isTrue();
    UserEntity user = userOptional.get();
    Duration duration = Duration.between(user.getLastUpdate(), Instant.now());
    assertThat(duration.getSeconds()).isLessThan(3);
  }
}
