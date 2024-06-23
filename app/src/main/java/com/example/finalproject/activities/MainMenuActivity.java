package com.example.finalproject.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.finalproject.R;
import com.example.finalproject.database.SQLAccess;

public class MainMenuActivity extends AppCompatActivity {
    public Button workoutsButton;
    public Button settingsButton;
    public Button helpButton;
    String weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        SQLAccess.initialize(this, this.getApplicationContext());//Initialize database access
        //load weight settings
        SharedPreferences pref = this.getSharedPreferences("com.example.finalproject", Context.MODE_PRIVATE);
        weight = pref.getString("kg", "63");

        createTestDB();//populate DB with sample data

        //load dark mode settings
        final boolean isDarkModeOn = pref.getBoolean("isDarkModeOn", false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        initializeButtons();//make buttons work
    }

    // ********** INSERT TEST DATA **********
    //Debug method for database
    private void createTestDB() {
        String[] date = {"2021-01-01", "2021-05-12", "2021-10-23", "2021-10-27", "2021-09-25", "2021-07-17", "2021-03-05"};
        String[] desc = {"Soccer at the park", "Core workout", "Yoga", "Hiking at High Cliff", "Flag football", "tennis", "bike"};
        Integer[] duration = {90, 40, 45, 90, 50, 60, 120};
        Integer[] intensity = {6, 11, 12, 8, 4, 8, 4};
        //calculate calories and insert data
        for (int i = 0; i < 7; i++) {
            double cal = duration[i]*(intensity[i]*3.5*Integer.parseInt(weight))/200;
            cal = Math.round(cal * 100);
            double cal2 = cal/100;
            String calories = Double.toString(cal2);
            SQLAccess.insertData("genericWorkout", new String[]{date[i],
                    desc[i], Integer.toString(duration[i]), Integer.toString(intensity[i]), calories});
        }
    }

    private void initializeButtons() {
        //workout button on click
        workoutsButton = findViewById(R.id.workoutsButton);
        workoutsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, WorkoutSubMenuActivity.class);
            startActivity(intent);
        });
        //help button on click
        helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        });
        //setting button on click
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}