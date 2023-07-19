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

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {


    // Store the Animate action.
    private Animate animate;
    private QiContext qiContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

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
        Say say = SayBuilder.with(qiContext)
                .withText("Welcome to Republic Polytechnic, I am Pepper. Say hello or touch the screen to begin.")
                .build();

        Say sayWrong = SayBuilder.with(qiContext)
                .withText("Sorry I was not able to understand what you said, Kindly repeat your command or tap the screen to continue.")
                .build();

        Say sayRight = SayBuilder.with(qiContext)
                .withText("Command Received!")
                .build();


        say.run();

        // Create the PhraseSet 1.
        PhraseSet phraseSetHello = PhraseSetBuilder.with(qiContext)
                .withTexts("Hello")
                .build();
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
                .withPhraseSets(phraseSetEvents, phraseSetHome, phraseSetMenu, phraseSetBack, phraseSetHello)
                .build();


        this.qiContext = qiContext;

        while (true) {
            ListenResult listenResult = listen.run();


            Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetHello)) {
                Log.i(TAG, "Heard phrase set: It seems you have LOST an item");
                sayRight.run();
                startEvents(MainActivity.this);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetEvents)) {
                Log.i(TAG, "Heard phrase set: Home Page");
                sayRight.run();
                startEvents(MainActivity.this);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetMenu)) {
                Log.i(TAG, "Heard phrase set: Home Page");
                sayRight.run();
                startMenu(MainActivity.this);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetHome)) {
                Log.i(TAG, "Heard phrase set: Home Page");
                sayRight.run();
                startHome(MainActivity.this);
            }
        }
    }

    private void startMenu(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }
    private void startHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void startEvents(Context context) {
        Intent intent = new Intent(context, EventsActivity.class);
        context.startActivity(intent);
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