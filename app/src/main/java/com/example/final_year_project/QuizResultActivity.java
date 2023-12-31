package com.example.final_year_project;

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

import java.util.List;

public class QuizResultActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private List<Integer> score;
    Button btnFinish;
    ImageView ivBack, ivHome;
    TextView tvScore;
    private int totalScore;
    private QiContext qiContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        Intent intent = getIntent();
        score = intent.getIntegerArrayListExtra("score");

        totalScore = sumList(score);

        btnFinish = findViewById(R.id.buttonFinish);
        tvScore = findViewById(R.id.textViewScore);
        ivBack = findViewById(R.id.ivBack);
        ivHome = findViewById(R.id.ivHome);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For example, start a new activity using an Intent
                // To be replaced with startEvents after testing
                startEntertainmentActivity();
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMenu(QuizResultActivity.this);
            }
        });

        tvScore.setText("You got " + totalScore + " out of 3 questions correct.");

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startEntertainmentActivity();
            }
        });

    }

    private void startMenu(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }

    private int sumList(List<Integer> list) {
        int sum = 0;
        for (int number : list) {
            sum += number;
        }
        return sum;
    }

    private void startEntertainmentActivity() {
        Intent intent = new Intent(this, EntertainmentActivity.class);
        startActivity(intent);
    }

    // Method to finish the current activity and go back to the previous one
    private void back() {
        Intent intent = new Intent(this, EntertainmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Say total = SayBuilder.with(qiContext).withText("You scored " + totalScore + " out of 3" + "Say Finish to exit the quiz.").build();

        PhraseSet phraseSetFinish = PhraseSetBuilder.with(qiContext)
                .withTexts("Finish").build();
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetFinish).build();
        this.qiContext = qiContext;
        while (true) {
            ListenResult listenResult = listen.run();
            Log.i(ControlsProviderService.TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetFinish)) {
                startEntertainmentActivity();
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