package com.locarie.backend.services.impl.post;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.repositories.PostRepository;
import com.locarie.backend.services.post.PostReadService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service("PostRead")
public class PostReadServiceImpl implements PostReadService {
    private final PostRepository repository;

    private final PostEntityDtoMapper mapper;

    public PostReadServiceImpl(PostRepository repository, PostEntityDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PostDto> get(Long id) {
        Optional<PostEntity> result = repository.findById(id);
        return result.map(mapper::mapTo);
    }

    @Override
    public List<PostDto> list() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public List<PostDto> listNearby(double latitude, double longitude, int distance) {
        return repository.findNearby(latitude, longitude, distance).stream()
                .map(mapper::mapTo)
                .toList();
    }
}
