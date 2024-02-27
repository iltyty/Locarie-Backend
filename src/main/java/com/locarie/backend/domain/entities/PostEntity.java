package com.locarie.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

  @ManyToMany private List<UserEntity> favoredBy;

  @Column(columnDefinition = "timestamp")
  @CreationTimestamp
  private Instant time; // post publish time

  @Column(nullable = false)
  private String content;

  @ElementCollection(fetch = FetchType.EAGER)
  @OrderColumn(name = "image_index")
  private List<String> imageUrls;
}
