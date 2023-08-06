package com.example.final_year_project;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

import java.util.List;

public class FeedbackListActivity extends RobotActivity implements RobotLifecycleCallbacks {

    RatingBar rbAverage;

    ListView lvRating;

    Button btnClearAll;

    TextView tvAvg;
    ImageView btnBack, btnHome;

    private QiContext qiContext;

    private List<FeedbackObject.Feedback> rated;

    private CustomListAdapterFeedback ratedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

        lvRating = findViewById(R.id.listViewRating);
//        lvRating.Scrollable = true;
//        lvRating.View = View.Details;
        rbAverage = findViewById(R.id.ratingBarAvg);
        btnBack = findViewById(R.id.ivBack);
        btnHome = findViewById(R.id.ivHome);
        tvAvg = findViewById(R.id.textViewAvgRating);
        btnClearAll = findViewById(R.id.buttonClr);

        QiSDK.register(this, this);

        FeedbackObject ratingList = FeedbackObject.getInstance(this);

        rated = ratingList.getFeedbacks();
        Log.i(TAG, "ListStorage Items Retrieved" + rated);

        ratedAdapter = new CustomListAdapterFeedback(this, R.layout.custom_row_item_feedback, rated);
        lvRating.setAdapter(ratedAdapter);
        Log.i(TAG, "ListView Adapted");

        calculateAndSetAverageRating();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMenu(FeedbackListActivity.this);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBackActivity();
            }
        });

        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackListActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("This action will clear ALL items in the list.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //clear list
                        FeedbackObject.getInstance(FeedbackListActivity.this).clearFeedbacks();
                        // Refresh the adapter to reflect the changes
                        ratedAdapter.clear(); // Clear the adapter data
                        ratedAdapter.notifyDataSetChanged();

                        // Recalculate and set the average rating
                        calculateAndSetAverageRating();

                        String ratingMessage = "Rating list cleared!";
                        Toast.makeText(FeedbackListActivity.this, ratingMessage, Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                        String ratingMessage = "Cancelled";
                        Toast.makeText(FeedbackListActivity.this, ratingMessage, Toast.LENGTH_SHORT).show();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void calculateAndSetAverageRating() {
        float totalRating = 0;
        for (FeedbackObject.Feedback feedback : rated) {
            totalRating += feedback.rating;
        }
        float averageRating = rated.isEmpty() ? 0 : totalRating / rated.size();
        rbAverage.setRating(averageRating);
        tvAvg.setText(String.valueOf(averageRating));
    }

    private void startBackActivity() {
        finish();
    }

    private void startMenu(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
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
}
