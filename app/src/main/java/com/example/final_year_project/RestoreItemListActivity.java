package com.example.final_year_project;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.DialogInterface;


import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

import java.util.ArrayList;
import java.util.List;

public class RestoreItemListActivity extends RobotActivity implements RobotLifecycleCallbacks {

    Button btnLost, btnClear;

    ImageView ivBack, ivHome;

    private ListView listView;

    private static final String TAG = "TAG";

    private CustomListAdapter adapter;
    private ArrayList<String> items;
    private QiContext qiContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restore_item);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        listView = findViewById(R.id.listView);
        btnLost = findViewById(R.id.btnLost);
        btnClear = findViewById(R.id.btnClear);

        ivBack = findViewById(R.id.ivBack);
        ivHome = findViewById(R.id.ivHome);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For example, start a new activity using an Intent
                // To be replaced with startEvents after testing
                startBackActivity();
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMenu(RestoreItemListActivity.this);
            }
        });

        // Get the QiContext
        QiSDK.register(this, this);

        RestoreStorage restoreStorage = RestoreStorage.getInstance(this);
        List<ListStorage.ListItem> restoredItems = restoreStorage.getRestoreItems();

//        ArrayList<String> restoreItemsText = new ArrayList<>();
//        for (ListStorage.ListItem listItem : restoredItems) {
//            restoreItemsText.add(formatListItem(listItem));
//        }
        adapter = new CustomListAdapter(this, R.layout.custom_row_item, restoredItems);
        listView.setAdapter(adapter);
        Log.i(TAG, "ListView Adapted");

//        ArrayAdapter<String> restoreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restoreItemsText);
//        listView.setAdapter(restoreAdapter);

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
                    ListStorage listStorage = ListStorage.getInstance(RestoreItemListActivity.this);
                    listStorage.addListItem(selectedItem.name, selectedItem.colour, selectedItem.description, selectedItem.date, selectedItem.image);

                    restoreStorage.removeItem(selectedItem);


                    // Update the ArrayList with the new data
//                    ArrayList<String> updatedItems = new ArrayList<>();
//                    for (ListStorage.ListItem listItem : restoredItems) {
//                        restoreItemsText.add(formatListItem(listItem));
//                    }

                    adapter = new CustomListAdapter(RestoreItemListActivity.this, R.layout.custom_row_item, restoredItems);
                    listView.setAdapter(adapter);
                    Log.i(TAG, "ListView Adapted");

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
                RestoreStorage restoreStorage = RestoreStorage.getInstance(RestoreItemListActivity.this);
                restoreStorage.clearRestoreItems();

                // Update the ListView to reflect the cleared items
                ArrayList<String> emptyList = new ArrayList<>();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RestoreItemListActivity.this, android.R.layout.simple_list_item_1, emptyList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });


        btnLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLostActivity(RestoreItemListActivity.this);

            }
        });

    }

    private void startLostActivity(Context context){
        Intent intent = new Intent(context, LostAndFoundActivity.class);
        context.startActivity(intent);
    }
    private void startMenu(Context context){
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }

    private void startHome(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void startEvents(Context context){
        Intent intent = new Intent(context, EventsActivity.class);
        context.startActivity(intent);
    }

    private void startRPS(Context context){
        Intent intent = new Intent(context, RPSActivity.class);
        context.startActivity(intent);
    }
    private void startTTT(Context context){
        Intent intent = new Intent(context, TicTacToeActivity.class);
        context.startActivity(intent);
    }

    private void startEntertainmentActivity(Context context){
        Intent intent = new Intent(context, EntertainmentActivity.class);
        context.startActivity(intent);
    }

    private void startLnFActivity(Context context){
        Intent intent = new Intent(context, LostAndFoundActivity.class);
        context.startActivity(intent);
    }

    private void startGuidanceActivity(Context context){
        Intent intent = new Intent(context, GuidanceActivity.class);
        context.startActivity(intent);
    }

    private void startFAQActivity(Context context){
        Intent intent = new Intent(context, FAQsMenuActivity.class);
        context.startActivity(intent);
    }

    private void startBackActivity() {
        finish();
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
