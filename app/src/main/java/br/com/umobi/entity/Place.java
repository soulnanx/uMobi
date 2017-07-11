package br.com.umobi.entity;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by renan on 10/07/17.
 */

@ParseClassName("Place")
public class Place extends ParseObject{
    public static final String TITLE = "title";
    public static final String LAT_LONG = "latlong";
    public static final String ADDRESS = "address";
    public static final String POSTAL_CODE = "postalCode";
    public static final String DESCRIPTION = "description";
    public static final String ENABLED = "enabled";

    public void setTitle(String title) {
        put(TITLE, title);
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public void setLatLong(LatLng latLong) {
        ParseGeoPoint geoPoint = new ParseGeoPoint();
        geoPoint.setLatitude(latLong.latitude);
        geoPoint.setLongitude(latLong.longitude);

        put(LAT_LONG, geoPoint);
    }

    public LatLng getLatLong() {
        ParseGeoPoint geo = getParseGeoPoint(LAT_LONG);

        if (geo != null){
            return new LatLng(geo.getLatitude(), geo.getLongitude());
        } else {
            return null;
        }

    }

    public void setAddress(String address) {
        put(ADDRESS, address);
    }

    public String getAddress() {
        return getString(ADDRESS);
    }

    public void setPostalCode(String postalCode) {
        put(POSTAL_CODE, postalCode);
    }

    public String getPostalCode() {
        return getString(POSTAL_CODE);
    }

    public void setDescription(String description) {
        put(DESCRIPTION, description);
    }

    public String getDescription() {
        return getString(DESCRIPTION);
    }

    public boolean isEnabled() {
        return getBoolean(ENABLED);
    }

    public void setEnabled(boolean isEnabled) {
        put(ENABLED, isEnabled);
    }



    public static void getPlacesNearMe(int meters, LatLng latlong, FindCallback<Place> callback){
        ParseGeoPoint geoPoint = new ParseGeoPoint(latlong.latitude, latlong.longitude);
        ParseQuery.getQuery(Place.class)
                .whereWithinKilometers(Place.LAT_LONG, geoPoint, meters)
                .whereEqualTo(Place.ENABLED, true)
                .findInBackground(callback);
    }


}
