package br.com.umobi.entity;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;

/**
 * Created by renan on 10/07/17.
 */

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String NICK_NAME = "nickname";
    public static final String NAME_SEARCH = "nameSearch";
    public static final String DDD = "ddd";
    public static final String PHONE = "phone";
    public static final String AVATAR = "avatar";
    public static final String BIRTHDAY = "birthday";
    public static final String GENDER = "gender";


    public void setNickName(String nickname) {
        put(NICK_NAME, nickname);
        String textWithoutAccent = Normalizer.normalize(nickname.toUpperCase(), Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        put(NAME_SEARCH, textWithoutAccent);
    }

    public void setPhone(String phone) {
        put(PHONE, phone);
    }

    public String getPhone() {
        if (getString(PHONE) != null) {
            return getString(PHONE);
        } else {
            return "";
        }
    }

    public void setDDD(String ddd) {
        put(DDD, ddd);
    }

    public String getDDD() {
        if (getString(DDD) != null) {
            return getString(DDD);
        } else {
            return "";
        }
    }


    public void setBirthday(String birthday) {
        put(BIRTHDAY, birthday);
    }

    public String getBirthday() {
        if (getString(BIRTHDAY) != null) {
            return getString(BIRTHDAY);
        } else {
            return "";
        }
    }

    public void setGender(String gender) {
        put(GENDER, gender);
    }

    public String getGender() {
        if (getString(GENDER) != null) {
            return getString(GENDER);
        } else {
            return "";
        }
    }

    public String getAvatar() {
        return getString(AVATAR);
    }

    public void setAvatar(String url) {
        put(AVATAR, url);
    }

    public String getNickname() {
        if (getString(NICK_NAME) != null) {
            return getString(NICK_NAME);
        } else {
            return "";
        }
    }

    public static User getUser() {
        return (User) getCurrentUser();
    }

    public static void updateFromJson(JSONObject jsonObjectFromParse, SaveCallback saveCallback) {
        User user = User.getUser();
        try {
            user.setNickName(jsonObjectFromParse.getString("name"));

            if (jsonObjectFromParse.has("email")) {
                user.setUsername(jsonObjectFromParse.getString("email"));
                user.setEmail(jsonObjectFromParse.getString("email"));
            }

            if (jsonObjectFromParse.has("birthday")) {
                user.setBirthday(jsonObjectFromParse.getString("birthday"));
            }

            if (jsonObjectFromParse.has("gender")) {
                user.setGender(jsonObjectFromParse.getString("gender"));
            }

            if (user.getAvatar() == null && hasAvatarOnFacebook(jsonObjectFromParse)) {
                user.setAvatar(getAvatarOnFacebook(jsonObjectFromParse));
            }

            user.saveInBackground(saveCallback);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("User - updateFromJson", "get fields from parse user");
        }
    }

    private static boolean hasAvatarOnFacebook(JSONObject jsonObjectFromParse) {
        try {
            if (jsonObjectFromParse.has("picture")) {
                JSONObject data = (JSONObject) jsonObjectFromParse.getJSONObject("picture").get("data");
                if (data != null) {
                    return data.has("url");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getAvatarOnFacebook(JSONObject jsonObjectFromParse) {
        try {
            if (jsonObjectFromParse.has("picture")) {
                JSONObject data = (JSONObject) jsonObjectFromParse.getJSONObject("picture").get("data");
                if (data != null) {
                    return data.getString("url");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isLogged() {
        return ParseUser.getCurrentUser() != null;
    }

    public String getFullPhone() {
        return "(" + getDDD() + ") " + getPhone();
    }
}