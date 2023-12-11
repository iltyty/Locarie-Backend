package com.locarie.backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginDto {
    @NotNull(message = "email is mandatory") private String email;

    @NotNull(message = "password is mandatory") @Size(min = 6, max = 20, message = "password must be between 6 and 20 characters")
    private String password;
}
