package com.example.final_year_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedbackActivity extends RobotActivity implements RobotLifecycleCallbacks {

    Button btnSubmit;

    ImageView ivBack, ivHome;


    EditText etComments;
    RatingBar rbFeedback;

    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        ivBack = findViewById(R.id.ivBack);
        ivHome = findViewById(R.id.ivHome);
        btnSubmit = findViewById(R.id.buttonSubmit);
        rbFeedback = findViewById(R.id.ratingBar);
        etComments = findViewById(R.id.editTextComments);

        rbFeedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating = rbFeedback.getRating();
                String ratingMessage = rating + " Stars";
                Toast.makeText(FeedbackActivity.this, ratingMessage, Toast.LENGTH_SHORT).show();

            }
        });

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
                startMenu(FeedbackActivity.this);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = rbFeedback.getRating();
                String comment = etComments.getText().toString().trim();
                saveFeedback(rating, comment);
                startActivitySubmit(rating);
            }
        });

    }

    private void saveFeedback(float rating, String comment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        FeedbackObject.getInstance(this).addFeedback(rating, comment, currentDate);
    }

    public void startActivityBack() {
        rbFeedback.setRating(0);
        etComments.setText("");
        finish(); // Close the current activity and go back
    }

    public void startActivityHome() {
        rbFeedback.setRating(0);
        etComments.setText("");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void startActivitySubmit(float rating) {
        Intent intent = new Intent(this, FeedbackFinishActivity.class);
        intent.putExtra("rating", rating);
        startActivity(intent);
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        Say sayFeedback = SayBuilder.with(qiContext)
                .withText("Please do leave me a rating on my performance! Either tap or say out the rating from 1 to 5!")
                .build();

        sayFeedback.run();

        PhraseSet phraseSetOne = PhraseSetBuilder.with(qiContext)
                .withTexts("one", "one star", "1", "bad", "worst")
                .build();

        PhraseSet phraseSetTwo = PhraseSetBuilder.with(qiContext)
                .withTexts("two", "two stars", "2", "below average", "not satisfied", "unsatisfied")
                .build();

        PhraseSet phraseSetThree = PhraseSetBuilder.with(qiContext)
                .withTexts("three", "three stars", "3", "ok", "okay", "satisfactory")
                .build();

        PhraseSet phraseSetFour = PhraseSetBuilder.with(qiContext)
                .withTexts("four", "four stars", "4", "good", "great")
                .build();

        PhraseSet phraseSetFive = PhraseSetBuilder.with(qiContext)
                .withTexts("five", " stars", "5", "excellent", "perfect")
                .build();

        PhraseSet phraseSetSubmit = PhraseSetBuilder.with(qiContext)
                .withTexts("Submit", "enter")
                .build();

        PhraseSet phraseSetHome = PhraseSetBuilder.with(qiContext)
                .withTexts("home")
                .build();

        PhraseSet phraseSetBack = PhraseSetBuilder.with(qiContext)
                .withTexts("back")
                .build();

        Listen listenForFeedback = ListenBuilder.with(qiContext)
                .withPhraseSets(phraseSetOne, phraseSetTwo, phraseSetThree, phraseSetFour, phraseSetFive, phraseSetSubmit, phraseSetHome, phraseSetBack)
                .build();

        ListenResult listenResult = listenForFeedback.run();

        PhraseSet matchedPhraseSetFeedback = listenResult.getMatchedPhraseSet();

        while (true) {

            if (PhraseSetUtil.equals(matchedPhraseSetFeedback, phraseSetOne)) {
                rbFeedback.setRating(1);
                Toast.makeText(FeedbackActivity.this, "1 Star", Toast.LENGTH_SHORT).show();

            } else if (PhraseSetUtil.equals(matchedPhraseSetFeedback, phraseSetTwo)) {
                rbFeedback.setRating(2);
                Toast.makeText(FeedbackActivity.this, "2 Star", Toast.LENGTH_SHORT).show();

            } else if (PhraseSetUtil.equals(matchedPhraseSetFeedback, phraseSetThree)) {
                rbFeedback.setRating(3);
                Toast.makeText(FeedbackActivity.this, "3 Star", Toast.LENGTH_SHORT).show();

            } else if (PhraseSetUtil.equals(matchedPhraseSetFeedback, phraseSetFour)) {
                rbFeedback.setRating(4);
                Toast.makeText(FeedbackActivity.this, "4 Star", Toast.LENGTH_SHORT).show();

            } else if (PhraseSetUtil.equals(matchedPhraseSetFeedback, phraseSetFive)) {
                rbFeedback.setRating(5);
                Toast.makeText(FeedbackActivity.this, "5 Star", Toast.LENGTH_SHORT).show();

            } else if (PhraseSetUtil.equals(matchedPhraseSetFeedback, phraseSetSubmit)) {
                startActivitySubmit(rating);


            } else if (PhraseSetUtil.equals(matchedPhraseSetFeedback, phraseSetHome)) {
                startActivityHome();


            } else if (PhraseSetUtil.equals(matchedPhraseSetFeedback, phraseSetBack)) {
                startActivityBack();

            }

        }

    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

    // Method to finish the current activity and go back to the previous one
    private void back() {
        finish();
    }

    // Method to start the MenuActivity
    private void startMenu(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }

}
