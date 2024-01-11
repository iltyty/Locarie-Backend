package com.locarie.backend.domain.dto.businesshours;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.serialization.deserializers.BusinessHoursTimeDeserializer;
import com.locarie.backend.serialization.serializers.BusinessHoursTimeSerializer;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHoursDto {
  private Long id;
  private DayOfWeek dayOfWeek;
  private Boolean closed;

  @JsonSerialize(using = BusinessHoursTimeSerializer.class)
  @JsonDeserialize(using = BusinessHoursTimeDeserializer.class)
  private LocalTime openingTime;

  @JsonSerialize(using = BusinessHoursTimeSerializer.class)
  @JsonDeserialize(using = BusinessHoursTimeDeserializer.class)
  private LocalTime closingTime;

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  private UserDto user;

  public enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
  }
}
