package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

public class Queue extends RobotActivity implements RobotLifecycleCallbacks {

    Button Back;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue);
        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        QiSDK.register(this, this);

        Back = findViewById(R.id.btnBack);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startBackActivity();
            }
        });

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

    }

    @Override
    public void onRobotFocusLost() {

    }

    private void startBackActivity() {
        Intent intent = new Intent(Queue.this, Lost.class);
        startActivity(intent);
    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}
