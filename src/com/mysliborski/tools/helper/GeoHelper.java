package com.mysliborski.tools.helper;

/**
 * Created by antonimysliborski on 04/09/2013.
 */
public class GeoHelper {

    public static final int RADIUS_EARTH_METERS = 6371; // http://en.wikipedia.org/wiki/Earth_radius#Equatorial_radius

    public static final float DEG2RAD = (float) (Math.PI / 180.0);

    public static int distanceTo(double lat1, double lon1, double lat2, double lon2) {

//        final double a1 = DEG2RAD * (lat1 / 1E6);
//        final double a2 = DEG2RAD * (lon1 / 1E6);
//        final double b1 = DEG2RAD * (lat2 / 1E6);
//        final double b2 = DEG2RAD * (lon2 / 1E6);
//
//        final double cosa1 = Math.cos(a1);
//        final double cosb1 = Math.cos(b1);
//
//        final double t1 = cosa1 * Math.cos(a2) * cosb1 * Math.cos(b2);
//
//        final double t2 = cosa1 * Math.sin(a2) * cosb1 * Math.sin(b2);
//
//        final double t3 = Math.sin(a1) * Math.sin(b1);
//
//        final double tt = Math.acos(t1 + t2 + t3);
//
//        return (int) (RADIUS_EARTH_METERS * tt);


        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = RADIUS_EARTH_METERS * c * 1000;

        return (int) dist;
    }


}
