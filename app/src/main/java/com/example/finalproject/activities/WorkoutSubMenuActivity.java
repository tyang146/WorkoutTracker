package com.example.finalproject.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.database.SQLAccess;
import com.example.finalproject.model.GenericWorkoutLog;
import com.example.finalproject.model.InputFilterMinMax;

public class WorkoutSubMenuActivity extends BaseSubMenuActivity {
    String dateRange = "All";
    protected Spinner dateSpinner;
    String weight;

    @Override
    protected void onResume() {//reload data preview cards on resuming activity
        super.onResume();
        initialize();
    }

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_submenu);
        super.initialize();
        tableName = "genericWorkout";

        //load weight settings
        SharedPreferences pref = this.getSharedPreferences("com.example.finalproject", Context.MODE_PRIVATE);
        weight = pref.getString("kg", "63");

        //dynamically generate submenu for workouts
        Log.d("DEBUG", "Loading workouts submenu: ");
        initializeSpinners();//initialize spinners

        //new entry button on click
        newEntryButton = findViewById(R.id.newEntryButton);
        newEntryButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NewWorkoutEntryActivity.class);
            startActivity(intent);
        });
        loadDBs();//load data onto recycler view
        refreshCards();//refresh recycler view
    }

    //preview workout on card click
    @Override
    protected void onPreviewCardClick(int position) {
        Log.d("WorkoutSubmenuActivity", "card clicked");
        //get data on card
        GenericWorkoutLog blank = (GenericWorkoutLog) cardsList.get(position);
        String date = blank.getDate();
        String desc = blank.getText();
        String duration = Integer.toString(blank.getDuration());
        String intensity = Integer.toString(blank.getIntensity());
        String calories = Double.toString(blank.getCalories());

        //alert layout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText editDuration = new EditText(this);
        editDuration.setHint("Duration (minutes)");
        editDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
        editDuration.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
        editDuration.setText(duration);
        editDuration.setSelection(editDuration.getText().length());

        final EditText editIntensity = new EditText(this);
        editIntensity.setHint("Intensity (1-18)");
        editIntensity.setInputType(InputType.TYPE_CLASS_NUMBER);
        editIntensity.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "18")});//limit input for intensity
        editIntensity.setText(intensity);
        editIntensity.setSelection(editIntensity.getText().length());

        final EditText editDesc = new EditText(this);
        editDesc.setHint("Description");
        editDesc.setText(desc);
        editDesc.setSelection(editDesc.getText().length());

        //initialize layout
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editIntensity);
        layout.addView(editDuration);
        layout.addView(editDesc);
        alertDialogBuilder.setMessage(date + "\nCalories Burned: " + calories);
        alertDialogBuilder.setView(layout);

        //delete workout on remove button click
        alertDialogBuilder.setPositiveButton("Remove", (arg0, arg1) -> {
            SQLAccess.deleteData(tableName, new String[]{date, desc, duration, intensity, calories});
            loadDBs();
            refreshCards();
        });

        //edit workout on edit button click
        alertDialogBuilder.setNegativeButton("Edit", (dialog, which) -> {
            //get new data
            String desc2 = editDesc.getText().toString();
            String duration2 = editDuration.getText().toString();
            String intensity2 = editIntensity.getText().toString();
            if(!desc2.isEmpty() && !duration2.isEmpty() && !intensity2.isEmpty()) {
                //calculate calories
                double cal = Integer.parseInt(duration2)*(Integer.parseInt(intensity2)*3.5*Integer.parseInt(weight))/200;
                cal = Math.round(cal * 100);
                double cal2 = cal/100;
                String calories2 = Double.toString(cal2);
                //replace data
                SQLAccess.replaceData(tableName, new String[]{date, desc, duration, intensity, calories}, new String[]{date, desc2, duration2, intensity2, calories2});
                loadDBs();
                refreshCards();
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "You must fill out all fields. Nothings Changed.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();//SHOW ALERT
    }

    protected void initializeSpinners() {
        //initialize date range spinner
        dateSpinner = findViewById(R.id.dateSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.date_categories, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(arrayAdapter);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("WorkoutSubmenu", "dateSpinner clicked.");
                dateRange = parent.getItemAtPosition(position).toString();
                loadDBs();
                refreshCards();//refresh preview cards displayed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadDBs();
                refreshCards();//refresh preview cards displayed
            }
        });
    }

    //refresh recycler view
    protected void refreshCards() {
        super.initializeCards();
    }

    //load data into recycler view
    protected void loadDBs() {
        cardsList.clear();//clear cardslist before (re)populating
        loadDbTable();
        if (cardsList.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "No results in database", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,400);
            toast.show();
        }
    }

    //load data according to date
    protected void loadDbTable() {
        Cursor cursor;
        String order = "DESC";
        switch (dateRange) {
            case "Today":
                cursor = SQLAccess.select("genericWorkout", SQLAccess.getTodayDate(), order);
                break;
            case "Past Week":
                cursor = SQLAccess.selectPastWeek("genericWorkout", order);
                break;
            case "Past Month":
                cursor = SQLAccess.selectPastMonth("genericWorkout", order);
                break;
            case "Past 6 Months":
                cursor = SQLAccess.selectPast6Months("genericWorkout", order);
                break;
            case "Past Year":
                cursor = SQLAccess.selectPastYear("genericWorkout", order);
                break;
            default://defaults to "All"
                cursor = SQLAccess.selectAll("genericWorkout", order);
                break;
        }

        //keeps workouts in order
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isLast()) { //Important
                getGenericWorkoutDbRow(cursor);
                cursor.move(1);
            } //get last row of data:
            getGenericWorkoutDbRow(cursor);
        } else {
            Log.d("WorkoutsSubmenu", "DB query found no results");
        }
    }

    //get workout data and add to recycler view
    protected void getGenericWorkoutDbRow(Cursor cursor) {
        GenericWorkoutLog temp = new GenericWorkoutLog();
        temp.setDate(cursor.getString(1));
        temp.setText(cursor.getString(2));
        temp.setDuration(cursor.getInt(3));
        temp.setIntensity(cursor.getInt(4));
        temp.setCalories(cursor.getDouble(5));
        cardsList.add(temp);
    }
}