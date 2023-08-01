package com.example.final_year_project;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.QiContext;
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
import com.aldebaran.qi.sdk.object.image.EncodedImage;
import com.aldebaran.qi.sdk.object.image.EncodedImageHandle;
import com.aldebaran.qi.sdk.object.image.TimestampedImageHandle;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

import java.nio.ByteBuffer;

public class MenuActivity extends RobotActivity implements RobotLifecycleCallbacks {

    Button btnBack, btnHome, btnQueue, btnFAQ, btnLostAndFound, btnGuidance, btnEntertainment;
    // Store the Animate action.
    private Animate animate;
    private QiContext qiContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);
        QiSDK.register(this, this);

        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnQueue = findViewById(R.id.btnQueue);
        btnFAQ = findViewById(R.id.btnFAQ);
        btnLostAndFound = findViewById(R.id.btnLostAndFound);
        btnGuidance = findViewById(R.id.btnGuidance);
        btnEntertainment = findViewById(R.id.btnEntertainment);

        // Set the click listeners for each button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBackActivity();
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMenu(MenuActivity.this);
            }
        });

        btnQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBackActivity();
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBackActivity();
            }
        });

        btnLostAndFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLnFActivity(MenuActivity.this);
            }
        });

        btnGuidance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGuidanceActivity(MenuActivity.this);
            }
        });

        btnEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTTT(MenuActivity.this);
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

        // Create a new say action.
        Say say = SayBuilder.with(qiContext)
                .withText("Let's record it's information, additionally, you can take a picture of the item")
                .build();

        Say sayNext = SayBuilder.with(qiContext)
                .withText("Next Page!")
                .build();

        Say sayPic = SayBuilder.with(qiContext)
                .withText("Hold up the item clearly in front of me!")
                .build();

        Say sayBack = SayBuilder.with(qiContext)
                .withText("Going back")
                .build();


        Say sayBuggin = SayBuilder.with(qiContext)
                .withText("success")
                .build();

        say.run();

        // Create the PhraseSet 2.
        PhraseSet phraseSetMenu = PhraseSetBuilder.with(qiContext)
                .withTexts("Menu")
                .build();

        // Create the PhraseSet 2.
        PhraseSet phraseSetEvents = PhraseSetBuilder.with(qiContext)
                .withTexts("Events")
                .build();

        PhraseSet phraseSetHome = PhraseSetBuilder.with(qiContext)
                .withTexts("Home")
                .build();

        // Create the PhraseSet 2.
        PhraseSet phraseSetBack = PhraseSetBuilder.with(qiContext)
                .withTexts("back", "previous")
                .build();

        // Create a new listen action.
        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSets(phraseSetEvents, phraseSetHome, phraseSetMenu, phraseSetBack)
                .build();

        this.qiContext = qiContext;

        while (true) {
            ListenResult listenResult = listen.run();

            Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetEvents)) {
                Log.i(TAG, "Heard phrase set: Events Page");
                sayNext.run();

                Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                        .withResources(R.raw.bowing_b001) // Set the animation resource.
                        .build(); // Build the animation.

                animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                        .withAnimation(animation) // Set the animation.
                        .build(); // Build the animate action.

                // Run the animate action asynchronously.
                Future<Void> animateFuture = animate.async().run();
                startMenu(MenuActivity.this);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetHome)) {
                Log.i(TAG, "Heard phrase set: Home Page");
                sayBack.run();
                startHome(MenuActivity.this);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetMenu)) {
                Log.i(TAG, "Heard phrase set: Menu Page");
                sayBack.run();
                startMenu(MenuActivity.this);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetBack)) {
                startBackActivity();
            }
        }


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
    private void startTTT(Context context){
        Intent intent = new Intent(context, TicTacToeActivity.class);
        context.startActivity(intent);
    }

    private void startLnFActivity(Context context){
        Intent intent = new Intent(context, LostnFoundMain.class);
        context.startActivity(intent);
    }

    private void startGuidanceActivity(Context context){
        Intent intent = new Intent(context, guidance.class);
        context.startActivity(intent);
    }
    private void startBackActivity() {
        finish();
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