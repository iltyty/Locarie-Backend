package com.locarie.backend;

import com.locarie.backend.domain.entities.Post;
import com.locarie.backend.domain.entities.User;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.Arrays;
import java.util.List;

public class TestDataUtil {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point newLocation(double latitude, double longitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    public static User newPlainUser() {
        return User.builder()
                .id(1L)
                .type(User.Type.PLAIN)
                .username("Tony Stark")
                .avatarUrl("https://picsum.photos/200/200")
                .build();
    }

    public static User newBusinessUserJoleneHornsey() {
        Point location = newLocation(51.560595, -0.116913);
        return User.builder()
                .id(2L)
                .type(User.Type.BUSINESS)
                .username("Jolene Hornsey")
                .avatarUrl("https://picsum.photos/200/200")
                .coverUrl("https://picsum.photos/800/450")
                .homepageUrl("https://www.bigjobakery.com/")
                .category("Restaurant")
                .introduction("WE ARE A BAKERY & RESTAURANT WHICH SUPPORT REGENERATIVE FOOD SYSTEMS & ETHICAL FARMING.")
                .phone("02039156760")
                .openHour(8)
                .openMinute(0)
                .closeHour(20)
                .closeMinute(0)
                .location(location)
                .locationName("318–326 HORNSEY ROAD N7 7HE")
                .build();
    }

    public static Post newPostJoleneHornsey1(final User user) {
        return Post.builder()
                .id(1L)
                .user(user)
                .time(1700395562L)
                .title("On as weekend spesh: Pistachio frangi & orange curd \uD83C\uDF4A\uD83E\uDDE1")
                .content("On all week: Our Apple & Custard Danish! \uD83C\uDF4F\uD83D\uDC9A")
                .imageUrls(List.of(
                        "https://i.ibb.co/82Qw0Tb/Jolene-Hornsey-Post1-1.jpg"
                ))
                .build();
    }

    public static Post newPostJoleneHornsey2(final User user) {
        return Post.builder()
                .id(2L)
                .user(user)
                .time(1700399162L)
                .title("Jaffa Cake & Sunday lunch \uD83E\uDDE1")
                .content("Today's delight")
                .imageUrls(Arrays.asList(
                        "https://i.ibb.co/b7fyLTq/Jolene-Hornsey-Post-2-1.jpg",
                        "https://i.ibb.co/cYw3z26/Jolene-Hornsey-Post-2-2.jpg"
                ))
                .build();
    }
}
