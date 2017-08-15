package br.com.umobi.entity;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Comment;

/**
 * Created by renan on 10/07/17.
 */

@ParseClassName("Answer")
public class Answer extends ParseObject{
    public static final String PLACE = "place";
    public static final String PLACE_CATEGORY = "placeCategory";
    public static final String QUESTION = "question";
    public static final String USER = "user";
    public static final String RATING = "rating";
    public static final String COMMENT = "comment";
    public static final String ANSWER = "answer";

    public void setPlace(Place place) {
        put(PLACE, place);
    }

    public Place getPlace() {
        return (Place)get(PLACE);
    }

    public void setPlaceCategory(PlaceCategory place) {
        put(PLACE_CATEGORY, place);
    }

    public PlaceCategory getPlaceCategory() {
        return (PlaceCategory)get(PLACE_CATEGORY);
    }

    public void setQuestion(Question question) {
        put(QUESTION, question);
    }

    public Question getQuestion() {
        return (Question)get(QUESTION);
    }

    public void setUser(User user) {
        put(USER, user);
    }

    public void setRating(int rating) {
        put(RATING, rating);
    }

    public int getRating() {
        return getInt(RATING);
    }

    public void setComment(String comment) {
        put(COMMENT, comment);
    }

    public String getComment() {
        return getString(COMMENT);
    }

    public void setAnswer(int answer) {
        put(ANSWER, answer);
    }

    public int getAnswer() {
        return getInt(ANSWER);
    }

}
