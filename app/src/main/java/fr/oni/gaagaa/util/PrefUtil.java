package fr.oni.gaagaa.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public final class PrefUtil {

    private PrefUtil() {

    }

    public static final String TWITTER = "twitter";
    public static final String TOKEN = "token";
    public static final String TOKEN_SECRET = "tokenSecret";

    private static void setStringPreferences(Activity activity, String name, String key,
                                             String value) {
        SharedPreferences settings = activity.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getStringPreferences(Activity activity, String name, String key) {
        SharedPreferences settings = activity.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    public static void setTwitterToken(Activity activity, String token) {
        setStringPreferences(activity, TWITTER, TOKEN, token);
    }

    public static void setTwitterTokenSecret(Activity activity, String tokenSecret) {
        setStringPreferences(activity, TWITTER, TOKEN_SECRET, tokenSecret);
    }

    public static String getTwitterToken(Activity activity) {
        return getStringPreferences(activity, TWITTER, TOKEN);
    }

    public static String getTwitterTokenSecret(Activity activity) {
        return getStringPreferences(activity, TWITTER, TOKEN_SECRET);
    }

}
