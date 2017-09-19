package br.com.umobi.entity;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

    public void setPlace(UMobiPlace place) {
        put(PLACE, place);
    }

    public UMobiPlace getPlace() {
        return (UMobiPlace)get(PLACE);
    }

    public void setPlaceCategory(PlaceCategory placeCategory) {
        put(PLACE_CATEGORY, placeCategory);
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

    public void setUser() {
        put(USER, User.getUser());
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

    public static void getByIdPlace(final UMobiPlace place, FindCallback<Answer> callback) {
        ParseQuery.getQuery(Answer.class)
                .include(PLACE)
                .include(QUESTION)
                .include(PLACE_CATEGORY)
                .include(USER)
                .whereEqualTo(PLACE, place)
                .findInBackground(callback);

    }
}
