package com.locarie.backend.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class JtsPointDeserializer extends JsonDeserializer<Point> {

  @Override
  public Point deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    GeometryFactory factory = new GeometryFactory();
    ObjectNode node = jsonParser.readValueAsTree();
    double x = node.get("longitude").asDouble();
    double y = node.get("latitude").asDouble();
    return factory.createPoint(new Coordinate(x, y));
  }
}
