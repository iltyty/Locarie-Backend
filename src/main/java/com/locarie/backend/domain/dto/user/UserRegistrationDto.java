package com.locarie.backend.domain.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserRegistrationDto extends UserDto {
  @NotNull(message = "password is mandatory") @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters")
  private String password;
}
