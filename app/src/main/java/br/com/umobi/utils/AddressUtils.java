package br.com.umobi.utils;

import android.location.Address;

/**
 * Created by renan on 14/08/17.
 */

public class AddressUtils {

    public static String getFullAddress(Address address) {
        return new StringBuilder()
                .append(address.getThoroughfare() != null ? address.getThoroughfare()       : "")
                .append(address.getFeatureName()  != null ? ", " + address.getFeatureName() : "")
                .append(address.getSubLocality()  != null ? ", " + address.getSubLocality() : "")
                .append(address.getLocality()     != null ? ", " + address.getLocality()    : "").toString();
    }
}
