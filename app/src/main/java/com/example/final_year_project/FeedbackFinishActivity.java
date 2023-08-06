package com.example.final_year_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
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
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;

public class FeedbackFinishActivity extends RobotActivity implements RobotLifecycleCallbacks {

    Button btnDone, btnViewList;

    private Animate animate;

    private QiContext qiContext;
    TextView tvResponse;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_finish);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);
        QiSDK.register(this, this);

        rating = getIntent().getFloatExtra("rating", -1);

        tvResponse = findViewById(R.id.tvResponse);
        btnDone = findViewById(R.id.buttonFinish);
        btnViewList = findViewById(R.id.buttonViewList);

        if (rating != -1) {
            if (rating < 3) {
                tvResponse.setText("I will try to do better..!");
            } else if (rating == 5) {
                tvResponse.setText("A perfect score? Wow!");
            } else {
                tvResponse.setText("I hope you enjoyed your experience!");
            }
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityHome();
            }
        });

        btnViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityList();
            }
        });
    }

    private void startActivityList() {
        Intent intent = new Intent(this, FeedbackListActivity.class);
        startActivity(intent);
    }

    public void startActivityHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }



    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        Say sayNegative = SayBuilder.with(qiContext)
                .withText("I will try to do better..!")
                .build();

        Say sayNeutral = SayBuilder.with(qiContext)
                .withText("I hope you enjoyed your experience!")
                .build();
        Say sayPositive = SayBuilder.with(qiContext)
                .withText("A perfect score? Wow, thanks!")
                .build();

        this.qiContext = qiContext;

        if (rating != -1) {
            if (rating < 3) {
                sayNegative.async().run();

                Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                        .withResources(R.raw.sad_a001) // Set the animation resource.
                        .build(); // Build the animation.

                animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                        .withAnimation(animation) // Set the animation.
                        .build(); // Build the animate action.

                // Run the animate action asynchronously.
                animate.async().run();

            } else if (rating == 5) {
                sayPositive.async().run();

                Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                        .withResources(R.raw.both_hands_low_b001) // Set the animation resource.
                        .build(); // Build the animation.

                animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                        .withAnimation(animation) // Set the animation.
                        .build(); // Build the animate action.

                // Run the animate action asynchronously.
                animate.async().run();

            } else {


                sayNeutral.async().run();

                Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                        .withResources(R.raw.bowing_b001) // Set the animation resource.
                        .build(); // Build the animation.

                animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                        .withAnimation(animation) // Set the animation.
                        .build(); // Build the animate action.

                // Run the animate action asynchronously.
                animate.async().run();
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
