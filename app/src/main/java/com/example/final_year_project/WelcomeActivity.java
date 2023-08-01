package com.example.final_year_project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;

public class WelcomeActivity extends RobotActivity implements RobotLifecycleCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button eventsButton = findViewById(R.id.eventsbutton);
        Button menuButton = findViewById(R.id.menubutton);
        Button feedbackButton = findViewById(R.id.feedbackbutton);

        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEventsActivity();
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity();
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedbackActivity();
            }
        });
    }

    public void openEventsActivity() {
        Intent intent = new Intent(this, EventsActivity.class);
        startActivity(intent);
    }

    public void openMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void openFeedbackActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // You can add any code that needs to run when the robot focus is gained.
    }

    @Override
    public void onRobotFocusLost() {
        // You can add any code that needs to run when the robot focus is lost.
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }
}