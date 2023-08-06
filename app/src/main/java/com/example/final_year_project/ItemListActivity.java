package com.example.final_year_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;


import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private ListView listView;
    private static final String TAG = "TAG";

    EditText etSearch;

    ImageView ivBack, ivHome;

    Button btnRestore;

    private CustomListAdapter adapter;
    private QiContext qiContext;

    private List<ListStorage.ListItem> storedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        listView = findViewById(R.id.listView);
        etSearch = findViewById(R.id.etSearch);
        btnRestore = findViewById(R.id.btnLost);
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
                startMenu(ItemListActivity.this);
            }
        });

        // Get the QiContext
        QiSDK.register(this, this);

        // Accessing ListStorage from another class
        ListStorage listStorage = ListStorage.getInstance(this);
        RestoreStorage restoreStorage = RestoreStorage.getInstance(this);

        Log.i(TAG, "ListStorage Instance Created");

        // Get the list of items from ListStorage
        //List<ListStorage.ListItem> storedItems = listStorage.getListItems();
        storedItems = listStorage.getListItems();
        Log.i(TAG, "ListStorage Items Retrieved");

        // Create an instance of CustomListAdapter and set it to the ListView
        adapter = new CustomListAdapter(this, R.layout.custom_row_item, storedItems);
        listView.setAdapter(adapter);
        Log.i(TAG, "ListView Adapted");


        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Retrieve the selected ListItem
            ListStorage.ListItem selectedItem = storedItems.get(position);
            Log.i(TAG, "Retrieved " + selectedItem.name + " from position: " + position);

            // Create the AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an option");
            builder.setMessage("What do you want to do with this item?");

            builder.setPositiveButton("View Image", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Handle Option 1 action here
                    Log.i(TAG, "View Img clicked");
                    // For example, you can start a new activity or perform any other action
                    // Retrieve the selected ListItem
                    ListStorage.ListItem selectedItem = storedItems.get(position);
                    Log.i(TAG, "Retrieved " + selectedItem.name + " from position: " + position);

                    // Check if the selected ListItem has an image
                    if (selectedItem.image != null) {

                        Log.i(TAG, "image passing TRUE: " + selectedItem.getUniqueId());

                        // Pass the UID of the item
                        Intent intent = new Intent(ItemListActivity.this, ViewItemImage.class);
                        intent.putExtra("UID", selectedItem.getUniqueId());
                        Log.i(TAG, "image intent'd: " + selectedItem.getUniqueId());
                        startActivity(intent);

                    } else {
                        Log.i(TAG, "image passing FALSE " + selectedItem);
                        // Replace "YourMessageHere" with the actual message you want to display.
                        showToast("No Image Taken!");
                    }
                }
            });

            builder.setNegativeButton("Claim Item", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Handle Option 2 action here
                    Log.i(TAG, "Claim Item clicked");


                    // Remove the selected item from the list
                    restoreStorage.addItem(selectedItem);

                    //remove
                    storedItems.remove(selectedItem);
                    listStorage.removeListItem(selectedItem.getUniqueId());


                    // Create an ArrayAdapter to populate the updated ListView

                    adapter = new CustomListAdapter(ItemListActivity.this, R.layout.custom_row_item, storedItems);
                    listView.setAdapter(adapter);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();

                    // Display a Toast message to indicate that the item has been claimed
                    showToast("Item claimed!");

                    // Start the Queue activity (or perform any other action after claiming the item)
                    startQueueActivity();
                }

            });

            // Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRestoreActivity();

            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Perform the search with the current text in etSearch
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed
            }
        });

    }


    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Say say = SayBuilder.with(qiContext).withText("Let's see if your item is in our care!").build();

        say.run();

        PhraseSet phraseSetBack = PhraseSetBuilder.with(qiContext).withTexts("Back").build();

        // Create a new listen action.
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetBack).build();

        this.qiContext = qiContext;

        while (true) {
            ListenResult listenResult = listen.run();

            Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetBack)) {
                Log.i(TAG, "Heard phrase set: Previous page command");

                Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                        .withResources(R.raw.bowing_b001) // Set the animation resource.
                        .build(); // Build the animation.

                Animate animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                        .withAnimation(animation) // Set the animation.
                        .build(); // Build the animate action.

                // Run the animate action asynchronously.
                Future<Void> animateFuture = animate.async().run();
                startBackActivity();


            }
        }
    }


    private void startBackActivity() {
        finish();
    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void startFeedback(Context context){
        Intent intent = new Intent(context, FeedbackActivity.class);
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


    private void startQueueActivity() {
        Intent intent = new Intent(ItemListActivity.this, QueueDisplayActivity.class);
        intent.putExtra("category", "Others");
        startActivity(intent);
    }

    private void startRestoreActivity() {
        Intent intent = new Intent(ItemListActivity.this, RestoreItemListActivity.class);
        startActivity(intent);
    }

    private void performSearch(String searchText) {
        // Filter the storedItems list based on the searchText
        List<ListStorage.ListItem> filteredItems = new ArrayList<>();
        for (ListStorage.ListItem item : storedItems) {
            // Perform a case-insensitive check to see if any field contains the searchText
            if (item.name != null && item.name.toLowerCase().contains(searchText.toLowerCase())
                    || (item.colour != null && item.colour.toLowerCase().contains(searchText.toLowerCase()))
                    || (item.description != null && item.description.toLowerCase().contains(searchText.toLowerCase()))
                    || (item.date != null && item.date.toLowerCase().contains(searchText.toLowerCase()))) {
                filteredItems.add(item);
            }
        }

        // Create an instance of CustomListAdapter with the filtered items and set it to the ListView
        adapter = new CustomListAdapter(this, R.layout.custom_row_item, filteredItems);
        listView.setAdapter(adapter);
    }


}


