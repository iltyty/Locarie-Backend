package com.locarie.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

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

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(columnDefinition = "timestamp")
  @CurrentTimestamp(event = EventType.INSERT)
  private Instant time; // post publish time

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @ElementCollection(fetch = FetchType.EAGER)
  @OrderColumn(name = "image_index")
  private List<String> imageUrls;
}
