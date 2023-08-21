package com.example.final_year_project;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

import java.util.ArrayList;

public class GuidanceActivity extends RobotActivity implements RobotLifecycleCallbacks {

    ImageView ivBack, ivHome;

    private ListView lvMap;

    private ImageView mapView;

    EditText guideSearch;

    TextView plcName;

    String event, setString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);
        QiSDK.register(this, this);

        event = getIntent().getStringExtra("event");

        lvMap = findViewById(R.id.areaList);
        mapView = findViewById(R.id.mapView);
        plcName = findViewById(R.id.placeName);
        guideSearch = findViewById(R.id.etPlaceSearch);
        ivBack = findViewById(R.id.ivBack);
        ivHome = findViewById(R.id.ivHome);

        ArrayList<String> locations = new ArrayList<>();

        //Populate the list w/ locations
        locations.add("W1 (SEG)");
        locations.add("W2 (SEG)");
        locations.add("W3 (SAS)");
        locations.add("W4 (SOH)");
        locations.add("W5 (SAS)");
        locations.add("W6 (SOI)");
        locations.add("E1 (CED)");
        locations.add("E2 (STA)");
        locations.add("E3 (SMC)");
        locations.add("E4 (SMC)");
        locations.add("E5 (SHL)");
        locations.add("E6 (SOI)");
        locations.add("South Food Court");
        locations.add("North Food Court");
        locations.add("Sports Hall");
        locations.add("Singapore Institute of Technology (SIT)");
        locations.add("Republic Polytechnic Industry Centre (RPIC)");
        locations.add("The Republic Cultural Centre (TRCC)");
        locations.add("Agora Halls");

        if (event != null ) {

            if (event.equals("GRAD")) {
                setString = "The Republic Cultural Centre (TRCC)";
            } else if (event.equals("MOMNT")) {
                setString = "The Republic Cultural Centre (TRCC)";
            } else if (event.equals("RF")) {
                setString = "Agora Halls";
            }

            plcName.setText(setString);
            // Get the resource identifier for the drawable based on the selected location
            int resourceId = getResourceIdForDrawable(setString);
            if (resourceId != 0) {
                // Set the image drawable for the mapView
                mapView.setImageResource(resourceId);
            }

        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For example, start a new activity using an Intent
                // To be replaced with startEvents after testing
                startMenu(GuidanceActivity.this);
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMenu(GuidanceActivity.this);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
        lvMap.setAdapter(adapter);

        lvMap.setOnItemClickListener((parent, view, position, id) -> {
            // Retrieve the selected ListItem

            String selectedLocation = (String) parent.getItemAtPosition(position);
            plcName.setText(selectedLocation);
            // Get the resource identifier for the drawable based on the selected location
            int resourceId = getResourceIdForDrawable(selectedLocation);
            if (resourceId != 0) {
                // Set the image drawable for the mapView
                mapView.setImageResource(resourceId);
            }

        });

        guideSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Perform the search with the current text in etSearch
                performSearch(charSequence.toString(), locations);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed
            }
        });

    }

    private int getResourceIdForDrawable(String drawableName) {
        // Remove spaces and convert to lowercase
        String resourceName = drawableName.replace(" ", "_").toLowerCase();

        // Remove curved brackets from the string
        resourceName = resourceName.replaceAll("[()]", "");

        // Get the package name of your app
        String packageName = getPackageName();

        // Get the resource identifier for the drawable
        Resources resources = getResources();
        return resources.getIdentifier(resourceName, "drawable", packageName);
    }

    private void performSearch(String searchText, ArrayList<String> list) {
        // Filter the storedItems list based on the searchText
        ArrayList<String> filteredPlaces = new ArrayList<>();

        for (String name : list ) {
            // Perform a case-insensitive check to see if any field contains the searchText
            if (name != null && name.toLowerCase().contains(searchText.toLowerCase())) {
                filteredPlaces.add(name);
            }
        }

        // Create an instance of CustomListAdapter with the filtered items and set it to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredPlaces);
        lvMap.setAdapter(adapter);
    }

    private void startMenu(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }
    private void startHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void startEvents(Context context) {
        Intent intent = new Intent(context, EventsActivity.class);
        context.startActivity(intent);
    }
    private void startBackActivity() {
        finish();
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