package com.locarie.backend.datacreators.businesshours;

import com.locarie.backend.domain.entities.BusinessHoursEntity;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BusinessHoursEntityCreator {
  public static List<BusinessHoursEntity> businessHoursEntities() {
    return new ArrayList<>(
        List.of(
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.MONDAY)
                .closed(false)
                .openingTime(new ArrayList<>(List.of(LocalTime.of(9, 0), LocalTime.of(18, 0))))
                .closingTime(new ArrayList<>(List.of(LocalTime.of(16, 0), LocalTime.of(21, 30))))
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.TUESDAY)
                .closed(true)
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.WEDNESDAY)
                .closed(false)
                .openingTime(new ArrayList<>(List.of(LocalTime.of(8, 0), LocalTime.of(17, 0))))
                .closingTime(new ArrayList<>(List.of(LocalTime.of(15, 0), LocalTime.of(20, 30))))
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.THURSDAY)
                .closed(false)
                .openingTime(new ArrayList<>(List.of(LocalTime.of(9, 0), LocalTime.of(18, 0))))
                .closingTime(new ArrayList<>(List.of(LocalTime.of(16, 0), LocalTime.of(21, 30))))
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.FRIDAY)
                .closed(false)
                .openingTime(new ArrayList<>(List.of(LocalTime.of(9, 0), LocalTime.of(18, 0))))
                .closingTime(new ArrayList<>(List.of(LocalTime.of(16, 0), LocalTime.of(21, 30))))
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.SATURDAY)
                .closed(true)
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.SUNDAY)
                .closed(true)
                .build()));
  }

  public static List<BusinessHoursEntity> randomBusinessHoursEntities() {
    return new ArrayList<>(
        List.of(
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.MONDAY)
                .closed(false)
                .openingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .closingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.TUESDAY)
                .closed(false)
                .openingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .closingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.WEDNESDAY)
                .closed(false)
                .openingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .closingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.THURSDAY)
                .closed(false)
                .openingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .closingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.FRIDAY)
                .closed(false)
                .openingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .closingTime(new ArrayList<>(List.of(LocalTime.of(randomHour(), randomMinute()))))
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.SATURDAY)
                .closed(true)
                .build(),
            BusinessHoursEntity.builder()
                .dayOfWeek(BusinessHoursEntity.DayOfWeek.SUNDAY)
                .closed(true)
                .build()));
  }

  private static int randomHour() {
    return new Random().nextInt(24);
  }

  private static int randomMinute() {
    return new Random().nextInt(60);
  }
}
