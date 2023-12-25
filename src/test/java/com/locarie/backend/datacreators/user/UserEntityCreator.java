package com.locarie.backend.datacreators.user;

import com.locarie.backend.datacreators.LocationCreator;
import com.locarie.backend.domain.entities.UserEntity;
import java.util.List;
import org.locationtech.jts.geom.Point;

public class UserEntityCreator {
  public static UserEntity plainUserEntity() {
    return UserEntity.builder()
        .id(1L)
        .type(UserEntity.Type.PLAIN)
        .firstName("Tony")
        .lastName("Stark")
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
        .firstName("Jolene")
        .lastName("Hornsey")
        .username("Jolene Hornsey")
        .password("88888888")
        .email("jolene-hornsey@bigjo.com")
        .avatarUrl("https://picsum.photos/200/200")
        .businessName("Big Jo Bakery")
        .coverUrls(List.of("https://picsum.photos/800/450"))
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
        .address("318–326 HORNSEY ROAD N7 7HE")
        .build();
  }

  public static UserEntity businessUserEntityShreeji() {
    Point location = LocationCreator.location(51.51871309884953, -0.15449968748875476);
    return UserEntity.builder()
        .id(3L)
        .type(UserEntity.Type.BUSINESS)
        .firstName("Shreeji")
        .lastName("Newsagents")
        .username("Shreeji Newsagents")
        .password("88888888")
        .email("shreejinews@btopenworld.com")
        .avatarUrl("https://www.shreejinewsagents.com/cdn/shop/files/shreeji-logo_400x.png")
        .businessName("Shreeji Newsagents")
        .coverUrls(List.of("https://www.shreejinewsagents.com/cdn/shop/files/6_3024x.jpg"))
        .homepageUrl("https://www.shreejinewsagents.com/")
        .category("Newsagent")
        .introduction(
            "We offer a reliable delivery service for specific titles, newspapers or magazines.")
        .phone("+442079355055")
        .openHour(8)
        .openMinute(30)
        .closeHour(18)
        .closeMinute(0)
        .location(location)
        .address("6 Chiltern St, London W1U 7PT, United Kingdom")
        .build();
  }
}
