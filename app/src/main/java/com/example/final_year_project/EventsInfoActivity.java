package com.example.final_year_project;

import static android.content.ContentValues.TAG;

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
import com.bumptech.glide.Glide;

public class EventsInfoActivity extends RobotActivity implements RobotLifecycleCallbacks {


    Button Back, btnDirect;
    String event;
    TextView categoryTitle, content;
    ImageView qr;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "EVENT INFO ACTIVITY STARTED 1");
        setContentView(R.layout.activity_events_info);
        event = getIntent().getStringExtra("event");
        String GRAD = getResources().getString(R.string.GRAD);
        String MOMNT = getResources().getString(R.string.MOMNT);
        String RF = getResources().getString(R.string.RF);
        int GRADQR = R.drawable.graduationqr;
        int MOMNTQR = R.drawable.momentumqr;
        int RFQR = R.drawable.reflectionsqr;

        Log.i(TAG, "EVENT INFO ACTIVITY STARTED 2");

        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);
        QiSDK.register(this, this);

        Log.i(TAG, "EVENT INFO ACTIVITY STARTED 3");

        Back = findViewById(R.id.btnBack);
        btnDirect = findViewById(R.id.btnHelp);
        categoryTitle = findViewById(R.id.eventsTitle);
        content = findViewById(R.id.tvContent);
        qr = findViewById(R.id.ivQR);

        Log.i(TAG, "EVENT INFO ACTIVITY STARTED 4");

        if (event != null) {
            if (event.equals("GRAD")) {
                categoryTitle.setText("Graduation Event:");
                content.setText(GRAD);
                Glide.with(this)
                        .load(GRADQR)
                        .into(qr);
                Log.i(TAG, "EVENT INFO ACTIVITY STARTED 4.5");

            } else if (event.equals("MOMNT")) {
                categoryTitle.setText("Momentum Events:");
                content.setText(MOMNT);
                Glide.with(this)
                        .load(GRADQR)
                        .into(qr);
                Log.i(TAG, "EVENT INFO ACTIVITY STARTED 4.5");

            } else if (event.equals("RF")) {
                categoryTitle.setText("Reflections Event:");
                content.setText(RF);
                Glide.with(this)
                        .load(GRADQR)
                        .into(qr);
                Log.i(TAG, "EVENT INFO ACTIVITY STARTED 4.5");
            }
        }

        Log.i(TAG, "EVENT INFO ACTIVITY STARTED 5");
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