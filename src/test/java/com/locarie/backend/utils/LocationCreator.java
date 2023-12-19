package com.locarie.backend.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class LocationCreator {
    public static Point location(double latitude, double longitude) {
        return new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
    }
}
