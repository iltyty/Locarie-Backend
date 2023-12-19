package com.locarie.backend.utils.user;

import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.utils.LocationCreator;
import org.locationtech.jts.geom.Point;

public class UserEntityCreator {
    public static UserEntity plainUserEntity() {
        return UserEntity.builder()
                .id(1L)
                .type(UserEntity.Type.PLAIN)
                .username("Tony Stark")
                .password("12345678")
                .email("tonystark@avengers.com")
                .avatarUrl("https://picsum.photos/200/200")
                .build();
    }

    public static UserEntity businessUserEntityJoleneHornsey() {
        Point location = LocationCreator.location(51.560595, -0.116913);
        return UserEntity.builder()
                .id(2L)
                .type(UserEntity.Type.BUSINESS)
                .username("Jolene Hornsey")
                .password("88888888")
                .email("jolene-hornsey@bigjo.com")
                .avatarUrl("https://picsum.photos/200/200")
                .coverUrl("https://picsum.photos/800/450")
                .homepageUrl("https://www.bigjobakery.com/")
                .category("Restaurant")
                .introduction(
                        "WE ARE A BAKERY & RESTAURANT WHICH SUPPORT REGENERATIVE FOOD SYSTEMS &"
                                + " ETHICAL FARMING.")
                .phone("02039156760")
                .openHour(8)
                .openMinute(0)
                .closeHour(20)
                .closeMinute(0)
                .location(location)
                .locationName("318–326 HORNSEY ROAD N7 7HE")
                .build();
    }
}
