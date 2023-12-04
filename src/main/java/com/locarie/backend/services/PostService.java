package com.locarie.backend.services;

import com.locarie.backend.domain.dto.PostDto;
import java.util.List;
import java.util.Optional;

public interface PostService {
    PostDto create(PostDto dto);

    Optional<PostDto> get(Long id);

    List<PostDto> list();

    List<PostDto> listNearby(double latitude, double longitude, int distance);
}
