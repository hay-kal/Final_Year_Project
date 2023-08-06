package com.example.final_year_project;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.builder.TakePictureBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.camera.TakePicture;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.image.TimestampedImageHandle;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

import java.util.List;


public class ViewItemImage extends RobotActivity implements RobotLifecycleCallbacks {
    private byte[] pictureArray;
    private Animate animate;

    private ListStorage listStorage;

    private static final String TAG = "CameraExample";
    private CameraDevice cameraDevice;
    private CameraManager cameraManager;

    // The button used to start take picture action.
    private Button button;
    // An image view used to show the picture.
    private ImageView pictureView;
    // The QiContext provided by the QiSDK.
    private QiContext qiContext;
    // TimestampedImage future.
    private Future<TimestampedImageHandle> timestampedImageHandleFuture;

    Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "ViewImage Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_item_picture);
        // Retrieve the Base64 encoded image string from the intent
        String itemID = getIntent().getStringExtra("UID");
        Log.i(TAG, "UID Intent Recieved");

        if (itemID != null) {
            Log.i(TAG, "UID Intent Contents" + itemID);
        } else {
            Log.e(TAG, "UID Intent NULL");
        }
        // Initialize pictureView
        pictureView = findViewById(R.id.picture_view);
        Back = findViewById(R.id.ivBack);
        Log.i(TAG, "Initialized pictureView");

        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        Log.i(TAG, "Finding Item");
        ListStorage.ListItem selectedItem = findItemById(itemID);
        Log.i(TAG, "Item Found");
        runOnUiThread(() -> displayPicture(selectedItem.image));

        QiSDK.register(this, this);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBackActivity();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your operations
                // For example, you can initialize the QiSDK here
                QiSDK.register(this, this);
            } else {
                // Permission denied, handle this situation (e.g., show a message)
                Log.e(TAG, "PERMISSION DENIED");
            }
        }
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {


        Future<TakePicture> takePictureFuture = TakePictureBuilder.with(qiContext).buildAsync();

        // Create a new say action.
        Say say = SayBuilder.with(qiContext).withText("This is what it looks like, is this your item?").build();



        Say sayBack = SayBuilder.with(qiContext).withText("Going back").build();



        say.run();

        // Create the PhraseSet 1.
        PhraseSet phraseSetBack = PhraseSetBuilder.with(qiContext).withTexts("back", "previous", "previous page", "go back").build();

        // Create the PhraseSet 2.
        PhraseSet phraseSetCheese = PhraseSetBuilder.with(qiContext).withTexts("cheese", "take picture").build();

        // Create a new listen action.
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetBack, phraseSetCheese).build();

        this.qiContext = qiContext;

        while (true) {
            ListenResult listenResult = listen.run();



            Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetBack)) {
                Log.i(TAG, "Heard phrase set: back command");
                sayBack.run();

                Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                        .withResources(R.raw.bowing_b001) // Set the animation resource.
                        .build(); // Build the animation.

                animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                        .withAnimation(animation) // Set the animation.
                        .build(); // Build the animate action.


                // Run the animate action asynchronously.
                Future<Void> animateFuture = animate.async().run();

                Log.i(TAG, "Heard phrase set: Previous Page");


                startBackActivity();

            }
        }
    }

    private ListStorage.ListItem findItemById(String itemId) {
        List<ListStorage.ListItem> items = ListStorage.getInstance(this).getListItems();
        for (ListStorage.ListItem item : items) {
            if (item.getUniqueId().equals(itemId)) {
                Log.i(TAG, "ITEM FOUND!");
                return item;
            }
        }
        Log.i(TAG, "Item NOT found!");
        return null; // Item not found
    }

    private void displayPicture(byte[] pictureData) {
        // Display the picture in the ImageView
        Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
        pictureView.setImageBitmap(pictureBitmap);
    }


    private void startBackActivity() {
        Intent intent = new Intent(ViewItemImage.this, ItemListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRobotFocusLost() {
        this.qiContext = null;
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }


}

