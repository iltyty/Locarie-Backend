package com.locarie.backend.repositories;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.entities.Post;
import com.locarie.backend.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostRepositoryIntegrationTests {

    @Autowired
    private PostRepository underTests;

    @Test
    void testPostCreateAndQuery() {
        User user = TestDataUtil.newBusinessUserJoleneHornsey();
        Post post = TestDataUtil.newPostJoleneHornsey1(user);
        underTests.save(post);
        Optional<Post> result = underTests.findById(post.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(post);
    }
}
