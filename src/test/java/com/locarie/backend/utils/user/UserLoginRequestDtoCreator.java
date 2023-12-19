package com.locarie.backend.utils.user;

import com.locarie.backend.domain.dto.UserLoginRequestDto;
import com.locarie.backend.domain.entities.UserEntity;

public class UserLoginRequestDtoCreator {
    public static UserLoginRequestDto plainUserLoginRequestDto() {
        UserEntity userEntity = UserEntityCreator.plainUserEntity();
        return UserLoginRequestDto.builder()
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .build();
    }

    public static UserLoginRequestDto businessUserLoginRequestDtoJoleneHornsey() {
        UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
        return UserLoginRequestDto.builder()
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .build();
    }

    public static UserLoginRequestDto incorrectUserLoginRequestDto() {
        return UserLoginRequestDto.builder()
                .email("this-is-an-email-that-does-not-exist@.com")
                .password("this-is-a-password-that-does-not-exist")
                .build();
    }
}
