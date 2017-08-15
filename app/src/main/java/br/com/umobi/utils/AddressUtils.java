package br.com.umobi.utils;

import android.location.Address;

/**
 * Created by renan on 14/08/17.
 */

public class AddressUtils {

    public static String getFullAddress(Address address){
        return new StringBuilder()
                .append(address.getThoroughfare())
                .append(", ")
                .append(address.getFeatureName())
                .append(", ")
                .append(address.getSubLocality())
                .append(", ")
                .append(address.getLocality()).toString();
    }
}
