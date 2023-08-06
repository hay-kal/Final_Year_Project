package com.example.final_year_project;

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

public class EventsInfoActivity extends RobotActivity implements RobotLifecycleCallbacks {


    Button Back, btnDirect;
    String event;
    TextView categoryTitle, content;
    ImageView qr;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_info);
        event = getIntent().getStringExtra("event");
        String GRAD = getResources().getString(R.string.GRAD);
        String MOMNT = getResources().getString(R.string.MOMNT);
        String RF = getResources().getString(R.string.RF);
        int GRADQR = R.drawable.graduationqr;
        int MOMNTQR = R.drawable.momentumqr;
        int RFQR = R.drawable.reflectionsqr;

        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);
        QiSDK.register(this, this);

        Back = findViewById(R.id.btnBack);
        btnDirect = findViewById(R.id.btnHelp);
        categoryTitle = findViewById(R.id.eventsTitle);
        content = findViewById(R.id.tvContent);
        qr = findViewById(R.id.ivQR);


        if (event != null) {
            if (event.equals("GRAD")) {
                categoryTitle.setText("Graduation Event:");
                content.setText(GRAD);
                qr.setImageResource(GRADQR);
            } else if (event.equals("MOMNT")) {
                categoryTitle.setText("Momentum Events:");
                content.setText(MOMNT);
                qr.setImageResource(MOMNTQR);
            } else if (event.equals("RF")) {
                categoryTitle.setText("Reflections Event:");
                content.setText(RF);
                qr.setImageResource(RFQR);
            }
        }
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryTitle.setText("");
                content.setText("");
                qr.setImageDrawable(null);
                startBackActivity();
            }
        });
        btnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryTitle.setText("");
                content.setText("");
                qr.setImageDrawable(null);

                openEventGuidance(event);
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

    private void startBackActivity() {
        finish();
    }

    private void openEventGuidance(String event) {
        Intent intent = new Intent(EventsInfoActivity.this, GuidanceActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }
}