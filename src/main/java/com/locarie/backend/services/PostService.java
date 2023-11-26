package com.locarie.backend.services;

import com.locarie.backend.domain.dto.PostDto;

public interface PostService {
    PostDto create(PostDto dto);
}
