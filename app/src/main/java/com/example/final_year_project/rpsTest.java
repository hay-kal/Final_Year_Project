package com.example.final_year_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class rpsTest extends AppCompatActivity {

    Button btnRock, btnPaper, btnScissors;
    TextView tvScore;
    ImageView ivComputer, ivHuman;
    int HumanScore, CompScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rps_test);

        btnRock = findViewById(R.id.btn_Rock);
        btnPaper = findViewById(R.id.btn_Paper);
        btnScissors = findViewById(R.id.btn_Scissors);
        tvScore = findViewById(R.id.tv_Score);
        ivComputer = findViewById(R.id.iv_ComChoice);
        ivHuman = findViewById(R.id.iv_HumanChoice);

        btnRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHuman.setImageResource(R.drawable.rock);
                String message = play_turn("rock");
                Toast.makeText(rpsTest.this, message, Toast.LENGTH_SHORT).show();
                tvScore.setText("Score Human: " + Integer.toString(HumanScore) + " Score Computer: " + Integer.toString(CompScore));

            }
        });

        btnPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHuman.setImageResource(R.drawable.paper);
                String message = play_turn("paper");
                Toast.makeText(rpsTest.this, message, Toast.LENGTH_SHORT).show();
                tvScore.setText("Score Human: " + Integer.toString(HumanScore) + " Score Computer: " + Integer.toString(CompScore));
            }
        });

        btnScissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHuman.setImageResource(R.drawable.scissors);
                String message = play_turn("scissors");
                Toast.makeText(rpsTest.this, message, Toast.LENGTH_SHORT).show();
                tvScore.setText("Score Human: " + Integer.toString(HumanScore) + " Score Computer: " + Integer.toString(CompScore));

            }
        });


    }

    public String play_turn(String player_choice) {
        String comp_choice = "";
        Random r = new Random();

        int comp_choice_no = r.nextInt(3) + 1;

        if(comp_choice_no == 1) {
            comp_choice = "rock";
        } else if(comp_choice_no == 2) {
            comp_choice = "paper";
        } else if(comp_choice_no == 3) {
            comp_choice = "scissors";
        }

        if(comp_choice == "rock") {
            ivComputer.setImageResource(R.drawable.rock);
        } else
        if(comp_choice == "paper") {
            ivComputer.setImageResource(R.drawable.paper);
        } else
        if(comp_choice == "scissors") {
            ivComputer.setImageResource(R.drawable.scissors);
        }

        if (comp_choice == player_choice) {
            return "Draw";
        }
        else if (player_choice == "rock" && comp_choice == "scissors") {
            HumanScore++;
            return "You win";
        } else if (player_choice == "rock" && comp_choice == "paper") {
            CompScore++;
            return "You lose!";
        } else if (player_choice == "paper" && comp_choice == "rock") {
            HumanScore++;
            return "You win";
        } else if (player_choice == "paper" && comp_choice == "scissors") {
            CompScore++;
            return "You lose!";
        } else if (player_choice == "scissors" && comp_choice == "rock") {
            CompScore++;
            return "You win";
        } else if (player_choice == "scissors" && comp_choice == "paper") {
            HumanScore++;
            return "You win";
        } else return "Not Sure";
    }
}