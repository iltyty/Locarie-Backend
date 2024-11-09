package com.locarie.backend.domain.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.locarie.backend.domain.dto.businesshours.BusinessHoursDto;
import com.locarie.backend.serialization.deserializers.JtsPointDeserializer;
import com.locarie.backend.serialization.serializers.JtsPointSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
  private String email;

  @NotNull(message = "username is mandatory") @Size(min = 1, max = 25, message = "username must be between 1 and 25 characters")
  private String username;

  @NotNull(message = "first name is mandatory") @Size(min = 1, max = 25, message = "first name must be between 1 and 25 characters")
  private String firstName;

  @NotNull(message = "last name is mandatory") @Size(min = 1, max = 25, message = "last name must be between 1 and 25 characters")
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
