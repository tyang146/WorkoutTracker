package com.example.finalproject.model;

import com.example.finalproject.database.SQLAccess;

public class GenericWorkoutLog implements SubmenuPreviewCard {
    String text;
    int duration;
    int intensity;
    double calories;
    String date;

    public GenericWorkoutLog() { }

    @Override
    public String getDescription() {
        return SQLAccess.getFormattedDate(date) + "\nCalories Burned: " + calories + "\nIntensity: " + intensity + "\n" + duration + " min\n" + text;
    }
    public void setText(String text) { this.text = text; }
    public String getText() { return text; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getIntensity() { return intensity; }
    public void setIntensity(int intensity) { this.intensity = intensity; }
    public double getCalories() { return calories ; }
    public void setCalories(double calories ) { this.calories  = calories ; }
    public void setDate(String date) { this.date = date; }
    public String getDate() { return date; }
}