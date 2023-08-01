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
import android.widget.ImageButton;
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

        ImageButton imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform the action to open the other page here
                // For example, start a new activity using an Intent
                startWelcome(MainActivity.this);
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
                .withText("Welcome to Republic Polytechnic, I am Pepper and today I will be your guide! Say keywords such as menu, events or guidance to jump directly to the page! You can also say Hello or Touch the screen to continue!")
                .build();

        Say sayWrong = SayBuilder.with(qiContext)
                .withText("Sorry I was not able to understand what you said, Kindly repeat your command or tap the screen to continue.")
                .build();

        Say sayRight = SayBuilder.with(qiContext)
                .withText("Command Received!")
                .build();


        say.run();

        // Create the PhraseSet for "Hello"
        PhraseSet phraseSetHello = PhraseSetBuilder.with(qiContext)
                .withTexts("Hello")
                .build();

        // Create the PhraseSet for "Home"
        PhraseSet phraseSetHome = PhraseSetBuilder.with(qiContext)
                .withTexts("Home")
                .build();

        // Create the PhraseSet for "Menu"
        PhraseSet phraseSetMenu = PhraseSetBuilder.with(qiContext)
                .withTexts("Menu")
                .build();

        // Create the PhraseSet for "Events"
        PhraseSet phraseSetEvents = PhraseSetBuilder.with(qiContext)
                .withTexts("Events")
                .build();

        // Create the PhraseSet for "Back"
        PhraseSet phraseSetBack = PhraseSetBuilder.with(qiContext)
                .withTexts("back", "previous")
                .build();

        // Create the PhraseSet for "Lost"
        PhraseSet phraseSetLost = PhraseSetBuilder.with(qiContext)
                .withTexts("Lost")
                .build();

        // Create the PhraseSet for "Found"
        PhraseSet phraseSetFound = PhraseSetBuilder.with(qiContext)
                .withTexts("Found")
                .build();

        // Create the PhraseSet for "Games", "Tic-Tac-Toe", "Rock-Paper-Scissors"
        PhraseSet phraseSetGames = PhraseSetBuilder.with(qiContext)
                .withTexts("Games", "Tic-Tac-Toe", "Rock-Paper-Scissors")
                .build();

        // Create the PhraseSet for "Quiz"
        PhraseSet phraseSetQuiz = PhraseSetBuilder.with(qiContext)
                .withTexts("Quiz")
                .build();

        // Create the PhraseSet for "FAQ", "Frequently Asked Question", "I have a question", "Question"
        PhraseSet phraseSetFAQ = PhraseSetBuilder.with(qiContext)
                .withTexts("FAQ", "Frequently Asked Question", "I have a question", "Question")
                .build();

        // Create the PhraseSet for "Queue"
        PhraseSet phraseSetQueue = PhraseSetBuilder.with(qiContext)
                .withTexts("Queue")
                .build();

        // Create the PhraseSet for "Financial Assistance", "Finance", "Money Related", "FAS", "Bursary"
        PhraseSet phraseSetFinancialAssistance = PhraseSetBuilder.with(qiContext)
                .withTexts("Financial Assistance", "Finance", "Money Related", "FAS", "Bursary")
                .build();

        // Create the PhraseSet for "Scholarship and Awards", "Awards", "Scholarship"
        PhraseSet phraseSetScholarshipAwards = PhraseSetBuilder.with(qiContext)
                .withTexts("Scholarship and Awards", "Awards", "Scholarship")
                .build();

        // Create the PhraseSet for "Student Care", "Counselling", "Mentoring", "Student Insurance", "Special Education Needs"
        PhraseSet phraseSetStudentCare = PhraseSetBuilder.with(qiContext)
                .withTexts("Student Care", "Counselling", "Mentoring", "Student Insurance", "Special Education Needs")
                .build();

        // Create the PhraseSet for "IT Enhanced Learning Experience", "IT", "Helpdesk"
        PhraseSet phraseSetITEnhancedLearning = PhraseSetBuilder.with(qiContext)
                .withTexts("IT Enhanced Learning Experience", "IT", "Helpdesk")
                .build();

        // Create the PhraseSet for "International Students"
        PhraseSet phraseSetInternationalStudents = PhraseSetBuilder.with(qiContext)
                .withTexts("International Students")
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

    private void startWelcome(Context context) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        context.startActivity(intent);
    }

    private void back() {
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