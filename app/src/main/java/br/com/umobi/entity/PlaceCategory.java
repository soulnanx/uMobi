package br.com.umobi.entity;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by renan on 10/07/17.
 */

@ParseClassName("PlaceCategory")
public class PlaceCategory extends ParseUser {
    public static final String TITLE = "title";
    public static final String ICON = "icon";

    public void setTitle(String title) {
        put(TITLE, title);
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public void setIcon(ParseFile icon) {
        put(ICON, icon);
    }

    public ParseFile getIcon() {
        return (ParseFile) get(ICON);
    }


}