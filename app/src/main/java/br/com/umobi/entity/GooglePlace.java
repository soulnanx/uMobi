package br.com.umobi.entity;

import com.google.android.gms.location.places.Place;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by vagnermartins on 19/09/17.
 */

public class GooglePlace implements Serializable{

    private String id;
    private List<Integer> placeTypes;
    private String address;
    private Locale locale;
    private String name;
    private double latitude;
    private double longitude;
    private String websiteUri;
    private String phoneNumber;
    private float rating;
    private int priceLevel;
    private String attributions;

    public GooglePlace(String id, List<Integer> placeTypes, String address, Locale locale,
                       String name, double latitude, double longitude, String websiteUri,
                       String phoneNumber, float rating, int priceLevel,
                       String attributions) {
        this.id = id;
        this.placeTypes = placeTypes;
        this.address = address;
        this.locale = locale;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.websiteUri = websiteUri;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.priceLevel = priceLevel;
        this.attributions = attributions;
    }

    public static GooglePlace placeToGooglePlace(Place place){
        return new GooglePlace(place.getId(), place.getPlaceTypes(), charSequenceToString(place.getAddress()),
                place.getLocale(), charSequenceToString(place.getName()), place.getLatLng().latitude, place.getLatLng().longitude,
                place.getWebsiteUri() != null ? place.getWebsiteUri().toString() : null, charSequenceToString(place.getPhoneNumber()), place.getRating(),
                place.getPriceLevel(), charSequenceToString(place.getAttributions()));
    }

    private static String charSequenceToString(CharSequence charSequence){
        if(charSequence == null){
            return null;
        }else{
            return charSequence.toString();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getPlaceTypes() {
        return placeTypes;
    }

    public void setPlaceTypes(List<Integer> placeTypes) {
        this.placeTypes = placeTypes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(String websiteUri) {
        this.websiteUri = websiteUri;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

}
