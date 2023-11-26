package com.locarie.backend.services.impl;

import com.locarie.backend.TestDataUtil;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.repositories.PostRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class PostServiceImplTest {

    private final PostRepository repository;

    private final PostServiceImpl underTests;

    private final PostEntityDtoMapper mapper;

    @Autowired
    PostServiceImplTest(PostRepository repository, PostServiceImpl underTests, PostEntityDtoMapper mapper) {
        this.repository = repository;
        this.underTests = underTests;
        this.mapper = mapper;
    }

    @Test
    void testCreate() {
        // data preparation
        UserDto userDto = TestDataUtil.newBusinessUserDtoJoleneHornsey();
        userDto.setId(null);
        PostDto postDto = TestDataUtil.newPostDtoJoleneHornsey1(userDto);
        postDto.setId(null);

        PostDto savedDto = underTests.create(postDto);
        Optional<PostEntity> post = repository.findById(savedDto.getId());
        assertThat(post.isPresent()).isTrue();
        assertThat(post.get()).isEqualTo(mapper.mapFrom(savedDto));
    }
}