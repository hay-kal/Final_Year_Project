package com.example.final_year_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.final_year_project.ListStorage;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<ListStorage.ListItem> {
    private Context context;
    private int resource;
    private List<ListStorage.ListItem> items;

    public CustomListAdapter(Context context, int resource, List<ListStorage.ListItem> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the custom_row_item.xml layout if convertView is null
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        // Get the current item from the List
        ListStorage.ListItem currentItem = items.get(position);

        // Find the TextViews in the custom_row_item.xml layout
        TextView textName = convertView.findViewById(R.id.textName);
        TextView textColour = convertView.findViewById(R.id.textColour);
        TextView textDescription = convertView.findViewById(R.id.textDescription);
        TextView textDate = convertView.findViewById(R.id.textDate);
        TextView textImage = convertView.findViewById(R.id.textImage);

        // Set the data from the currentItem to the TextViews
        textName.setText(currentItem.name);
        textColour.setText(currentItem.colour);
        textDescription.setText(currentItem.description);
        textDate.setText(currentItem.date);

        // Set the Image availability text based on the presence of the image
        if (currentItem.image != null) {
            textImage.setText("Yes");
        } else {
            textImage.setText("No");
        }

        return convertView;
    }
}
