package com.locarie.backend.datacreators.businesshours;

import com.locarie.backend.domain.entities.BusinessHoursEntity;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class BusinessHoursEntityCreator {
  public static List<BusinessHoursEntity> businessHoursEntities() {
    return List.of(
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.MONDAY)
            .closed(false)
            .openingTime(LocalTime.of(9, 0))
            .closingTime(LocalTime.of(17, 0))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.TUESDAY)
            .closed(false)
            .openingTime(LocalTime.of(9, 0))
            .closingTime(LocalTime.of(17, 0))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.WEDNESDAY)
            .closed(false)
            .openingTime(LocalTime.of(9, 0))
            .closingTime(LocalTime.of(17, 0))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.THURSDAY)
            .closed(false)
            .openingTime(LocalTime.of(9, 0))
            .closingTime(LocalTime.of(17, 0))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.FRIDAY)
            .closed(false)
            .openingTime(LocalTime.of(9, 0))
            .closingTime(LocalTime.of(17, 0))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.SATURDAY)
            .closed(true)
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.SUNDAY)
            .closed(true)
            .build());
  }

  public static List<BusinessHoursEntity> randomBusinessHoursEntities() {
    return List.of(
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.MONDAY)
            .closed(false)
            .openingTime(LocalTime.of(randomHour(), randomMinute()))
            .closingTime(LocalTime.of(randomHour(), randomMinute()))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.TUESDAY)
            .closed(false)
            .openingTime(LocalTime.of(randomHour(), randomMinute()))
            .closingTime(LocalTime.of(randomHour(), randomMinute()))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.WEDNESDAY)
            .closed(false)
            .openingTime(LocalTime.of(randomHour(), randomMinute()))
            .closingTime(LocalTime.of(randomHour(), randomMinute()))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.THURSDAY)
            .closed(false)
            .openingTime(LocalTime.of(randomHour(), randomMinute()))
            .closingTime(LocalTime.of(randomHour(), randomMinute()))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.FRIDAY)
            .closed(false)
            .openingTime(LocalTime.of(randomHour(), randomMinute()))
            .closingTime(LocalTime.of(randomHour(), randomMinute()))
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.SATURDAY)
            .closed(true)
            .build(),
        BusinessHoursEntity.builder()
            .dayOfWeek(BusinessHoursEntity.DayOfWeek.SUNDAY)
            .closed(true)
            .build());
  }

  private static int randomHour() {
    return new Random().nextInt(24);
  }

  private static int randomMinute() {
    return new Random().nextInt(60);
  }
}
