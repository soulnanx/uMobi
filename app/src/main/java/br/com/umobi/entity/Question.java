package br.com.umobi.entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by renan on 10/07/17.
 */

@ParseClassName("Question")
public class Question extends ParseObject{
    public static final String TITLE = "title";
    public static final String IS_RATING = "isRating";
    public static final String PLACE_CATEGORIES = "placeCategories";

    public void setTitle(String title) {
        put(TITLE, title);
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public boolean isRating() {
        return getBoolean(IS_RATING);
    }

    public List<PlaceCategory> getPlaceCategories() {
        return getList(PLACE_CATEGORIES);
    }
}
