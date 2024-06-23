package com.example.finalproject.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.finalproject.R;

public class SettingsActivity extends AppCompatActivity {
    public Button saveSettingsButton;
    public Button btnToggleDark;
    private EditText weightEditText;
    String weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initialize();//make buttons work
    }

    @SuppressLint("SetTextI18n")
    private void initialize() {
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        btnToggleDark = findViewById(R.id.btnToggleDark);
        weightEditText = findViewById(R.id.weightEditText);

        //initialize shared weight settings
        SharedPreferences pref = this.getSharedPreferences("com.example.finalproject", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        weight = pref.getString("kg", "63");
        weightEditText.setText(weight);
        weightEditText.setSelection(weightEditText.getText().length());

        //initialize shared dark mode settings
        final SharedPreferences.Editor editor2 = pref.edit();
        final boolean isDarkModeOn = pref.getBoolean("isDarkModeOn", false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            btnToggleDark.setText("Disable Dark Mode");
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            btnToggleDark.setText("Enable Dark Mode");
        }

        //dark mode button on click
        btnToggleDark.setOnClickListener((View v) -> {
            if (isDarkModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor2.putBoolean("isDarkModeOn", false);
                editor2.apply();
                btnToggleDark.setText("Enable Dark Mode");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor2.putBoolean("isDarkModeOn", true);
                editor2.apply();
                btnToggleDark.setText("Disable Dark Mode");
            }
        });

        //save settings button on click
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        saveSettingsButton.setOnClickListener(v -> {
            editor.putString("kg", weightEditText.getText().toString());
            editor.apply();
            Toast toast = Toast.makeText(getApplicationContext(), "Settings Saved!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 350);
            toast.show();
            finish();//close activity and return to menu
        });
    }
}