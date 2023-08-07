package com.example.final_year_project;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

import java.nio.ByteBuffer;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    // Variables to store QiContext and Animate action
    private QiContext qiContext;
    private Animate animate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the speech bar display strategy and position
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        // Registering the robot lifecycle callbacks to this activity
        QiSDK.register(this, this);

        // Setting up the click listener for the ImageButton
        ImageButton imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On button click, start the events activity
                startEvents(MainActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        // Unregistering the robot lifecycle callbacks for this activity
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // Save the QiContext
        this.qiContext = qiContext;

        // Create a new Say action to greet the user
        Say say = SayBuilder.with(qiContext)
                .withText("Welcome to Republic Polytechnic, I am Pepper and today I will be your guide! " +
                        "If you need help with a specific issues say it out loud and if it is in my database I will send you to the page! " +
                        "You can also say Hello or touch the screen to continue!")
                .build();

        // Create a new Say action for the "Command Received!" confirmation
        Say sayRight = SayBuilder.with(qiContext)
                .withText("Command Received!")
                .build();

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

        // Create the PhraseSet for "ItemListActivity"
        PhraseSet phraseSetLost = PhraseSetBuilder.with(qiContext)
                .withTexts("Lost", "Misplaced")
                .build();

        // Create the PhraseSet for "Found"
        PhraseSet phraseSetFound = PhraseSetBuilder.with(qiContext)
                .withTexts("Found")
                .build();

        // Create the PhraseSet for "Games", "Tic-Tac-Toe", "Rock-Paper-Scissors"
        PhraseSet phraseSetEntertainment = PhraseSetBuilder.with(qiContext)
                .withTexts("Games", "Tic-Tac-Toe", "Rock-Paper-Scissors", "Quiz", "Entertain", "Entertainment")
                .build();

        // Create the PhraseSet for "FAQ", "Frequently Asked Question", "I have a question", "Question"
        PhraseSet phraseSetFAQ = PhraseSetBuilder.with(qiContext)
                .withTexts("FAQ", "Frequently Asked Question", "I have a question", "Question" )
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

        // Create the PhraseSet for "Guidance"
        PhraseSet phraseSetGuidance = PhraseSetBuilder.with(qiContext)
                .withTexts("Guidance", "Map", "How do I get to", "Where is")
                .build();


        // Create a new Listen action with all the PhraseSets
        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSets(phraseSetEvents, phraseSetHome, phraseSetMenu, phraseSetBack, phraseSetHello,
                        phraseSetLost, phraseSetFound, phraseSetEntertainment, phraseSetFAQ,
                        phraseSetQueue, phraseSetFinancialAssistance, phraseSetScholarshipAwards,
                        phraseSetStudentCare, phraseSetITEnhancedLearning, phraseSetInternationalStudents, phraseSetGuidance)
                .build();

        // Run the Say action to greet the user
        say.async().run()
                .andThenCompose(ignored -> {
                    // Say action completed, now start the Listen action asynchronously
                    return listen.async().run();
                })
                .andThenConsume(listenResult -> {
                    // Handle the result of the Listen action here
                    Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
                    // Identify the matched phrase set.
                    PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
                    if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetHello)) {
                        Log.i(TAG, "Heard phrase set: Hello");
                        // Confirm the command received
                        sayRight.run();
                        // Start the EventsActivity
                        startEvents(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetEvents)) {
                        Log.i(TAG, "Heard phrase set: Events Page");
                        sayRight.run();
                        startEvents(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetMenu)) {
                        Log.i(TAG, "Heard phrase set: Menu Page");
                        sayRight.run();
                        startMenu(MainActivity.this);
                    }  else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetHome)) {
                        Log.i(TAG, "Heard phrase set: Home Page");
                        sayRight.run();
                        startHome(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetEntertainment)) {
                        Log.i(TAG, "Heard phrase set: Entertainment Page");
                        sayRight.run();
                        startEntertainmentActivity(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetFAQ)) {
                        Log.i(TAG, "Heard phrase set: FAQ Page");
                        sayRight.run();
                        startFAQActivity(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetFinancialAssistance)) {
                        Log.i(TAG, "Heard phrase set: Financial Assistance Page");
                        sayRight.run();
                        startFAQActivity(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetInternationalStudents)) {
                        Log.i(TAG, "Heard phrase set: International Student Page");
                        sayRight.run();
                        startFAQActivity(MainActivity.this);
                    }  else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetITEnhancedLearning)) {
                        Log.i(TAG, "Heard phrase set: IT Enhanced Learning Page");
                        sayRight.run();
                        startFAQActivity(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetScholarshipAwards)) {
                        Log.i(TAG, "Heard phrase set: ScholarshipAward Page");
                        sayRight.run();
                        startFAQActivity(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetStudentCare)) {
                        Log.i(TAG, "Heard phrase set: Student Care Page");
                        sayRight.run();
                        startFAQActivity(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetLost)) {
                        Log.i(TAG, "Heard phrase set: Lost Page");
                        sayRight.run();
                        startLnFActivity(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetFound)) {
                        Log.i(TAG, "Heard phrase set: Found Page");
                        sayRight.run();
                        startLnFActivity(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetGuidance)) {
                        Log.i(TAG, "Heard phrase set: Guidance Page");
                        sayRight.run();
                        startGuidanceActivity(MainActivity.this);
                    } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetBack)) {
                        Log.i(TAG, "Heard phrase set: Back");
                        sayRight.run();
                        // Finish the current activity to go back to the previous one
                        back();
                    }
                });

    }

    // Method to start the MenuActivity
    private void startMenu(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }

    // Method to start the MainActivity (Home)
    private void startHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    // Method to start the EventsActivity
    private void startEvents(Context context) {
        Intent intent = new Intent(context, EventsActivity.class);
        context.startActivity(intent);
    }

    // Method to start the EntertainmentActivity
    private void startEntertainmentActivity(Context context) {
        Intent intent = new Intent(context, EntertainmentActivity.class);
        context.startActivity(intent);
    }

    // Method to start the FAQsMenuActivity
    private void startFAQActivity(Context context){
        Intent intent = new Intent(context, FAQsMenuActivity.class);
        context.startActivity(intent);
    }

   // Method to start the Lost and Found Activity
    private void startLnFActivity(Context context){
        Intent intent = new Intent(context, LostAndFoundActivity.class);
        context.startActivity(intent);
    }

    // Method to start the Guidance Activity
    private void startGuidanceActivity(Context context){
        Intent intent = new Intent(context, GuidanceActivity.class);
        context.startActivity(intent);
    }

    // Method to start the Feedback Activity
    private void startFeedbackActivity(Context context){
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    // Method to start the Feedback Activity
    private void startQuizActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    // Method to finish the current activity and go back to the previous one
    private void back() {
        finish();
    }

    @Override
    public void onRobotFocusLost() {
        // Reset the QiContext when robot focus is lost
        this.qiContext = null;
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused. Handle the refusal if needed.
    }
}