package com.locarie.backend.services.impl.post;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.repositories.PostRepository;
import com.locarie.backend.services.post.PostCreateService;
import com.locarie.backend.storage.StorageService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("PostCreate")
public class PostCreateServiceImpl implements PostCreateService {
    private final PostRepository repository;

    private final PostEntityDtoMapper mapper;

    private final StorageService storageService;

    public PostCreateServiceImpl(
            PostRepository repository, PostEntityDtoMapper mapper, StorageService storageService) {
        this.repository = repository;
        this.mapper = mapper;
        this.storageService = storageService;
    }

    @Override
    public PostDto create(PostDto dto) {
        PostEntity post = mapper.mapFrom(dto);
        PostEntity savedPost = repository.save(post);
        return mapper.mapTo(savedPost);
    }

    private List<String> savePostImages(MultipartFile[] images) {
        return new ArrayList<>();
    }
}
