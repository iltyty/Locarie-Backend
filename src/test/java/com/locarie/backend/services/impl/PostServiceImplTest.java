package com.locarie.backend.services.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.repositories.PostRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class PostServiceImplTest {
    private static final MockMultipartFile avatar =
            new MockMultipartFile("avatar", "avatar.jpg", "image/jpeg", new byte[1]);

    @Autowired private PostRepository postRepository;

    @Autowired private PostServiceImpl underTests;

    @Autowired private UserServiceImpl userService;

    @Autowired private PostEntityDtoMapper mapper;

    private UserDto registerBusinessUserJoleneHornsey() {
        UserRegistrationDto userDto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        return userService.register(userDto, avatar);
    }

    private PostDto createPostJoleneHornsey1(final UserDto userDto) {
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey1(userDto);
        return underTests.create(postDto);
    }

    private PostDto createPostJoleneHornsey2(final UserDto userDto) {
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey2(userDto);
        return underTests.create(postDto);
    }

    @Test
    void testCreate() {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        PostDto postDto = createPostJoleneHornsey1(userDto);
        Optional<PostEntity> post = postRepository.findById(postDto.getId());
        assertThat(post.isPresent()).isTrue();
        assertThat(post.get().getId()).isEqualTo(postDto.getId());
    }

    @Test
    void testList() {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        PostDto postDto1 = createPostJoleneHornsey1(userDto);
        PostDto postDto2 = createPostJoleneHornsey2(userDto);
        assertThat(underTests.list()).contains(postDto1);
        assertThat(underTests.list()).contains(postDto2);
        assertThat(underTests.list().size()).isEqualTo(2);
    }

    @Test
    void testListNearby() {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        PostDto postDto1 = createPostJoleneHornsey1(userDto);
        PostDto postDto2 = createPostJoleneHornsey2(userDto);
        Point location1 = postDto1.getUser().getLocation();
        Point location2 = postDto2.getUser().getLocation();

        List<PostDto> result1 = underTests.listNearby(location1.getY(), location1.getX(), 0);
        List<PostDto> result2 = underTests.listNearby(location2.getY(), location2.getX(), 0);
        List<PostDto> result3 = underTests.listNearby(location2.getY(), location2.getX(), 999);
        assertThat(result1.size()).isEqualTo(2);
        assertThat(result2.size()).isEqualTo(2);
        assertThat(result3.size()).isEqualTo(2);
    }

    @Test
    void testGet() {
        UserDto userDto = registerBusinessUserJoleneHornsey();
        PostDto postDto = createPostJoleneHornsey2(userDto);
        Optional<PostDto> result = underTests.get(postDto.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(postDto);
    }
}
