package com.example.final_year_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FoundItemFormActivity extends RobotActivity implements RobotLifecycleCallbacks {

    ListStorage listStorage;
    EditText etItem, etChar, etColour;
//    private SharedPreferences sharedPreferences;
    Button btnNext;
    ImageView btnPic, btnBack;


    // Store the Animate action.
    private Animate animate;

    private static final String TAG = "CameraExample";
    private CameraDevice cameraDevice;
    private CameraManager cameraManager;

    // The button used to start take picture action.
    private Button button;
    // An image view used to show the picture.
    private ImageView pictureView;
    // The QiContext provided by the QiSDK.
    private QiContext qiContext;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_item_desc);
        byte[] pictureArray = getIntent().getByteArrayExtra("PICTURE_ARRAY");

        Log.i(TAG, "Image Intent Recieved");

        listStorage = ListStorage.getInstance(this);

        etChar = findViewById(R.id.editTextChar);
        etItem = findViewById(R.id.editTextItem);
        etColour = findViewById(R.id.editTextColour);
        btnBack = findViewById(R.id.back);
        btnPic = findViewById(R.id.imageTakePic);
        btnNext = findViewById(R.id.btnNext);



        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);


        QiSDK.register(this, this);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText boxes
                String character = etChar.getText().toString();
                String item = etItem.getText().toString();
                String colour = etColour.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = dateFormat.format(new Date());


//                sharedPreferences = getPreferences(Context.MODE_PRIVATE);
//                etChar.setText(sharedPreferences.getString("character", ""));
//                etItem.setText(sharedPreferences.getString("item", ""));
//                etColour.setText(sharedPreferences.getString("colour", ""));

                // If you have an image byte array, save it to storage
                byte[] imageData = pictureArray; // Replace this with your actual image byte array

                if (imageData != null) {
                    listStorage.saveImageToStorage(getApplicationContext(), "image_" + System.currentTimeMillis(), imageData);
                    Log.i(TAG, "Image Saved to Storage " + System.currentTimeMillis());
                } else {
                    Log.e(TAG, "not saved!");
                }

                // Add the new item to the list and save the updated list to storage
                listStorage.addListItem(item, colour, character, currentDate, imageData);

                //finish();
                startBackActivity();
            }
        });

        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startNextActivity();
            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBackActivity();
            }
        });

    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        this.qiContext = qiContext;

        Future<TakePicture> takePictureFuture = TakePictureBuilder.with(qiContext).buildAsync();

        // Create a new say action.
        Say say = SayBuilder.with(qiContext).withText("Let's record it's information, additionally, you can take a picture of the item").build();

        Say sayNext = SayBuilder.with(qiContext).withText("Next Page!").build();

        Say sayPic = SayBuilder.with(qiContext).withText("Hold up the item clearly in front of me!").build();

        Say sayBack = SayBuilder.with(qiContext).withText("Going back").build();


        Say sayBuggin = SayBuilder.with(qiContext).withText("success").build();

        say.run();

        // Create the PhraseSet 1.
        PhraseSet phraseSetNext = PhraseSetBuilder.with(qiContext).withTexts("next").build();

        // Create the PhraseSet 2.
        PhraseSet phraseSetBack = PhraseSetBuilder.with(qiContext).withTexts("back", "previous").build();

        PhraseSet phraseSetPic = PhraseSetBuilder.with(qiContext).withTexts("picture").build();


        // Create a new listen action.
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetNext, phraseSetBack, phraseSetPic).build();


        while (true) {
            ListenResult listenResult = listen.run();

            Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetNext)) {
                Log.i(TAG, "Heard phrase set: Next Page");

            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetBack)) {
                Log.i(TAG, "Heard phrase set: Previous Page");
                sayBack.run();
                startBackActivity();
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetPic)) {
                Log.i(TAG, "Heard phrase set: Picture Taking Start");


                sayNext.run();

                Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                        .withResources(R.raw.bowing_b001) // Set the animation resource.
                        .build(); // Build the animation.

                animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                        .withAnimation(animation) // Set the animation.
                        .build(); // Build the animate action.

                // Run the animate action asynchronously.
                Future<Void> animateFuture = animate.async().run();

                startNextActivity();


            }
        }


    }

    private void startNextActivity(Bitmap pictureBitmap) {
        Intent intent = new Intent(FoundItemFormActivity.this, FoundItemPicture.class);
        intent.putExtra("pictureBitmap", pictureBitmap);
        startActivity(intent);
    }



    private void startNextActivity() {
        Intent intent = new Intent(FoundItemFormActivity.this, FoundItemPicture.class);
        startActivity(intent);
    }

    private void startBackActivity() {
        Intent intent = new Intent(FoundItemFormActivity.this, LostAndFoundActivity.class);
        startActivity(intent);
    }

    public void takePicture() {
        Future<TakePicture> takePictureFuture = TakePictureBuilder.with(qiContext).buildAsync();
        // Check that the Activity owns the focus.
        if (qiContext == null) {
            return;
        }

        // Disable the button.
        button.setEnabled(false);

        Future<TimestampedImageHandle> timestampedImageHandleFuture = takePictureFuture.andThenCompose(takePicture -> {
            Log.i(TAG, "take picture launched!");
            return takePicture.async().run();
        });
    }




    @Override
    public void onRobotFocusLost() {

        this.qiContext = null;
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }

    @Override
    protected void onPause() {
        super.onPause();


//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("character", etChar.getText().toString());
//        editor.putString("item", etItem.getText().toString());
//        editor.putString("colour", etColour.getText().toString());
//        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (sharedPreferences != null) {
//            etChar.setText(sharedPreferences.getString("character", ""));
//            etItem.setText(sharedPreferences.getString("item", ""));
//            etColour.setText(sharedPreferences.getString("colour", ""));
//        }
    }



}