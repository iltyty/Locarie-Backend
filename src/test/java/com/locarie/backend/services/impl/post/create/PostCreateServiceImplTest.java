package com.locarie.backend.services.impl.post.create;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.PostRepository;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.impl.post.PostCreateServiceImpl;
import com.locarie.backend.utils.post.PostDtoCreator;
import com.locarie.backend.utils.user.UserEntityCreator;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class PostCreateServiceImplTest {
  private UserDto userDto;
  private static final MultipartFile avatar =
      new MockMultipartFile("avatar", "avatar.jpg", "image/jpeg", new byte[1]);

  @Autowired Mapper<UserEntity, UserDto> userMapper;
  @Autowired private PostRepository postRepository;
  @Autowired private PostCreateServiceImpl underTests;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  public void createBusinessUserJoleneHornsey() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    UserEntity savedUserEntity = userRepository.save(userEntity);
    userDto = userMapper.mapTo(savedUserEntity);
  }

  @Test
  void testCreate() {
    List<PostDto> posts = givenPosts("post1");
    Optional<PostEntity> postEntity = whenFindPostById(posts.getFirst().getId());
    assertThat(postEntity.isPresent()).isTrue();
    thenResultIdShouldEqualsToPostId(postEntity.get().getId(), posts.getFirst().getId());
  }

  private List<PostDto> givenPosts(String... postNames) {
    List<PostDto> posts = new ArrayList<>();
    for (String postName : postNames) {
      PostDto postDto;
      switch (postName) {
        case "post1":
          postDto = PostDtoCreator.postDtoJoleneHornsey1();
          break;
        case "post2":
          postDto = PostDtoCreator.postDtoJoleneHornsey2();
          break;
        default:
          continue;
      }
      postDto.setUser(userDto);
      PostDto createdPostDto = underTests.create(postDto, new MultipartFile[0]);
      posts.add(createdPostDto);
    }
    return posts;
  }

  private Optional<PostEntity> whenFindPostById(Long id) {
    return postRepository.findById(id);
  }

  private void thenResultIdShouldEqualsToPostId(Long resultId, Long postId) {
    assertThat(resultId).isEqualTo(postId);
  }
}
