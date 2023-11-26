package com.locarie.backend.repositories;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostRepositoryTests {

    @Autowired
    private PostRepository underTests;

    @Test
    void testPostCreateAndQuery() {
        UserEntity user = TestDataUtil.newBusinessUserEntityJoleneHornsey();
        user.setId(null);
        PostEntity post = TestDataUtil.newPostJoleneHornsey1(user);
        post.setId(null);

        underTests.save(post);
        Optional<PostEntity> result = underTests.findById(post.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTitle()).isEqualTo(post.getTitle());
        assertThat(result.get().getContent()).isEqualTo(post.getContent());
    }
}
