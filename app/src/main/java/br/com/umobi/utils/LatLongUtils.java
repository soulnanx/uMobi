package br.com.umobi.utils;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by renan on 11/07/17.
 */

public class LatLongUtils {


    public static double calcDistance(LatLng point1, LatLng point2) {
        long R = 6378137L;
        double dLat = rad(point2.latitude - point1.latitude);
        double dLong = rad(point2.longitude - point1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(rad(point1.latitude)) * Math.cos(rad(point2.latitude))
                * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private static double rad(double lat) {
        return lat * Math.PI / 180;
    }

    public static LatLng parseAddress(Address address){
        return new LatLng(address.getLatitude(), address.getLongitude());
    }

}
