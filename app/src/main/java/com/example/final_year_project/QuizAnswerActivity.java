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

public class QuizAnswerActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private List<Integer> askedQuestions, score;
    Button btnNext, btnEnt;
    TextView tvAns, tvExpl;
    ImageView ivAns, ivBack, ivHome;
    String marking;
    int questionNumber;
    private QiContext qiContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_answer);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        Intent intent = getIntent();

        int cwp = R.drawable.cards_welcome_prog;
        int rp = R.drawable.republicpoly;
        int ftc = R.drawable.full_time_courses_sub;

        askedQuestions = intent.getIntegerArrayListExtra("list");
        score = intent.getIntegerArrayListExtra("score");

        questionNumber = questionNo(askedQuestions);
        marking = checkCorrect(score);

        btnNext = findViewById(R.id.btnNext);
        btnEnt = findViewById(R.id.btnEntertainment);
        tvAns = findViewById(R.id.textViewAnswer);
        tvExpl = findViewById(R.id.textViewExplanation);
        ivAns = findViewById(R.id.imageViewResult);
        ivBack = findViewById(R.id.ivBack);
        ivHome = findViewById(R.id.ivHome);


        if (questionNumber == 1) {
            tvAns.setText("2002");
            tvExpl.setText("Republic Polytechnic was established in 2002, and it was at the temporary Tanglin campus");
            ivAns.setImageResource(rp);

        } else if (questionNumber == 2) {
            tvAns.setText("7");
            tvExpl.setText("There are a total of 7 schools in Republic Polytechnic.\n School of Applied Science (SAS), School of Engineering (SEG)\n School of Hospitality (SOH), School of Infocomm (SOI)\n School of Management and Communication (SMC), School of Sports, Health and Leisure (SHL)\n and School of Technology for the Arts (STA)");
            ivAns.setImageResource(ftc);

        } else if (questionNumber == 3) {
            tvAns.setText("43");
            tvExpl.setText("There are a total of 43 courses in Republic Polytechnic");
            ivAns.setImageResource(cwp);

        }

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
                startMenu(QuizAnswerActivity.this);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearViews();
                startQuizActivity(askedQuestions, score);
            }
        });

        btnEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearViews();
                startEntertainmentActivity();

            }
        });




    }

    private void startMenu(Context context){
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }

    private void startBackActivity() {
        finish();
    }

    private void startEntertainmentActivity() {
        Intent intent = new Intent(QuizAnswerActivity.this,EntertainmentActivity.class);
        startActivity(intent);
    }


    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        if (questionNumber == 1) {
            Say qn = SayBuilder.with(qiContext).withText("The answer is 2002," + "Republic Polytechnic was established in 2002, and it was at the temporary Tanglin campus").build();
            qn.run();
        } else if (questionNumber == 2) {
            Say qn = SayBuilder.with(qiContext).withText("The answer is 7" + "There are a total of 7 schools in Republic Polytechnic. School of Applied Science (SAS), School of Engineering (SEG), School of Hospitality (SOH), School of Infocomm (SOI), School of Management and Communication (SMC), School of Sports, Health and Leisure (SHL), and School of Technology for the Arts (STA)").build();
            qn.run();
        } else if (questionNumber == 3) {
            Say qn = SayBuilder.with(qiContext).withText("The answer is 43" + "There are a total of 43 courses in Republic Polytechnic").build();
            qn.run();
        }
        PhraseSet phraseSetNext = PhraseSetBuilder.with(qiContext)
                .withTexts("Next").build();
        PhraseSet phraseSetMenu = PhraseSetBuilder.with(qiContext)
                .withTexts("Menu").build();
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetMenu, phraseSetNext).build();
        this.qiContext = qiContext;
        while (true) {
            ListenResult listenResult = listen.run();
            Log.i(ControlsProviderService.TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetNext)) {
                clearViews();
                startQuizActivity(askedQuestions, score);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetMenu)) {
                startMenu(QuizAnswerActivity.this);
            }
        }
    }

    private String checkCorrect(List<Integer> scoreList) {
        int lastNumber = scoreList.get(scoreList.size() - 1);
        String valid = "";
        if (lastNumber == 1) {
            valid = "Correct!";
        } else if (lastNumber == 0) {
            valid = "Wrong!";
        }

        return valid;

    }


    private void clearViews(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tvAns.setText("");
                tvExpl.setText("");
                ivAns.setImageDrawable(null);
            }
        });
    }

    private int questionNo(List<Integer> askedList) {
        int lastNumber = askedList.get(askedList.size() - 1);
        return  lastNumber;
    }

    private void startQuizActivity(List askedArray, List scoreArray) {
        Intent intent = new Intent(this, QuizQuestionActivity.class);
        intent.putExtra("list", new ArrayList<>(askedArray)); // Create a new ArrayList to avoid issues with serializable
        Log.i(TAG, "Itenting" + askedArray);
        intent.putExtra("score", new ArrayList<>(scoreArray));
        Log.i(TAG, "Score: " + scoreArray);
        startActivity(intent);
    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

}
