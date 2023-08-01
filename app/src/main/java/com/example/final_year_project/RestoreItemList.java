package com.example.final_year_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

import java.util.ArrayList;
import java.util.List;

public class RestoreItemList extends RobotActivity implements RobotLifecycleCallbacks {

    Button btnBack, btnLost, btnClear;

    private ListView listView;

    private static final String TAG = "TAG";

    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private QiContext qiContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restore_item);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        listView = findViewById(R.id.listView);
        btnBack = findViewById(R.id.btnBack);
        btnLost = findViewById(R.id.btnLost);
        btnClear = findViewById(R.id.btnClear);

        // Get the QiContext
        QiSDK.register(this, this);

        RestoreStorage restoreStorage = RestoreStorage.getInstance(this);
        List<ListStorage.ListItem> restoredItems = restoreStorage.getRestoreItems();

        ArrayList<String> restoreItemsText = new ArrayList<>();

        for (ListStorage.ListItem listItem : restoredItems) {
            restoreItemsText.add(formatListItem(listItem));
        }

        ArrayAdapter<String> restoreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restoreItemsText);
        listView.setAdapter(restoreAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Retrieve the selected ListItem
            ListStorage.ListItem selectedItem = restoredItems.get(position);
            Log.i(TAG, "Retrieved " + selectedItem.name + " from position: " + position);

            // Create the AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an option");
            builder.setMessage("Do you want to restore this entry?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.i(TAG, "Restore Item clicked");

                    // Remove the selected item from the list
                    ListStorage listStorage = ListStorage.getInstance(RestoreItemList.this);
                    listStorage.addListItem(selectedItem.name, selectedItem.colour, selectedItem.description, selectedItem.date, selectedItem.image);

                    restoreStorage.removeItem(selectedItem);


                    // Update the ArrayList with the new data
                    ArrayList<String> updatedItems = new ArrayList<>();
                    for (ListStorage.ListItem listItem : restoredItems) {
                        restoreItemsText.add(formatListItem(listItem));
                    }

                    // Create an ArrayAdapter to populate the updated ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RestoreItemList.this, android.R.layout.simple_list_item_1, updatedItems);

                    // Set the adapter to the ListView
                    listView.setAdapter(adapter);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.i(TAG, "Restore Item clicked");
                    dialog.dismiss();
                }
            });

            // Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestoreStorage restoreStorage = RestoreStorage.getInstance(RestoreItemList.this);
                restoreStorage.clearRestoreItems();

                // Update the ListView to reflect the cleared items
                ArrayList<String> emptyList = new ArrayList<>();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RestoreItemList.this, android.R.layout.simple_list_item_1, emptyList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBackActivity();

            }
        });

        btnLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLostActivity();

            }
        });

    }

    private void startBackActivity() {
        Intent intent = new Intent(RestoreItemList.this, LostnFoundMain.class);
        startActivity(intent);
    }

    private void startLostActivity() {
        Intent intent = new Intent(RestoreItemList.this, Lost.class);
        startActivity(intent);
    }


    private String formatListItem(ListStorage.ListItem listItem) {
        StringBuilder itemTextBuilder = new StringBuilder();
        // Format the listItem as desired (e.g., concatenate different fields into a single string)
        // For example:
        itemTextBuilder.append("Name: ").append(listItem.name);
        itemTextBuilder.append("  |  Colour: ").append(listItem.colour);
        itemTextBuilder.append("  |  Description: ").append(listItem.description);
        itemTextBuilder.append("  |  Date: ").append(listItem.date);

        if (listItem.image != null) {
            if (itemTextBuilder.length() > 0) {
                itemTextBuilder.append("  |  ");
            }
            itemTextBuilder.append("  |  Image Available?: ").append("Yes");
            Log.i(TAG, "Added Image Link");

        } else if (listItem.image == null) {
            if (itemTextBuilder.length() > 0) {
                itemTextBuilder.append("  |  ");
            }
            itemTextBuilder.append("  |  Image Available?: ").append("No");
            Log.i(TAG, "No Available Image Link");
        }
        // Add other fields if needed
        return itemTextBuilder.toString();
    }


    @Override
    public void onRobotFocusGained(QiContext qiContext) {

    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}
