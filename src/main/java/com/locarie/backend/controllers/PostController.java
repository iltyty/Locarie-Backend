package com.locarie.backend.controllers;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.services.PostService;
import com.locarie.backend.services.impl.PostServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService service;

    public PostController(PostServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@RequestBody PostDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }
}
