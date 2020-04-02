package com.X00135794.medminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MedItemActivity extends AppCompatActivity {

    private TextView medName;
    private TextView medDose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Medication");
        medName = findViewById(R.id.medItemName);
        medDose = findViewById(R.id.medItemDosage);

        // getting this current intent to get the passed in values
        Intent intent = getIntent();
        String name = intent.getStringExtra("medName");
        String dose = intent.getStringExtra("medDose");

        medName.setText("Medication name: " + name);
        medDose.setText("Dosage: " +dose);


    }
}
