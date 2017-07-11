package br.com.umobi.entity;


import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by renan on 10/07/17.
 */

@ParseClassName("Rating")
public class Rating extends ParseUser {
    public static final String PLACE = "place";
    public static final String USER = "user";
    public static final String SCORE = "score";
    public static final String COMMENT = "comment";


    public void setPlace(Place place) {
        put(PLACE, place);
    }

    public Place getPlace() {
        return (Place)get(PLACE);
    }

    public void setUser(User user) {
        put(USER, user);
    }

    public User getUser() {
        return (User)get(USER);
    }

    public void setScore(Number score) {
        put(SCORE, score);
    }

    public Number getScore() {
        return getNumber(SCORE);
    }

    public void setComment(String comment) {
        put(COMMENT, comment);
    }

    public String getComment() {
        return getString(COMMENT);
    }


}