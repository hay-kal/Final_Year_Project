package com.example.final_year_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Queue;

public class QueueSelect extends AppCompatActivity {

    Button SNA, FA, SC, IT, IS, Others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_select);

        SNA = findViewById(R.id.btnSNA1);
        FA = findViewById(R.id.btnFA1);
        SC = findViewById(R.id.btnSC1);
        IT = findViewById(R.id.btnIT1);
        IS = findViewById(R.id.btnIS1);
        Others = findViewById(R.id.btnOthers1);

        SNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("SNA");
            }
        });

        FA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("FA");
            }
        });

        IS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("IS");
            }
        });

        SC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("SC");
            }
        });

        IT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("IT");
            }
        });

        Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueueSelect("Others");
            }
        });
    }

    private void openQueueSelect(String category) {
        Intent intent = new Intent(QueueSelect.this, QueueDisplay.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
