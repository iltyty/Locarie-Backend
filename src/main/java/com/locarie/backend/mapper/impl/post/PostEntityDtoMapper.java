package com.locarie.backend.mapper.impl.post;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.Mapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PostEntityDtoMapper implements Mapper<PostEntity, PostDto> {

  private final ModelMapper modelMapper = new ModelMapper();

  public PostEntityDtoMapper() {
    addEntityToDtoTypeMap();
    addDtoToEntityTypeMap();
  }

  private void addEntityToDtoTypeMap() {
    TypeMap<PostEntity, PostDto> typeMap = modelMapper.createTypeMap(PostEntity.class, PostDto.class);
    typeMap.addMappings(
        mapping ->
            mapping
                .using(COLLECTION_TO_SIZE_CONVERTER)
                .map(PostEntity::getFavoredBy, PostDto::setFavoredByCount));
  }

  private void addDtoToEntityTypeMap() {
    TypeMap<PostDto, PostEntity> typeMap = modelMapper.createTypeMap(PostDto.class, PostEntity.class);
    typeMap.addMappings(mapping -> mapping.skip(PostEntity::setFavoredBy));
  }

  @Override
  public PostDto mapTo(PostEntity postEntity) {
    return modelMapper.map(postEntity, PostDto.class);
  }

  @Override
  public PostEntity mapFrom(PostDto postDto) {
    return modelMapper.map(postDto, PostEntity.class);
  }
}
