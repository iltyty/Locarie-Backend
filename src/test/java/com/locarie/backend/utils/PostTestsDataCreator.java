package com.locarie.backend.utils;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.repositories.PostRepository;
import com.locarie.backend.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostTestsDataCreator {
    @Autowired private PostRepository postRepository;
    @Autowired private PostEntityDtoMapper postEntityDtoMapper;
    @Autowired private UserRepository userRepository;

    private UserEntity user;

    public List<PostDto> givenPosts(String... postNames) {
        registerBusinessUserJoleneHornsey();
        List<PostDto> posts = new ArrayList<>();
        for (String postName : postNames) {
            PostEntity postEntity;
            switch (postName) {
                case "post1":
                    postEntity = PostTestsDataCreator.newPostEntityJoleneHornsey1(user);
                    break;
                case "post2":
                    postEntity = PostTestsDataCreator.newPostEntityJoleneHornsey2(user);
                    break;
                default:
                    continue;
            }
            PostEntity savedPostEntity = postRepository.save(postEntity);
            PostDto savedPostDto = postEntityDtoMapper.mapTo(savedPostEntity);
            posts.add(savedPostDto);
        }
        return posts;
    }

    private void registerBusinessUserJoleneHornsey() {
        UserEntity userEntity = UserTestsDataCreator.newBusinessUserEntityJoleneHornsey();
        userEntity.setId(null);
        user = userRepository.save(userEntity);
    }

    public static PostEntity newPostEntityJoleneHornsey1(final UserEntity user) {
        return PostEntity.builder()
                .id(1L)
                .user(user)
                .title(
                        "On as weekend spesh: Pistachio frangi & orange curd"
                                + " \uD83C\uDF4A\uD83E\uDDE1")
                .content("On all week: Our Apple & Custard Danish! \uD83C\uDF4F\uD83D\uDC9A")
                .imageUrls(List.of("https://i.ibb.co/82Qw0Tb/Jolene-Hornsey-Post1-1.jpg"))
                .build();
    }

    public static PostEntity newPostEntityJoleneHornsey2(final UserEntity user) {
        return PostEntity.builder()
                .id(2L)
                .user(user)
                .title("Jaffa Cake & Sunday lunch \uD83E\uDDE1")
                .content("Today's delight")
                .imageUrls(
                        Arrays.asList(
                                "https://i.ibb.co/b7fyLTq/Jolene-Hornsey-Post-2-1.jpg",
                                "https://i.ibb.co/cYw3z26/Jolene-Hornsey-Post-2-2.jpg"))
                .build();
    }
}
