package com.example.final_year_project;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

public class FAQsMenuActivity extends RobotActivity implements RobotLifecycleCallbacks {
    Button SNA, FA, SC, IT, IS, Others;
    ImageView ivBack, ivHome;
    private QiContext qiContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_menu);

        setSpeechBarDisplayStrategy((SpeechBarDisplayStrategy.IMMERSIVE));
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        SNA = findViewById(R.id.btnSNA1);
        FA = findViewById(R.id.btnFA1);
        SC = findViewById(R.id.btnSC1);
        IT = findViewById(R.id.btnIT1);
        IS = findViewById(R.id.btnIS1);
        Others = findViewById(R.id.btnOthers1);
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
                startMenu(FAQsMenuActivity.this);
            }
        });

        SNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("SNA");
            }
        });
        FA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("FA");
            }
        });
        IS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("IS");
            }
        });
        SC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("SC");
            }
        });
        IT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("IT");
            }
        });
        Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                straight2Queue("Others");
            }
        });
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // Create the PhraseSet 1.
        PhraseSet phraseSetSNA = PhraseSetBuilder.with(qiContext)
                .withTexts("Scholarship and Awards")
                .build();

        // Create the PhraseSet 2.
        PhraseSet phraseSetFA = PhraseSetBuilder.with(qiContext)
                .withTexts("Financial Assistance")
                .build();


        PhraseSet phraseSetSC = PhraseSetBuilder.with(qiContext)
                .withTexts("Student Care")
                .build();

        // Create the PhraseSet 4.
        PhraseSet phraseSetIS = PhraseSetBuilder.with(qiContext)
                .withTexts("International Students")
                .build();

        // Create the PhraseSet 5.
        PhraseSet phraseSetIT = PhraseSetBuilder.with(qiContext)
                .withTexts("IT Enhanced Learning Experience")
                .build();


        PhraseSet phraseSetOthers = PhraseSetBuilder.with(qiContext)
                .withTexts("Others")
                .build();


        // Create a new listen action.
        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSets(phraseSetSNA, phraseSetFA, phraseSetSC, phraseSetIS, phraseSetIT, phraseSetOthers)
                .build();

        this.qiContext = qiContext;

        while (true) {
            ListenResult listenResult = listen.run();

            Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();

            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetSNA)) {
                openQueueSelect("SNA");
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetFA)) {
                openQueueSelect("FA");
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetSC)) {
                openQueueSelect("SC");
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetFA)) {
                openQueueSelect("FA");
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetIS)) {
                openQueueSelect("IS");
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetIT)) {
                openQueueSelect("IT");
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetOthers)) {
                openQueueSelect("Others");
            }
        }
    }

    @Override
    public void onRobotFocusLost() {
    }

    @Override
    public void onRobotFocusRefused(String reason) {
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

    private void startFeedback(Context context){
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }
    private void startTTT(Context context){
        Intent intent = new Intent(context, TicTacToeActivity.class);
        context.startActivity(intent);
    }

    private void startEntertainmentActivity(Context context){
        Intent intent = new Intent(context, EntertainmentActivity.class);
        context.startActivity(intent);
    }

    private void startLnFActivity(Context context){
        Intent intent = new Intent(context, LostAndFoundActivity.class);
        context.startActivity(intent);
    }

    private void startGuidanceActivity(Context context){
        Intent intent = new Intent(context, GuidanceActivity.class);
        context.startActivity(intent);
    }

    private void startFAQActivity(Context context){
        Intent intent = new Intent(context, FAQsMenuActivity.class);
        context.startActivity(intent);
    }

    private void startBackActivity() {
        finish();
    }

    private void openQueueSelect(String category) {
        Intent intent = new Intent(FAQsMenuActivity.this, FAQsActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void straight2Queue(String category) {
        Intent intent = new Intent(FAQsMenuActivity.this, QueueDisplayActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}