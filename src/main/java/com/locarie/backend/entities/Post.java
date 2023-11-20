package com.locarie.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post {
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private Long time;  // post publish time in seconds

    private String title;

    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "image_index")
    private List<String> imageUrls;
}
