package br.com.umobi.entity;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by renan on 10/07/17.
 */

@ParseClassName("Question")
public class Question extends ParseObject{
    public static final String TITLE = "title";
    public static final String IS_RATING = "isRating";
    public static final String PLACE_CATEGORIES = "placeCategories";
    public static final String AVAILABLE = "available";


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

    public static void getByPlaceCategory(final PlaceCategory placeCategory, final FindCallback<Question> serviceFindCallback) {
        ParseQuery.getQuery(Question.class)
                .include(PLACE_CATEGORIES)
                .whereEqualTo(AVAILABLE, true)
                .whereEqualTo(PLACE_CATEGORIES, placeCategory.getObjectId()).findInBackground(serviceFindCallback);
    }
}
