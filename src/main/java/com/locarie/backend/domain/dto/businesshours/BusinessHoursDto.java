package com.locarie.backend.domain.dto.businesshours;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.serialization.deserializers.BusinessHoursTimeDeserializer;
import com.locarie.backend.serialization.serializers.BusinessHoursTimeSerializer;
import java.time.LocalTime;
import java.util.List;
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

  @JsonSerialize(contentUsing = BusinessHoursTimeSerializer.class)
  @JsonDeserialize(contentUsing = BusinessHoursTimeDeserializer.class)
  private List<LocalTime> openingTime;

  @JsonSerialize(contentUsing = BusinessHoursTimeSerializer.class)
  @JsonDeserialize(contentUsing = BusinessHoursTimeDeserializer.class)
  private List<LocalTime> closingTime;

  @JsonBackReference private UserDto user;

  public enum DayOfWeek {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    DayOfWeek(String value) {
      this.value = value;
    }

    private final String value;

    @JsonValue
    public String getValue() {
      return value;
    }
  }
}
