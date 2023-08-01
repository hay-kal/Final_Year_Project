package com.example.final_year_project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueDisplay extends AppCompatActivity {

    TextView textView;
    Map<String, List<String>> categoryNumberMap;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_display);

        textView = findViewById(R.id.tvQueue);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Initialize the category and numbers mapping
        categoryNumberMap = new HashMap<>();
        categoryNumberMap.put("SNA", new ArrayList<>());
        categoryNumberMap.put("FA", new ArrayList<>());
        categoryNumberMap.put("IS", new ArrayList<>());
        categoryNumberMap.put("IT", new ArrayList<>());
        categoryNumberMap.put("SC", new ArrayList<>());
        categoryNumberMap.put("Others", new ArrayList<>());

        // Retrieve the category information from the intent
        String category = getIntent().getStringExtra("category");
        Log.d("QueueSelect", "Category: " + category);

        // Process the category information and display the appropriate output
        if (category != null) {
            if (categoryNumberMap.containsKey(category)) {
                List<String> numbers = loadNumbersForCategory(category);
                Log.d("QueueSelect", "Numbers: " + numbers.toString());

                // Generate a new number and add it to the list
                String newNumber = generateNewNumber(category, numbers);
                numbers.add(newNumber);

                // Update the output string with the latest number and set it to textView
                String outputString = "Selected category: " + category + "\nYour Queue Number Is: \n" + newNumber;
                textView.setText(outputString);
                Log.d("QueueSelect", "Output string: " + outputString);

                // Save the updated numbers list to SharedPreferences
                saveNumbersForCategory(category, numbers);
            } else {
                textView.setText("Unknown category selected");
            }
        } else {
            textView.setText("Category not provided");
        }

    }

    // Method to generate a new number based on the category and existing numbers
    private String generateNewNumber(String category, List<String> existingNumbers) {
        String prefix = category + "-";

        if (existingNumbers.isEmpty()) {
            // If no numbers exist for the category, start from 1
            return prefix + "001";
        } else {
            // Get the last generated number for the category
            String lastNumber = existingNumbers.get(existingNumbers.size() - 1);

            if (lastNumber.isEmpty()) {
                // Handle the case when lastNumber is an empty string
                return prefix + "001";
            } else if (lastNumber.equals(prefix + "999")) {
                // If the last number is "category-999", reset back to "category-001"
                return prefix + "001";
            } else if (lastNumber.length() < prefix.length() + 4) {
                // The last number does not have enough digits (e.g., "FA-1" instead of "FA-001")
                // Increment and format it with leading zeros
                int lastNumericPart = Integer.parseInt(lastNumber.substring(prefix.length()));
                int nextNumber = lastNumericPart + 1;
                return prefix + String.format("%03d", nextNumber);
            } else {
                // Extract the numeric part and increment it
                int lastNumericPart = Integer.parseInt(lastNumber.substring(prefix.length() + 1));
                int nextNumber = lastNumericPart + 1;// Format the next number with leading zeros and return it
                return prefix + String.format("%03d", nextNumber);
            }
        }
    }


    private List<String> loadNumbersForCategory(String category) {
        String numbersString = sharedPreferences.getString(category, "");
        String[] numbersArray = numbersString.split(",");
        List<String> numbers = new ArrayList<>();
        for (String number : numbersArray) {
            numbers.add(number.trim());
        }
        return numbers;
    }

    // Method to save the updated numbers list for a specific category to SharedPreferences
    private void saveNumbersForCategory(String category, List<String> numbers) {
        StringBuilder numbersString = new StringBuilder();
        for (String number : numbers) {
            numbersString.append(number).append(",");
        }
        sharedPreferences.edit().putString(category, numbersString.toString()).apply();
    }
}
