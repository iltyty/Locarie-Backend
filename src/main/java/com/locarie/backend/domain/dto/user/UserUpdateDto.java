package com.locarie.backend.domain.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.locarie.backend.domain.dto.businesshours.BusinessHoursDto;
import java.time.Instant;
import java.util.List;

import com.locarie.backend.serialization.deserializers.JtsPointDeserializer;
import com.locarie.backend.serialization.serializers.JtsPointSerializer;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
  private String email;
  private String username;
  private String firstName;
  private String lastName;
  private Instant birthday;
  private String businessName;
  private String homepageUrl;
  private List<String> categories;
  private String introduction;
  private String phone;
  private List<BusinessHoursDto> businessHours;

  private String address;
  private String neighborhood;

  @JsonSerialize(using = JtsPointSerializer.class)
  @JsonDeserialize(using = JtsPointDeserializer.class)
  private Point location;
}
