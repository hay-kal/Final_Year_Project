package com.example.final_year_project;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.example.final_year_project.FeedbackObject;

public class CustomListAdapterFeedback extends ArrayAdapter<FeedbackObject.Feedback> {

    private List<FeedbackObject.Feedback> feedbackList;
    private int resource;
    private Context context;

    public CustomListAdapterFeedback(Context context, int resource, List<FeedbackObject.Feedback> feedbackList) {
        super(context, resource, feedbackList);
        this.resource = resource;
        this.context = context;
        this.feedbackList = feedbackList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        FeedbackObject.Feedback feedbackItem = feedbackList.get(position);

        TextView textRating = convertView.findViewById(R.id.textStarsNo);
        TextView textComment = convertView.findViewById(R.id.textComments);
        TextView textDate = convertView.findViewById(R.id.textDate);

        textRating.setText(String.valueOf(feedbackItem.rating));
        textComment.setText(feedbackItem.comment);
        textDate.setText(feedbackItem.date);


        return convertView;
    }


}

