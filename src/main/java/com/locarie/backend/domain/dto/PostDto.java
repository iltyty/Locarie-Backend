package com.locarie.backend.domain.dto;

import java.util.Date;
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
    private Date time;
    private String title;
    private String content;
    private List<String> imageUrls;
}
