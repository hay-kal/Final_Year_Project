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

public class EventsActivity extends RobotActivity implements RobotLifecycleCallbacks {

    ImageView ivBack, ivHome;
    ImageButton ibGrad, ibMomentum, ibReflections;

    // Store the Animate action.
    private Animate animate;
    private QiContext qiContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        ibGrad = findViewById(R.id.ibGrad);
        ibMomentum = findViewById(R.id.ibMomentum);
        ibReflections = findViewById(R.id.ibReflections);
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
                startMenu(EventsActivity.this);
            }
        });

        ibGrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For example, start a new activity using an Intent
                // To be replaced with startEvents after testing
                startInfoActivity("GRAD");
            }
        });

        ibMomentum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For example, start a new activity using an Intent
                // To be replaced with startEvents after testing
                startInfoActivity("MOMNT");
            }
        });

        ibReflections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For example, start a new activity using an Intent
                // To be replaced with startEvents after testing
                startInfoActivity("RF");
            }
        });

    }

    public void startInfoActivity (String event) {
        Intent intent = new Intent(EventsActivity.this, EventsInfoActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        // Create a new say action for "On Start"
        Say sayOnStart = SayBuilder.with(qiContext)
                .withText("Here are the events being hosted today!")
                .build();

        // Create a new say action for "Next Page"
        Say sayNext = SayBuilder.with(qiContext)
                .withText("Next Page!")
                .build();

        // Create a new say action for "Back"
        Say sayBack= SayBuilder.with(qiContext)
                .withText("Going back!")
                .build();

        sayOnStart.async().run();

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

        // Create the PhraseSet for "ItemListActivity"
        PhraseSet phraseSetLost = PhraseSetBuilder.with(qiContext)
                .withTexts("ItemListActivity")
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

            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetHome)) {
                Log.i(TAG, "Heard phrase set: Home Page");
                sayBack.run();
                startHome(EventsActivity.this);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetMenu)) {
                Log.i(TAG, "Heard phrase set: Menu Page");
                sayBack.run();
                startMenu(EventsActivity.this);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetBack)) {
                startBackActivity();
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