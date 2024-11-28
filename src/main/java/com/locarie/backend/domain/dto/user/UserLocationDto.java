package com.locarie.backend.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.locarie.backend.serialization.deserializers.JtsPointDeserializer;
import com.locarie.backend.serialization.serializers.JtsPointSerializer;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationDto {
  private Long id;
  private String avatarUrl;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
  private Instant lastUpdate;

  @JsonSerialize(using = JtsPointSerializer.class)
  @JsonDeserialize(using = JtsPointDeserializer.class)
  private Point location;
}
