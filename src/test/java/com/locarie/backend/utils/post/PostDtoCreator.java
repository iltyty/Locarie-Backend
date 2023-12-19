package com.locarie.backend.utils.post;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.utils.user.UserEntityCreator;

public class PostDtoCreator {
    private static final Mapper<PostEntity, PostDto> mapper = new PostEntityDtoMapper();

    public static PostDto postDtoJoleneHornsey1() {
        UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
        return mapper.mapTo(PostEntityCreator.newPostEntityJoleneHornsey1(userEntity));
    }

    public static PostDto postDtoJoleneHornsey2() {
        UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
        return mapper.mapTo(PostEntityCreator.newPostEntityJoleneHornsey2(userEntity));
    }
}
