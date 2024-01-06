package com.locarie.backend.domain.entities;

import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "business_hours")
public class BusinessHoursEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated
  @Column(columnDefinition = "tinyint")
  private DayOfWeek dayOfWeek;

  private Boolean closed;

  private LocalTime openingTime;
  private LocalTime closingTime;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  public enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
  }
}
