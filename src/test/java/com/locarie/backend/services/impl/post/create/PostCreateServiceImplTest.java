package com.locarie.backend.services.impl.post.create;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.repositories.PostRepository;
import com.locarie.backend.services.impl.post.PostCreateServiceImpl;
import com.locarie.backend.services.impl.user.UserServiceImpl;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class PostCreateServiceImplTest {
    private UserDto userDto;

    @Autowired private PostRepository postRepository;
    @Autowired private PostCreateServiceImpl underTests;
    @Autowired private UserServiceImpl userService;

    @BeforeEach
    public void registerBusinessUserJoleneHornsey() {
        UserRegistrationDto registrationDto =
                TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        userDto = userService.register(registrationDto, TestDataUtil.getAvatar());
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
                    postDto = TestDataUtil.newPostDtoJoleneHornsey1(userDto);
                    break;
                case "post2":
                    postDto = TestDataUtil.newPostDtoJoleneHornsey2(userDto);
                    break;
                default:
                    continue;
            }
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
