package com.locarie.backend.services.impl.post.delete;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.services.post.PostDeleteService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostDeleteServiceImplTest {
  @Autowired private PostDeleteService underTests;
  @Autowired private PostRepository repository;
  @Autowired private PostTestsDataCreator postTestsDataCreator;

  @Test
  void testDeletePostShouldSucceed() {
    PostDto post = postTestsDataCreator.givenPostDtoShreeji1AfterCreated();
    underTests.delete(post.getId());
    assertThat(repository.findAll()).isEmpty();
  }
}
