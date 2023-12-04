package com.locarie.backend.services.impl;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.repositories.PostRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class PostServiceImplTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostServiceImpl underTests;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PostEntityDtoMapper mapper;

    private PostDto createPostJoleneHornsey1() {
        UserRegistrationDto userDto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        UserDto savedUserDto = userService.register(userDto);
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey1(savedUserDto);
        return underTests.create(postDto);
    }

    private PostDto createPostJoleneHornsey2() {
        UserRegistrationDto userDto = TestDataUtil.newBusinessUserRegistrationDtoJoleneHornsey();
        UserDto savedUserDto = userService.register(userDto);
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey2(savedUserDto);
        return underTests.create(postDto);
    }

    @Test
    void testCreate() {
        PostDto dto = createPostJoleneHornsey1();
        Optional<PostEntity> post = postRepository.findById(dto.getId());
        assertThat(post.isPresent()).isTrue();
        assertThat(post.get().getId()).isEqualTo(dto.getId());
    }

    @Test
    void testList() {
        PostDto dto1 = createPostJoleneHornsey1();
        PostDto dto2 = createPostJoleneHornsey2();
        assertThat(underTests.list()).contains(dto1);
        assertThat(underTests.list()).contains(dto2);
        assertThat(underTests.list().size()).isEqualTo(2);
    }

    @Test
    void testListNearby() {
        PostDto dto1 = createPostJoleneHornsey1();
        PostDto dto2 = createPostJoleneHornsey2();
        Point location1 = dto1.getUser().getLocation();
        Point location2 = dto2.getUser().getLocation();

        List<PostDto> result1 = underTests.listNearby(location1.getY(), location1.getX(), 0);
        List<PostDto> result2 = underTests.listNearby(location2.getY(), location2.getX(), 0);
        List<PostDto> result3 = underTests.listNearby(location2.getY(), location2.getX(), 999);
        assertThat(result1.size()).isEqualTo(2);
        assertThat(result2.size()).isEqualTo(2);
        assertThat(result3.size()).isEqualTo(2);
    }

    @Test
    void testGet() {
        PostDto savedDto = createPostJoleneHornsey2();
        Optional<PostDto> result = underTests.get(savedDto.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(savedDto);
    }
}