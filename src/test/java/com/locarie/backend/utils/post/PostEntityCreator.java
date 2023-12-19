package com.locarie.backend.utils.post;

import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import java.util.Arrays;
import java.util.List;

public class PostEntityCreator {
  public static PostEntity newPostEntityJoleneHornsey1(final UserEntity user) {
    return PostEntity.builder()
        .id(1L)
        .user(user)
        .title("On as weekend spesh: Pistachio frangi & orange curd" + " \uD83C\uDF4A\uD83E\uDDE1")
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
