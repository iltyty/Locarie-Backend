package com.locarie.backend.serialization.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalTime;

public class BusinessHoursTimeSerializer extends JsonSerializer<LocalTime> {
  @Override
  public void serialize(
      LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("hour", localTime.getHour());
    jsonGenerator.writeNumberField("minute", localTime.getMinute());
    jsonGenerator.writeEndObject();
  }
}
