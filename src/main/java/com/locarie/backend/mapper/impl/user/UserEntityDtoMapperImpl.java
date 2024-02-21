package com.locarie.backend.mapper.impl.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import java.util.Collection;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class UserEntityDtoMapperImpl implements Mapper<UserEntity, UserDto> {
  private final ModelMapper modelMapper = new ModelMapper();

  public UserEntityDtoMapperImpl() {
    addEntityToDtoTypeMap();
    addDtoToEntityTypeMap();
  }

  private void addEntityToDtoTypeMap() {
    TypeMap<UserEntity, UserDto> typeMap =
        modelMapper.createTypeMap(UserEntity.class, UserDto.class);
    Converter<Collection<?>, Integer> collectionToSize =
        c -> {
          if (c.getSource() == null) {
            return 0;
          }
          return c.getSource().size();
        };
    typeMap.addMappings(
        mapping ->
            mapping
                .using(collectionToSize)
                .map(UserEntity::getFavoredBy, UserDto::setFavoredByCount));
    typeMap.addMappings(
        mapping ->
            mapping
                .using(collectionToSize)
                .map(UserEntity::getFavoritePosts, UserDto::setFavoritePostsCount));
    typeMap.addMappings(
        mapping ->
            mapping
                .using(collectionToSize)
                .map(UserEntity::getFavoriteBusinesses, UserDto::setFavoriteBusinessesCount));
  }

  private void addDtoToEntityTypeMap() {
    TypeMap<UserDto, UserEntity> typeMap =
        modelMapper.createTypeMap(UserDto.class, UserEntity.class);
    typeMap.addMappings(mapping -> mapping.skip(UserEntity::setFavoredBy));
    typeMap.addMappings(mapping -> mapping.skip(UserEntity::setFavoritePosts));
    typeMap.addMappings(mapping -> mapping.skip(UserEntity::setFavoriteBusinesses));
  }

  @Override
  public UserDto mapTo(UserEntity user) {
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public UserEntity mapFrom(UserDto userDto) {
    return modelMapper.map(userDto, UserEntity.class);
  }
}
