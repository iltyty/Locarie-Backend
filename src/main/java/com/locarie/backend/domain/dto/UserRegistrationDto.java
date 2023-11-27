package com.locarie.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRegistrationDto extends UserDto {

    @NotNull(message = "password is mandatory")
    @Size(min = 6, max = 20, message = "password must be between 6 and 20 characters")
    private String password;
}
