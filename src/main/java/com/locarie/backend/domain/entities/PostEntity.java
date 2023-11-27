package com.locarie.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @CurrentTimestamp(event = EventType.INSERT)
    private Date time;  // post publish time
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "image_index")
    private List<String> imageUrls;
}
