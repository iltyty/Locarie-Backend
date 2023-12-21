package com.locarie.backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
  private Long id;
  private UserDto user;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date time;

  @NotEmpty(message = "title cannot be empty")
  private String title;

  @NotEmpty(message = "content cannot be empty")
  private String content;

  List<String> imageUrls;
}
