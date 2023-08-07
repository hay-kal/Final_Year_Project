package com.example.final_year_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

import java.util.List;

public class QuizResultActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private List<Integer> score;
    Button btnFinish;
    ImageView ivBack, ivHome;

    TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        Intent intent = getIntent();
        score = intent.getIntegerArrayListExtra("score");

        int totalScore = sumList(score);


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

    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}