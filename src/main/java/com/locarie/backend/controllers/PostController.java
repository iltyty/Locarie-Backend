package com.locarie.backend.controllers;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.services.PostService;
import com.locarie.backend.services.impl.PostServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    @GetMapping
    public List<PostDto> list() {
        return service.list();
    }

    @GetMapping("/nearby")
    public List<PostDto> listNearby(
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "distance") int distance
    ) {
        return service.listNearby(latitude, longitude, distance);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> get(@PathVariable Long id) {
        return service.get(id)
                .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
