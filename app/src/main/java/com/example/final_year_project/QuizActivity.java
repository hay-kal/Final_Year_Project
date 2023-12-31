package com.example.final_year_project;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.controls.ControlsProviderService;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private List<Integer> askedQuestions;
    ImageView ivBack, ivHome;

    TextView tvTitle;
    Button btnStart;
    private QiContext qiContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        btnStart = findViewById(R.id.buttonStart);

        askedQuestions = new ArrayList<>();
        ivBack = findViewById(R.id.ivBack);
        ivHome = findViewById(R.id.ivHome);
        tvTitle = findViewById(R.id.textViewExplanation);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For example, start a new activity using an Intent
                // To be replaced with startEvents after testing
                back();
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMenu(QuizActivity.this);
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuizActivity(askedQuestions);
            }
        });
    }

    private void startQuizActivity(List array) {
        Intent intent = new Intent(QuizActivity.this, QuizQuestionActivity.class);
        intent.putExtra("list", new ArrayList<>(array)); // Create a new ArrayList to avoid issues with serializable
        Log.i(TAG, "Itenting" + array);
        startActivity(intent);
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
    private void startFAQActivity(Context context) {
        Intent intent = new Intent(context, FAQsMenuActivity.class);
        context.startActivity(intent);
    }

    // Method to start the Lost and Found Activity
    private void startLnFActivity(Context context) {
        Intent intent = new Intent(context, LostAndFoundActivity.class);
        context.startActivity(intent);
    }

    // Method to start the Guidance Activity
    private void startGuidanceActivity(Context context) {
        Intent intent = new Intent(context, GuidanceActivity.class);
        context.startActivity(intent);
    }

    // Method to start the Feedback Activity
    private void startFeedbackActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    // Method to finish the current activity and go back to the previous one
    private void back() {
        finish();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Say welcomeQuiz = SayBuilder.with(qiContext).withText("Welcome to the quiz section, now" + tvTitle.getText() + " Say start to begin with the quiz").build();
        PhraseSet phraseSetStart = PhraseSetBuilder.with(qiContext)
                .withTexts("Start").build();
        welcomeQuiz.run();
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetStart).build();
        this.qiContext = qiContext;
        while (true) {
            ListenResult listenResult = listen.run();
            Log.i(ControlsProviderService.TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetStart)) {
                startQuizActivity(askedQuestions);
            }
        }
    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}