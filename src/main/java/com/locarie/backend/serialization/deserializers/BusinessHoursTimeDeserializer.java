package com.locarie.backend.serialization.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.time.LocalTime;

public class BusinessHoursTimeDeserializer extends JsonDeserializer<LocalTime> {
  @Override
  public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    ObjectNode root = jsonParser.readValueAsTree();
    int hour = root.get("hour").asInt();
    int minute = root.get("minute").asInt();
    return LocalTime.of(hour, minute);
  }
}
