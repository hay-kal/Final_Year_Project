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
import android.widget.SearchView;
import android.widget.Toast;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
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

public class Lost extends RobotActivity implements RobotLifecycleCallbacks {
    private ListView listView;
    private static final String TAG = "TAG";

    Button btnBack, btnRestore;

    private SearchView searchView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private QiContext qiContext;

    private List<ListStorage.ListItem> storedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        listView = findViewById(R.id.listView);
        btnBack = findViewById(R.id.btnBack);
        searchView = findViewById(R.id.searchView);
        btnRestore = findViewById(R.id.btnRestore);

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

        // Create an ArrayList to store the text items
        ArrayList<String> items = new ArrayList<>();
        Log.i(TAG, "ArrayList Created");

        // Add the names of the stored items to the ArrayList
        if (storedItems != null) {
            for (ListStorage.ListItem listItem : storedItems) {

                // Initialize a StringBuilder to construct the itemText
                StringBuilder itemTextBuilder = new StringBuilder();

                // Append the name if it's not null
                if (listItem.name != null) {
                    itemTextBuilder.append("Name: ").append(listItem.name);
                    Log.i(TAG, "Added" + listItem.name);
                }

                // Append the colour if it's not null
                if (listItem.colour != null) {
                    if (itemTextBuilder.length() > 0) {
                        itemTextBuilder.append("  |  ");
                    }
                    itemTextBuilder.append("Colour: ").append(listItem.colour);
                    Log.i(TAG, "Added" + listItem.colour);
                }

                // Append the description if it's not null
                if (listItem.description != null) {
                    if (itemTextBuilder.length() > 0) {
                        itemTextBuilder.append("  |  ");
                    }
                    itemTextBuilder.append("Description: ").append(listItem.description);
                    Log.i(TAG, "Added" + listItem.description);
                }

                // Append the date if it's not null
                if (listItem.date != null) {
                    if (itemTextBuilder.length() > 0) {
                        itemTextBuilder.append("  |  ");
                    }
                    itemTextBuilder.append("Date: ").append(listItem.date);
                    Log.i(TAG, "Added" + listItem.date);
                }

                if (listItem.image != null) {
                    if (itemTextBuilder.length() > 0) {
                        itemTextBuilder.append("  |  ");
                    }
                    itemTextBuilder.append("Image Available?: ").append("Yes");
                    Log.i(TAG, "Added Image Link");

                } else if (listItem.image == null) {
                    if (itemTextBuilder.length() > 0) {
                        itemTextBuilder.append("  |  ");
                    }
                    itemTextBuilder.append("Image Available?: ").append("No");
                    Log.i(TAG, "No Available Image Link");
                }

                // Add the concatenated string to the ArrayList
                items.add(itemTextBuilder.toString());

                Log.i(TAG, "Added");
            }
        }
        Log.i(TAG, "Finished Adding");


        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        Log.i(TAG, "Adapter Created");

        // Set the adapter to the ListView
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
                        Intent intent = new Intent(Lost.this, ViewItemImage.class);
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

                    // Update the ArrayList with the new data
                    ArrayList<String> updatedItems = new ArrayList<>();
                    for (ListStorage.ListItem listItem : storedItems) {
                        StringBuilder itemTextBuilder = new StringBuilder();

                        // Append the name if it's not null
                        if (listItem.name != null) {
                            itemTextBuilder.append("Name: ").append(listItem.name);
                            Log.i(TAG, "Added" + listItem.name);
                        }

                        // Append the colour if it's not null
                        if (listItem.colour != null) {
                            if (itemTextBuilder.length() > 0) {
                                itemTextBuilder.append("  |  ");
                            }
                            itemTextBuilder.append("Colour: ").append(listItem.colour);
                            Log.i(TAG, "Added" + listItem.colour);
                        }

                        // Append the description if it's not null
                        if (listItem.description != null) {
                            if (itemTextBuilder.length() > 0) {
                                itemTextBuilder.append("  |  ");
                            }
                            itemTextBuilder.append("Description: ").append(listItem.description);
                            Log.i(TAG, "Added" + listItem.description);
                        }

                        // Append the date if it's not null
                        if (listItem.date != null) {
                            if (itemTextBuilder.length() > 0) {
                                itemTextBuilder.append("  |  ");
                            }
                            itemTextBuilder.append("Date: ").append(listItem.date);
                            Log.i(TAG, "Added" + listItem.date);
                        }

                        if (listItem.image != null) {
                            if (itemTextBuilder.length() > 0) {
                                itemTextBuilder.append("  |  ");
                            }
                            itemTextBuilder.append("Image Available?: ").append("Yes");
                            Log.i(TAG, "Added Image Link");

                        } else if (listItem.image == null) {
                            if (itemTextBuilder.length() > 0) {
                                itemTextBuilder.append("  |  ");
                            }
                            itemTextBuilder.append("Image Available?: ").append("No");
                            Log.i(TAG, "No Available Image Link");
                        }

                        // Add the concatenated string to the ArrayList
                        updatedItems.add(itemTextBuilder.toString());
                    }

                    // Create an ArrayAdapter to populate the updated ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Lost.this, android.R.layout.simple_list_item_1, updatedItems);

                    // Set the adapter to the ListView
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBackActivity();

            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRestoreActivity();

            }

        });

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterItems(newText);
//                return false;
//            }
//        });

        // Initialize the ArrayList and adapter
//        items = new ArrayList<>();
//
//        if (items != null) {
//            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
//        }

//        // Set the adapter to the ListView
//        listView.setAdapter(adapter);

        // Load all items initially

//        if (items != null) {
//            loadAllItems();
//        }

    }


    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Say say = SayBuilder.with(qiContext).withText("Let's see if your item is in our care!").build();

        say.run();

        // Create the PhraseSet 1.
        PhraseSet phraseSetLost = PhraseSetBuilder.with(qiContext).withTexts("lost").build();

        // Create the PhraseSet 2.
        PhraseSet phraseSetFound = PhraseSetBuilder.with(qiContext).withTexts("found").build();


        PhraseSet phraseSetBack = PhraseSetBuilder.with(qiContext).withTexts("Back").build();


        // Create a new listen action.
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetBack, phraseSetLost).build();

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


            } //else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetFound)) {
//                Log.i(TAG, "Heard phrase set: It seems you have FOUND an item");
//                sayFound.run();
//                startFoundActivity();
//
//            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetSkip)) {
//                Log.i(TAG, "Heard phrase set: skipping page");
//                sayFound.run();
//                startPicActivity();
//            }
        }
    }

    private void startBackActivity() {
        Intent intent = new Intent(Lost.this, LostnFoundMain.class);
        startActivity(intent);
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

    private void startQueueActivity() {
        Intent intent = new Intent(Lost.this, QueueSelect.class);
        startActivity(intent);
    }

    private void startRestoreActivity() {
        Intent intent = new Intent(Lost.this, RestoreItemList.class);
        startActivity(intent);
    }

//    // Method to filter items based on the search query
//    private void filterItems(String query) {
//        if (items != null) {
//            items.clear();
//            for (ListStorage.ListItem listItem : storedItems) {
//                if (listItem.name != null && listItem.name.toLowerCase().contains(query.toLowerCase())) {
//                    // Add the item to the list if the name contains the query
//                    items.add(formatListItem(listItem));
//                }
//            }
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    // Method to load all items (without filtering)
//    private void loadAllItems() {
//        if (items != null) {
//            items.clear();
//
//            for (ListStorage.ListItem listItem : storedItems) {
//                items.add(formatListItem(listItem));
//            }
//            adapter.notifyDataSetChanged();
//        }
//    }

    // Method to format a ListItem as a single string
    private String formatListItem(ListStorage.ListItem listItem) {
        StringBuilder itemTextBuilder = new StringBuilder();

        // Append the name if it's not null
        if (listItem.name != null) {
            itemTextBuilder.append("Name: ").append(listItem.name);
        }

        // Append the colour if it's not null
        if (listItem.colour != null) {
            itemTextBuilder.append("  |  Colour: ").append(listItem.colour);
        }

        // Append the description if it's not null
        if (listItem.description != null) {
            itemTextBuilder.append("  |  Description: ").append(listItem.description);
        }

        // Append the date if it's not null
        if (listItem.date != null) {
            itemTextBuilder.append("  |  Date: ").append(listItem.date);
        }

        if (listItem.image != null) {
            itemTextBuilder.append("  |  Image Available?: Yes");
        } else {
            itemTextBuilder.append("  |  Image Available?: No");
        }

        return itemTextBuilder.toString();
    }


}
