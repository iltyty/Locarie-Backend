package com.locarie.backend.datacreators.post;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostEntityCreator {
  public static PostEntity postEntityJoleneHornsey1() {
    UserEntity user = UserEntityCreator.businessUserEntityJoleneHornsey();
    return PostEntity.builder()
        .id(1L)
        .user(user)
        .content("On all week: Our Apple & Custard Danish! \uD83C\uDF4F\uD83D\uDC9A")
        .imageUrls(new ArrayList<>(List.of("https://i.ibb.co/82Qw0Tb/Jolene-Hornsey-Post1-1.jpg")))
        .build();
  }

  public static PostEntity postEntityJoleneHornsey2() {
    UserEntity user = UserEntityCreator.businessUserEntityJoleneHornsey();
    return PostEntity.builder()
        .id(2L)
        .user(user)
        .content("Today's delight")
        .imageUrls(
            new ArrayList<>(
                List.of(
                    "https://i.ibb.co/b7fyLTq/Jolene-Hornsey-Post-2-1.jpg",
                    "https://i.ibb.co/cYw3z26/Jolene-Hornsey-Post-2-2.jpg")))
        .build();
  }

  public static PostEntity postEntityShreeji1() {
    UserEntity user = UserEntityCreator.businessUserEntityShreeji();
    return PostEntity.builder()
        .id(3L)
        .user(user)
        .content("Shreeji Post 1 Content")
        .imageUrls(new ArrayList<>(List.of("image-url-1", "image-url-2")))
        .build();
  }

  public static PostEntity postEntityShreeji2() {
    UserEntity user = UserEntityCreator.businessUserEntityShreeji();
    return PostEntity.builder()
        .id(4L)
        .user(user)
        .content("Shreeji Post 2 Content")
        .imageUrls(new ArrayList<>(Arrays.asList("image-url-1", "image-url-2")))
        .build();
  }
}
