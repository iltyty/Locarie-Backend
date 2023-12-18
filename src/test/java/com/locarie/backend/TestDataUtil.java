package com.locarie.backend;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginRequestDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.mapper.impl.UserEntityDtoMapperImpl;
import com.locarie.backend.mapper.impl.UserEntityRegistrationDtoMapperImpl;
import com.locarie.backend.utils.PostTestsDataCreator;
import com.locarie.backend.utils.UserTestsDataCreator;
import java.lang.reflect.Field;
import java.util.Collections;
import lombok.Getter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class TestDataUtil {
    @Getter
    private static final MockMultipartFile avatar =
            new MockMultipartFile("avatar", "avatar.jpg", "image/jpeg", new byte[1]);

    private static final GeometryFactory geometryFactory = new GeometryFactory();
    private static final Mapper<UserEntity, UserDto> userEntityDtoMapper =
            new UserEntityDtoMapperImpl();
    private static final Mapper<UserEntity, UserRegistrationDto> registrationDtoMapper =
            new UserEntityRegistrationDtoMapperImpl();
    private static final Mapper<PostEntity, PostDto> postEntityDtoMapper =
            new PostEntityDtoMapper();

    public static Point newLocation(double latitude, double longitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    public static UserDto newPlainUserDto() {
        return userEntityDtoMapper.mapTo(UserTestsDataCreator.newPlainUserEntity());
    }

    public static UserRegistrationDto newPlainUserRegistrationDto() {
        return registrationDtoMapper.mapTo(UserTestsDataCreator.newPlainUserEntity());
    }

    public static UserLoginRequestDto newPlainUserLoginDto() {
        UserEntity user = UserTestsDataCreator.newPlainUserEntity();
        return UserLoginRequestDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public static UserDto newBusinessUserDtoJoleneHornsey() {
        return userEntityDtoMapper.mapTo(UserTestsDataCreator.newBusinessUserEntityJoleneHornsey());
    }

    public static UserRegistrationDto newBusinessUserRegistrationDtoJoleneHornsey() {
        return registrationDtoMapper.mapTo(
                UserTestsDataCreator.newBusinessUserEntityJoleneHornsey());
    }

    public static UserLoginRequestDto newBusinessUserLoginDtoJoleneHornsey() {
        UserEntity user = UserTestsDataCreator.newBusinessUserEntityJoleneHornsey();
        return UserLoginRequestDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public static PostDto newPostDtoJoleneHornsey1(final UserDto dto) {
        UserEntity user = dto != null ? userEntityDtoMapper.mapFrom(dto) : null;
        return postEntityDtoMapper.mapTo(PostTestsDataCreator.newPostEntityJoleneHornsey1(user));
    }

    public static PostDto newPostDtoJoleneHornsey2(final UserDto dto) {
        UserEntity user = dto != null ? userEntityDtoMapper.mapFrom(dto) : null;
        return postEntityDtoMapper.mapTo(PostTestsDataCreator.newPostEntityJoleneHornsey2(user));
    }

    public static MultiValueMap<String, String> objectToMultiValueMap(Object object)
            throws IllegalAccessException {
        if (object == null) {
            return null;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        Class<?> currentClass = object.getClass();

        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null) {
                    map.put(field.getName(), Collections.singletonList(value.toString()));
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return map;
    }
}
