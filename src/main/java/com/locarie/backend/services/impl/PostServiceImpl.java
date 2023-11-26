package com.locarie.backend.services.impl;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.repositories.PostRepository;
import com.locarie.backend.services.PostService;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    private final PostEntityDtoMapper mapper;

    public PostServiceImpl(PostRepository repository, PostEntityDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PostDto create(PostDto dto) {
        PostEntity post = mapper.mapFrom(dto);
        PostEntity savedPost = repository.save(post);
        return mapper.mapTo(savedPost);
    }
}
