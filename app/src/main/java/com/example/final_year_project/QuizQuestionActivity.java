package com.example.final_year_project;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class QuizQuestionActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private List<Integer> askedQuestions;
    private List<Integer> score;


    int question;
    int randomQuestion;

    TextView tvQuestion;

    ImageView ivQn, ivBack, ivHome;
    Button btnAns1, btnAns2, btnAns3;

    private static final String CORRECT_ANSWER_Q1 = "2002";
    private static final String CORRECT_ANSWER_Q2 = "7";
    private static final String CORRECT_ANSWER_Q3 = "43";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        int cwp = R.drawable.cards_welcome_prog;
        int rp = R.drawable.republicpoly;
        int ftc = R.drawable.full_time_courses_sub;

        Intent intent = getIntent();

        askedQuestions = intent.getIntegerArrayListExtra("list");
        score = intent.getIntegerArrayListExtra("score");
        Log.i(TAG, "Score: " + score);

        if (score == null) {
            score = new ArrayList<>();
        }

        tvQuestion = findViewById(R.id.questionTextView);
        btnAns2 = findViewById(R.id.answerButton2);
        btnAns1 = findViewById(R.id.answerButton1);
        btnAns3 = findViewById(R.id.answerButton3);
        ivQn = findViewById(R.id.ivQN);
        question = checkAndAddQuestion();
        Log.i(TAG, "Set question: " + question);

        ivBack = findViewById(R.id.ivBack);
        ivHome = findViewById(R.id.ivHome);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For example, start a new activity using an Intent
                // To be replaced with startEvents after testing
            startEntertainmentActivity(QuizQuestionActivity.this);
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMenu(QuizQuestionActivity.this);
            }
        });




        if (question == 1) {
            tvQuestion.setText("When was Republic Polytechnic founded?");
            ivQn.setImageResource(rp);
            //randomizer, then set text for each of the 3 options
            setRandomizedAnswerOptions(1);

        } else if (question == 2) {
            tvQuestion.setText("How many Schools are there in Republic Polytechnic?");
            ivQn.setImageResource(ftc);
            //randomizer, then set text for each of the 3 options
            setRandomizedAnswerOptions(2);

        } else if (question == 3) {
            tvQuestion.setText("How many Full Time Courses are there in Republic Polytechnic?");
            ivQn.setImageResource(cwp);
            //randomizer, then set text for each of the 3 options
            setRandomizedAnswerOptions(3);

        }





        btnAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //method to check for ans
                checkAnswer(question, btnAns1.getText().toString());
                clearAllViews();
                startAnswerActivity(askedQuestions, score);
            }
        });


        btnAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //method to check for ans
                checkAnswer(question, btnAns2.getText().toString());
                clearAllViews();
                startAnswerActivity(askedQuestions, score);
            }
        });

        btnAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //method to check for ans
                checkAnswer(question, btnAns3.getText().toString());
                clearAllViews();
                startAnswerActivity(askedQuestions, score);
            }
        });


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

    private void startAnswerActivity(List askedArray, List scoreArray) {
        Intent intent = new Intent(QuizQuestionActivity.this, QuizAnswerActivity.class);
        intent.putExtra("list", new ArrayList<>(askedArray)); // Create a new ArrayList to avoid issues with serializable
        Log.i(TAG, "Asked list: " + askedArray);
        intent.putExtra("score", new ArrayList<>(scoreArray));
        Log.i(TAG, "Score: " + scoreArray);
        startActivity(intent);
    }

    private void clearAllViews() {
        tvQuestion.setText(null);
        btnAns1.setText(null);
        btnAns2.setText(null);
        btnAns3.setText(null);
    }

    private void startResultActivity(List<Integer> scoreArray) {
        Intent intent = new Intent(QuizQuestionActivity.this, QuizResultActivity.class);
        intent.putExtra("score", new ArrayList<>(scoreArray));
        startActivity(intent);
    }

    public int checkAndAddQuestion() {
        // Check if the list is empty
        if (askedQuestions.isEmpty()) {
            // Generate a random number between 1 and 3 (inclusive)
            randomQuestion = new Random().nextInt(3) + 1;

            Log.i(TAG, "Generated: " + randomQuestion);
            askedQuestions.add(randomQuestion);
        } else {
            // Create a set containing 1, 2, and 3
            Set<Integer> allQuestions = new HashSet<>();
            allQuestions.add(1);
            allQuestions.add(2);
            allQuestions.add(3);

            // Create a set from the askedQuestions list
            Set<Integer> askedSet = new HashSet<>(askedQuestions);

            // Check if askedSet contains all three numbers
            if (askedSet.containsAll(allQuestions)) {
                // Proceed to the next page
                // Call the method to go to the next page here
                Log.i(TAG, "SCORE: " + score);
                startResultActivity(score);
            } else {

                // Find the missing number (not asked yet)

                randomQuestion = new Random().nextInt(3) + 1;
                Log.i(TAG, "Generated: " + randomQuestion);
                Log.i(TAG, "Asked Set: " + askedSet);

                while (askedSet.contains(randomQuestion)) {
                    randomQuestion = new Random().nextInt(3) + 1;
                    Log.i(TAG, "Generated: " + randomQuestion);
                    Log.i(TAG, "Asked Set: " + askedSet);
                }

                askedQuestions.add(randomQuestion);


            }
        }

        return randomQuestion;
    }

    private void setRandomizedAnswerOptions(int question) {
        List<String> answerOptions = new ArrayList<>();

        // Add all possible answer options for each question

        //The following are the text in buttons, option a b c
        if (question == 1) {
            answerOptions.addAll(Arrays.asList("2001", "2002", "2003"));
        } else if (question == 2) {
            answerOptions.addAll(Arrays.asList("6", "7", "8"));
        } else if (question == 3) {
            answerOptions.addAll(Arrays.asList("43", "40", "39"));
        }

        // Shuffle the answer options to randomize their order
        Collections.shuffle(answerOptions);

        // Set the text of each button with the randomized answer options
        btnAns1.setText(answerOptions.get(0));
        btnAns2.setText(answerOptions.get(1));
        btnAns3.setText(answerOptions.get(2));
    }

    private void checkAnswer(int questionNumber, String selectedAnswer) {
        String correctAnswer = getCorrectAnswer(questionNumber);

        if (selectedAnswer.equals(correctAnswer)) {
            // Correct answer
            score.add(1); // Add 1 to the score list
        } else {
            // Incorrect answer
            score.add(0); // Add 0 to the score list
        }

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

    // Method to finish the current activity and go back to the previous one
    private void back() {
        finish();
    }

    private String getCorrectAnswer(int questionNumber) {
        switch (questionNumber) {
            case 1:
                return CORRECT_ANSWER_Q1;
            case 2:
                return CORRECT_ANSWER_Q2;
            case 3:
                return CORRECT_ANSWER_Q3;
            default:
                return "";
        }
    }


}
