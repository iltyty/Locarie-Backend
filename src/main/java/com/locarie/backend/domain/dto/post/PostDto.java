package com.locarie.backend.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.locarie.backend.domain.dto.user.UserDto;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
  private Long id;
  private UserDto user;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
  private Instant time;

  private String content;

  private int favoredByCount;

  List<String> imageUrls;
}
