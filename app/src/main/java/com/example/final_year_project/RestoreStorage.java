package com.example.final_year_project;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RestoreStorage {

    private static RestoreStorage instance;

    private static final String PREF_NAME = "RestoreItems";
    private static final String KEY_RESTORE_ITEMS = "restoreItems";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    private RestoreStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized RestoreStorage getInstance(Context context) {
        if (instance == null) {
            instance = new RestoreStorage(context.getApplicationContext());
        }
        return instance;
    }

    public void addItem(ListStorage.ListItem item) {
        List<ListStorage.ListItem> restoreItems = getRestoreItems();
        if (restoreItems.size() >= 3) {
            // Remove the oldest item to maintain the size of 3
            restoreItems.remove(0);
        }
        restoreItems.add(item);
        saveRestoreItems(restoreItems);

    }

    public void removeItem(ListStorage.ListItem item) {
        List<ListStorage.ListItem> restoreItems = getRestoreItems();
        restoreItems.remove(item);
        saveRestoreItems(restoreItems);
    }

    public void clearRestoreItems() {
        List<ListStorage.ListItem> restoreItems = getRestoreItems();
        restoreItems.clear();
        saveRestoreItems(restoreItems);
    }

    public List<ListStorage.ListItem> getRestoreItems() {
        String json = sharedPreferences.getString(KEY_RESTORE_ITEMS, "[]");
        Type type = new TypeToken<List<ListStorage.ListItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void saveRestoreItems(List<ListStorage.ListItem> restoreItems) {
        String json = gson.toJson(restoreItems);
        sharedPreferences.edit().putString(KEY_RESTORE_ITEMS, json).apply();
    }



}
