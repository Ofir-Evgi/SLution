package com.example.slution;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FavoriteStorage {

    private static final String PREF_NAME = "favorites_pref";
    private static final String KEY_FAVORITES = "favorites";

    public static void saveFavorites(Context context, List<ChatItem> favorites) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favorites);
        editor.putString(KEY_FAVORITES, json);
        editor.apply();
    }

    public static List<ChatItem> loadFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_FAVORITES, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ChatItem>>(){}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public static void addFavorite(Context context, ChatItem item) {
        List<ChatItem> current = loadFavorites(context);
        if (!current.contains(item)) {
            current.add(item);
            saveFavorites(context, current);
        }
    }

    public static void removeFavorite(Context context, ChatItem item) {
        List<ChatItem> current = loadFavorites(context);
        current.remove(item);
        saveFavorites(context, current);
    }

    public static boolean isFavorite(Context context, ChatItem item) {
        return loadFavorites(context).contains(item);
    }
}
