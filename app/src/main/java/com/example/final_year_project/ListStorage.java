package com.example.final_year_project;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ListStorage {

    private static ListStorage instance;

    private static final String PREF_NAME = "MyListItems";
    private static final String KEY_LIST_ITEMS = "listItems";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public class ListItem {
        String name;

        String colour;
        String description;

        String date;
        byte[] image;

        private String uniqueId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListItem listItem = (ListItem) o;
            return Objects.equals(uniqueId, listItem.uniqueId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uniqueId);
        }





        // Constructor, getters, and setters (if needed)
        public ListItem(String name, String colour, String description, String date, byte[] image) {
            this.name = name;
            this.colour = colour;
            this.description = description;
            this.date = date;
            this.image = image;
        }

        public byte[] getImageArr() {
            return image;
        }

        public void setImageArr(byte[] pictureArray) {
            this.image = pictureArray;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }
    }

    private ListStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized ListStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ListStorage(context.getApplicationContext());
        }
        return instance;
    }

    public void addListItem(String name, String colour, String description, String date, byte[] image) {
        List<ListItem> listItems = getListItems();
        ListItem listItem = new ListItem(name, colour, description, date, image);

        String uniqueId = generateUniqueId();
        listItem.setUniqueId(uniqueId);

        listItems.add(listItem);
        saveListItems(listItems);

    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    //without image
    public void addListItem(String name, String colour, String description, String date) {
        addListItem(name, colour, description, date, null);
    }


    public boolean saveImageToStorage(Context context, String imageName, byte[] imageData) {
        try {
            File imagesDir = new File(context.getFilesDir(), "images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }

            File imageFile = new File(imagesDir, imageName + ".jpg");
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageData);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ListItem> getListItems() {
        String json = sharedPreferences.getString(KEY_LIST_ITEMS, "[]");
        Type type = new TypeToken<List<ListItem>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private void saveListItems(List<ListItem> listItems) {
        String json = gson.toJson(listItems);
        sharedPreferences.edit().putString(KEY_LIST_ITEMS, json).apply();
    }

    public void removeListItem(String uniqueId) {
        List<ListItem> listItems = getListItems();

        // Find the index of the item with the specified uniqueId
        int indexToRemove = -1;
        for (int i = 0; i < listItems.size(); i++) {
            ListItem listItem = listItems.get(i);
            if (listItem.getUniqueId().equals(uniqueId)) {
                indexToRemove = i;
                break;
            }
        }

        // If the item is found, remove it from the list
        if (indexToRemove >= 0) {
            listItems.remove(indexToRemove);
            saveListItems(listItems);
        }
    }
}
