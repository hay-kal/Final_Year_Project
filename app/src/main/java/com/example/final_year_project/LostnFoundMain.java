package com.example.final_year_project;

import android.content.Intent;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

public class LostnFoundMain extends RobotActivity implements RobotLifecycleCallbacks {

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

    Button Found,Lost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostnfound_layout);
        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        Found = findViewById(R.id.btnFound);
        Lost= findViewById(R.id.btnLost);

        Found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startFoundActivity();
            }
        });
        Lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLostActivity();
            }
        });

    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    private void startCameraPreview() {
        // Code to start the camera preview on a SurfaceView or TextureView
        // You can use the Camera API or Camera2 API for this part
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Future<TakePicture> takePictureFuture = TakePictureBuilder.with(qiContext).buildAsync();

        // Create a new say action.
        Say say = SayBuilder.with(qiContext)
                .withText("Hello human, did you lose an item, or, find and item?")
                .build();

        Say sayFound = SayBuilder.with(qiContext)
                .withText("It seems that you have found an item")
                .build();
        Say sayLost = SayBuilder.with(qiContext)
                .withText("It seems that you have lost an item")
                .build();

        Say sayBuggin = SayBuilder.with(qiContext)
                .withText("success")
                .build();

        say.run();

        // Create the PhraseSet 1.
        PhraseSet phraseSetLost = PhraseSetBuilder.with(qiContext)
                .withTexts("lost")
                .build();

        // Create the PhraseSet 2.
        PhraseSet phraseSetFound = PhraseSetBuilder.with(qiContext)
                .withTexts("found")
                .build();


        PhraseSet phraseSetSkip = PhraseSetBuilder.with(qiContext)
                .withTexts("Skip")
                .build();


        // Create a new listen action.
        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSets(phraseSetLost, phraseSetFound, phraseSetSkip)
                .build();

        this.qiContext = qiContext;

        while (true) {
            ListenResult listenResult = listen.run();

            Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetLost)) {
                Log.i(TAG, "Heard phrase set: It seems you have LOST an item");
                sayLost.run();

                Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                        .withResources(R.raw.bowing_b001) // Set the animation resource.
                        .build(); // Build the animation.

                animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                        .withAnimation(animation) // Set the animation.
                        .build(); // Build the animate action.

                // Run the animate action asynchronously.
                Future<Void> animateFuture = animate.async().run();
                Log.i(TAG, "lost bowed");
                startLostActivity();
                Log.i(TAG, "starting activity");
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetFound)) {
                Log.i(TAG, "Heard phrase set: It seems you have FOUND an item");
                sayFound.run();
                startFoundActivity();

            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetSkip)) {
                Log.i(TAG, "Heard phrase set: skipping page");
                sayFound.run();
                startPicActivity();
            }
        }
    }

    private void startFoundActivity() {
        Intent intent = new Intent(LostnFoundMain.this, FoundItemDesc.class);
        startActivity(intent);
    }

    private void startLostActivity() {
        Intent intent = new Intent(LostnFoundMain.this, Lost.class);
        startActivity(intent);
    }

    private void startPicActivity() {
        Intent intent = new Intent(LostnFoundMain.this, FoundItemPicture.class);
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