package com.example.final_year_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

import java.util.Random;

public class RPSActivity extends RobotActivity implements RobotLifecycleCallbacks {

    ImageView ivRock, ivPaper, ivScissors;
    TextView tvHumanScore, tvCompScore;
    ImageView ivComputer, ivHuman, ivBack, ivHome;
    int HumanScore, CompScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpsactivity);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        ivRock = findViewById(R.id.ivRock);
        ivPaper = findViewById(R.id.ivPaper);
        ivScissors = findViewById(R.id.ivScissors);
        tvCompScore = findViewById(R.id.tvCompScore);
        tvHumanScore = findViewById(R.id.tvHumanScore);
        ivComputer = findViewById(R.id.iv_CompChoice);
        ivHuman = findViewById(R.id.iv_HumanChoice);
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
                startMenu(RPSActivity.this);
            }
        });


        ivRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHuman.setImageResource(R.drawable.rock);
                String message = play_turn("rock");
                Toast.makeText(RPSActivity.this, message, Toast.LENGTH_SHORT).show();
                updateScoreDisplay();
            }
        });

        ivPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHuman.setImageResource(R.drawable.paper);
                String message = play_turn("paper");
                Toast.makeText(RPSActivity.this, message, Toast.LENGTH_SHORT).show();
                updateScoreDisplay();
            }
        });

        ivScissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHuman.setImageResource(R.drawable.scissors);
                String message = play_turn("scissors");
                Toast.makeText(RPSActivity.this, message, Toast.LENGTH_SHORT).show();
                updateScoreDisplay();
            }
        });

        // Initialize the score display
        updateScoreDisplay();
    }

    public String play_turn(String player_choice) {
        String comp_choice = "";
        Random r = new Random();

        int comp_choice_no = r.nextInt(3) + 1;

        if (comp_choice_no == 1) {
            comp_choice = "rock";
        } else if (comp_choice_no == 2) {
            comp_choice = "paper";
        } else if (comp_choice_no == 3) {
            comp_choice = "scissors";
        }

        if (comp_choice.equals("rock")) {
            ivComputer.setImageResource(R.drawable.rock);
        } else if (comp_choice.equals("paper")) {
            ivComputer.setImageResource(R.drawable.paper);
        } else if (comp_choice.equals("scissors")) {
            ivComputer.setImageResource(R.drawable.scissors);
        }

        if (comp_choice.equals(player_choice)) {
            return "Draw";
        } else if (player_choice.equals("rock") && comp_choice.equals("scissors")) {
            HumanScore++;
            return "You win";
        } else if (player_choice.equals("rock") && comp_choice.equals("paper")) {
            CompScore++;
            return "You lose!";
        } else if (player_choice.equals("paper") && comp_choice.equals("rock")) {
            HumanScore++;
            return "You win";
        } else if (player_choice.equals("paper") && comp_choice.equals("scissors")) {
            CompScore++;
            return "You lose!";
        } else if (player_choice.equals("scissors") && comp_choice.equals("rock")) {
            CompScore++;
            return "You win";
        } else if (player_choice.equals("scissors") && comp_choice.equals("paper")) {
            HumanScore++;
            return "You win";
        } else {
            return "Not Sure";
        }
    }

    private void updateScoreDisplay() {
        tvHumanScore.setText(String.valueOf(HumanScore));
        tvCompScore.setText(String.valueOf(CompScore));
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
