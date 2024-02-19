package com.locarie.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.locarie.backend.serialization.deserializers.JtsPointDeserializer;
import com.locarie.backend.serialization.serializers.JtsPointSerializer;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"businessHours"})
@Entity
@Table(name = "users")
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user-id-generator")
  @SequenceGenerator(name = "user-id-generator", sequenceName = "user_id_seq", allocationSize = 1)
  private Long id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Type type;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @ColumnDefault("''")
  private String avatarUrl;

  @Column(columnDefinition = "timestamp")
  private Instant birthday;

  // The following fields are only valid for business users
  private String businessName;

  @ElementCollection(fetch = FetchType.EAGER)
  @OrderColumn(name = "profile_image_index")
  private List<String> profileImageUrls;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BusinessHoursEntity> businessHours;

  @ManyToMany(mappedBy = "favoredBy", fetch = FetchType.LAZY)
  private List<PostEntity> favoritePosts;

  @ManyToMany(mappedBy = "favoredBy", fetch = FetchType.LAZY)
  private List<UserEntity> favoriteBusinesses;

  @ManyToMany private List<UserEntity> favoredBy;

  private String homepageUrl;
  private String category;
  private String introduction;
  private String phone;

  @JsonSerialize(using = JtsPointSerializer.class)
  @JsonDeserialize(using = JtsPointDeserializer.class)
  @Column(columnDefinition = "Point")
  private Point location;

  private String address;

  public enum Type {
    PLAIN,
    BUSINESS
  }
}
