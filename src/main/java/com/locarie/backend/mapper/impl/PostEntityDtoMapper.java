package com.locarie.backend.mapper.impl;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PostEntityDtoMapper implements Mapper<PostEntity, PostDto> {

  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public PostDto mapTo(PostEntity postEntity) {
    return modelMapper.map(postEntity, PostDto.class);
  }

  @Override
  public PostEntity mapFrom(PostDto postDto) {
    return modelMapper.map(postDto, PostEntity.class);
  }
}
