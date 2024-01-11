package com.locarie.backend.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(exclude = {"businessHours"})
@EqualsAndHashCode(exclude = {"businessHours"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
  private Long id;

  @NotNull(message = "type = PLAIN/BUSINESS is mandatory") private Type type;

  @NotNull(message = "username is mandatory") @Size(min = 2, max = 20, message = "username must be between 2 and 20 characters")
  private String username;

  @NotNull(message = "first name is mandatory") @Size(min = 2, max = 20, message = "first name must be between 2 and 20 characters")
  private String firstName;

  @NotNull(message = "last name is mandatory") @Size(min = 2, max = 20, message = "last name must be between 2 and 20 characters")
  private String lastName;

  @NotNull(message = "email is mandatory") private String email;

  private String avatarUrl;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant birthday;

  // The following fields are only valid for business users
  private String businessName;
  private List<String> coverUrls;
  private String homepageUrl;
  private String category;
  private String introduction;
  private String phone;
  private List<BusinessHoursDto> businessHours;

  @JsonSerialize(using = JtsPointSerializer.class)
  @JsonDeserialize(using = JtsPointDeserializer.class)
  private Point location;

  private String address;

  public enum Type {
    PLAIN,
    BUSINESS
  }
}
