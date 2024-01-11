package com.locarie.backend.serialization.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.locationtech.jts.geom.Point;

public class JtsPointSerializer extends JsonSerializer<Point> {
  @Override
  public void serialize(
      Point point, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("latitude", point.getY());
    jsonGenerator.writeNumberField("longitude", point.getX());
    jsonGenerator.writeEndObject();
  }
}
