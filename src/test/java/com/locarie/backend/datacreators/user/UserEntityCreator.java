package com.locarie.backend.datacreators.user;

import com.locarie.backend.datacreators.LocationCreator;
import com.locarie.backend.datacreators.businesshours.BusinessHoursEntityCreator;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.domain.enums.BusinessCategory;
import java.time.Instant;
import java.util.ArrayList;
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
        .birthday(Instant.now())
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
        .birthday(Instant.now())
        .businessName("Big Jo Bakery")
        .categories(
            new ArrayList<>(
                List.of(BusinessCategory.FOOD.getValue(), BusinessCategory.SHOP.getValue())))
        .profileImageUrls(new ArrayList<>(List.of("https://picsum.photos/800/450")))
        .homepageUrl("https://www.bigjobakery.com/")
        .introduction(
            "WE ARE A BAKERY & RESTAURANT WHICH SUPPORT REGENERATIVE FOOD SYSTEMS &"
                + " ETHICAL FARMING.")
        .phone("02039156760")
        .location(location)
        .address("318–326 HORNSEY ROAD N7 7HE")
        .neighborhood("Upper Holloway")
        .businessHours(BusinessHoursEntityCreator.businessHoursEntities())
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
        .birthday(Instant.now())
        .businessName("Shreeji Newsagents")
        .categories(
            new ArrayList<>(
                List.of(BusinessCategory.ART.getValue(), BusinessCategory.LIFESTYLE.getValue())))
        .profileImageUrls(
            new ArrayList<>(
                List.of("https://www.shreejinewsagents.com/cdn/shop/files/6_3024x.jpg")))
        .homepageUrl("https://www.shreejinewsagents.com/")
        .introduction(
            "We offer a reliable delivery service for specific titles, newspapers or magazines.")
        .phone("+442079355055")
        .location(location)
        .neighborhood("Marylebone")
        .address("6 Chiltern St, London W1U 7PT, United Kingdom")
        .businessHours(BusinessHoursEntityCreator.businessHoursEntities())
        .build();
  }
}
