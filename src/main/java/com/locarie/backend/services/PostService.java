package com.locarie.backend.services;

import com.locarie.backend.domain.dto.PostDto;

import java.util.Optional;

public interface PostService {
    PostDto create(PostDto dto);

    Optional<PostDto> getPost(Long id);
}
