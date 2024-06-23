package com.example.finalproject.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

import java.util.Objects;

public class HelpActivity extends AppCompatActivity {
    private Spinner intensitySpinner;
    private String intensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        intensitySpinner = findViewById(R.id.intensitySpinner);
        initializeSpinner();
    }

    //initialize spinner
    private void initializeSpinner() {
        SharedPreferences pref = this.getSharedPreferences("com.example.finalproject", Context.MODE_PRIVATE);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.activities_categories, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intensitySpinner.setAdapter(arrayAdapter);
        // Get the current setting to display as selected value in spinner:
        intensity = pref.getString("intensity", "1");// get currently saved spinner value
        int pos = 0;// need to find position of that string in the arrayAdapter used in the spinner:
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            if (intensity.equals(Objects.requireNonNull(arrayAdapter.getItem(i)).toString())) {
                pos = i;
            }
        }
        intensitySpinner.setSelection(pos);// get spinner to show the string of the currently chosen dateRange setting
        intensitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SettingsMenu", "intensitySpinner clicked.");
                intensity = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}