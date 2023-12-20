package com.locarie.backend.services.impl.post.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.services.impl.post.PostReadServiceImpl;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class PostReadServiceImplTest {
  @Autowired private PostReadServiceImpl underTests;
  @Autowired private PostTestsDataCreator postTestsDataCreator;

  @Test
  void testGet() {
    List<PostDto> postDtos = postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    Optional<PostDto> result = whenGetPost(postDtos.getFirst().getId());
    assertThat(result.isPresent()).isTrue();
    thenGetResultShouldEqualsToPostDto(result.get(), postDtos.getFirst());
  }

  private Optional<PostDto> whenGetPost(Long id) {
    return underTests.get(id);
  }

  private void thenGetResultShouldEqualsToPostDto(PostDto result, PostDto postDto) {
    assertThat(result).isEqualTo(postDto);
  }
}
