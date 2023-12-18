package com.locarie.backend.services.post;

import com.locarie.backend.domain.dto.PostDto;

public interface PostCreateService {
    PostDto create(PostDto dto);
}
