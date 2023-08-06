package com.example.final_year_project;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FeedbackObject {

    private static FeedbackObject instance;

    private static final String PREF_NAME = "MyFeedback";
    private static final String KEY_FEEDBACKS = "feedbacks";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public static class Feedback {
        float rating;
        String comment;
        String date;
        String uniqueId;

        // Constructor
        public Feedback(float rating, String comment, String date) {
            this.rating = rating;
            this.comment = comment;
            this.date = date;
            this.uniqueId = generateUniqueId();
        }

        // Getters and Setters
        public float getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }

        public String getDate() {
            return date;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        private String generateUniqueId() {
            return UUID.randomUUID().toString();
        }
    }

    private FeedbackObject(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized FeedbackObject getInstance(Context context) {
        if (instance == null) {
            instance = new FeedbackObject(context.getApplicationContext());
        }
        return instance;
    }

    public void addFeedback(float rating, String comment, String date) {
        List<Feedback> feedbacks = getFeedbacks();
        Feedback feedback = new Feedback(rating, comment, date);
        feedbacks.add(feedback);
        saveFeedbacks(feedbacks);
    }

    public void addFeedback(float rating, String date) {
        addFeedback(rating, null, date);
    }

    public List<Feedback> getFeedbacks() {
        String json = sharedPreferences.getString(KEY_FEEDBACKS, "[]");
        Type type = new TypeToken<List<Feedback>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private void saveFeedbacks(List<Feedback> feedbacks) {
        String json = gson.toJson(feedbacks);
        sharedPreferences.edit().putString(KEY_FEEDBACKS, json).apply();
    }

    public void removeFeedback(String uniqueId) {
        List<Feedback> feedbacks = getFeedbacks();

        // Find the index of the feedback with the specified uniqueId
        int indexToRemove = -1;
        for (int i = 0; i < feedbacks.size(); i++) {
            Feedback feedback = feedbacks.get(i);
            if (feedback.getUniqueId().equals(uniqueId)) {
                indexToRemove = i;
                break;
            }
        }

        // If the feedback is found, remove it from the list
        if (indexToRemove >= 0) {
            feedbacks.remove(indexToRemove);
            saveFeedbacks(feedbacks);
        }
    }

    public void clearFeedbacks() {
        List<Feedback> feedbacks = getFeedbacks();
        feedbacks.clear(); // Remove all feedback objects from the list
        saveFeedbacks(feedbacks); // Update the saved data in SharedPreferences
    }
}
