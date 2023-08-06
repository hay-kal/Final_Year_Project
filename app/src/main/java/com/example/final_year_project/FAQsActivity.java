package com.example.final_year_project;

import static android.service.controls.ControlsProviderService.TAG;

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

public class FAQsActivity extends RobotActivity implements RobotLifecycleCallbacks {


    Button Back, FrontDesk;
    String category;
    TextView categoryTitle, content;
    ImageView qr;
    private QiContext qiContext;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        category = getIntent().getStringExtra("category");
        String SNA = getResources().getString(R.string.SNA);
        String FA = getResources().getString(R.string.FA);
        String SC = getResources().getString(R.string.SC);
        String IT = getResources().getString(R.string.IT);
        String IS = getResources().getString(R.string.IS);
        int SNAQR = R.drawable.sna;
        int FAQR = R.drawable.fa;
        int SCQR = R.drawable.sc;
        int ISQR = R.drawable.is;
        int ITQR = R.drawable.it;
        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);
        QiSDK.register(this, this);

        Back = findViewById(R.id.btnBack);
        FrontDesk = findViewById(R.id.btnHelp);
        categoryTitle = findViewById(R.id.faqTitle);
        content = findViewById(R.id.tvContent);
        qr = findViewById(R.id.ivQR);


        if (category != null) {
            if (category.equals("SNA")) {
                categoryTitle.setText("Scholarships and Awards:");
                content.setText(SNA);
                qr.setImageResource(SNAQR);
            } else if (category.equals("FA")) {
                categoryTitle.setText("Financial Assistance:");
                content.setText(FA);
                qr.setImageResource(FAQR);
            } else if (category.equals("IS")) {
                categoryTitle.setText("International Students:");
                content.setText(IS);
                qr.setImageResource(ISQR);
            } else if (category.equals("SC")) {
                categoryTitle.setText("Student Care:");
                content.setText(SC);
                qr.setImageResource(SCQR);
            } else if (category.equals("IT")) {
                categoryTitle.setText("IT enhanced Learning Experience:");
                content.setText(IT);
                qr.setImageResource(ITQR);
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
        FrontDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryTitle.setText("");
                content.setText("");
                qr.setImageDrawable(null);
                openQueueSelect(category);
            }
        });
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Say welcomeFAQs = SayBuilder.with(qiContext).withText("You have selected" + category + "Please scan the QR Code to find out more").build();
        Log.i(TAG, "The category is " + category);
        // Create the PhraseSet for "Home"
         PhraseSet phraseSetFrontDesk = PhraseSetBuilder.with(qiContext)
                .withTexts("FrontDesk").build();
        PhraseSet phraseSetBack = PhraseSetBuilder.with(qiContext)
                .withTexts("Back").build();
        welcomeFAQs.run();
        // Create a new listen action.
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetBack, phraseSetFrontDesk).build();
        this.qiContext = qiContext;
        while (true) {
            ListenResult listenResult = listen.run();
            Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
             PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetFrontDesk)) {
                categoryTitle.setText("");
                content.setText("");
                qr.setImageDrawable(null);
                openQueueSelect(category);
            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetBack)) {
                categoryTitle.setText("");
                content.setText("");
                qr.setImageDrawable(null);
                startBackActivity();
            }
        }
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

    private void openQueueSelect(String category) {
        Intent intent = new Intent(FAQsActivity.this, QueueDisplayActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}