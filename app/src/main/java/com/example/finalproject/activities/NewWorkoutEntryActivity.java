package com.example.finalproject.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.database.SQLAccess;
import com.example.finalproject.model.InputFilterMinMax;

public class NewWorkoutEntryActivity extends AppCompatActivity {
    public Button submitButton;
    private EditText descEntryEditText;
    private EditText durationEditText;
    private EditText intensityEditText;
    String weight;
    String calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        descEntryEditText = findViewById(R.id.descEntryEditText);
        durationEditText = findViewById(R.id.durationEditText);
        intensityEditText = findViewById(R.id.intensityEditText);
        intensityEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "18")});//limit input for intensity

        //load weight settings
        SharedPreferences pref = this.getSharedPreferences("com.example.finalproject", Context.MODE_PRIVATE);
        weight = pref.getString("kg", "63");

        //submit button on click
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener((View v) -> {
            String date = SQLAccess.getTodayDate();
            InsertOther(date);
        });
    }

    //Inserts input into the generic workout table
    private void InsertOther(String date) {
        String desc = descEntryEditText.getText().toString();
        String duration = durationEditText.getText().toString();
        String intensity = intensityEditText.getText().toString();
        if(!desc.isEmpty() && !duration.isEmpty() && !intensity.isEmpty()) {
            CalculateCalories(duration, intensity);//calculate calories
            SQLAccess.insertData("genericWorkout", new String[] {
                    date, desc, duration, intensity, calories
            });
            //Navigate back to workouts submenu if a new workout is successfully submitted
            Intent intent = new Intent(this, WorkoutSubMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "You must fill out all fields before submitting", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //calculate calories
    private void CalculateCalories(String duration, String intensity) {
        double cal = Integer.parseInt(duration) * (Integer.parseInt(intensity) * 3.5 * Integer.parseInt(weight)) / 200;
        cal = Math.round(cal * 100);
        double cal2 = cal / 100;
        calories = Double.toString(cal2);
    }
}