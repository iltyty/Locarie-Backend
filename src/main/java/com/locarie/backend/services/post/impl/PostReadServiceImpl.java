package com.locarie.backend.services.post.impl;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.impl.post.PostEntityDtoMapper;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.services.post.PostReadService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import lombok.extern.log4j.Log4j2;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

@Log4j2
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
        .sorted((a, b) -> b.getTime().compareTo(a.getTime()))
        .toList();
  }

  @Override
  public List<PostDto> listNearby(double latitude, double longitude, double distance) {
    return repository.findNearby(latitude, longitude, distance).stream()
        .map(mapper::mapTo)
        .sorted((a, b) -> b.getTime().compareTo(a.getTime()))
        .toList();
  }

  @Override
  public List<PostDto> listNearbyAll(double latitude, double longitude) {
    return repository.findNearbyAll(latitude, longitude).stream()
        .map(mapper::mapTo)
        .toList();
  }

  @Override
  public List<PostDto> listUserPosts(Long id) {
    return repository.findByUserId(id).stream()
        .map(mapper::mapTo)
        .sorted((a, b) -> b.getTime().compareTo(a.getTime()))
        .toList();
  }

  @Override
  public List<PostDto> listWithin(
      double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
    Geometry bound = pointsToBound(minLatitude, maxLatitude, minLongitude, maxLongitude);
    return repository.findWithin(bound).stream()
        .map(mapper::mapTo)
        .sorted((a, b) -> b.getTime().compareTo(a.getTime()))
        .toList();
  }

  private Geometry pointsToBound(
      double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
    GeometryFactory factory = new GeometryFactory();
    Coordinate northwest = new Coordinate(minLongitude, maxLatitude);
    Coordinate southeast = new Coordinate(maxLongitude, minLatitude);
    Envelope envelope = new Envelope(northwest, southeast);
    return factory.toGeometry(envelope);
  }
}
