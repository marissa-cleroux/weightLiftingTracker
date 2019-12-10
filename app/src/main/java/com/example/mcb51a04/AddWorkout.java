package com.example.mcb51a04;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddWorkout extends AppCompatActivity {

    private ArrayList<Exercise> exercises;
    private Exercise exerciseToAdd;
    private Spinner ddAddExercise;
    private Button btnAddExercise;
    private Button btnSave;
    private Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        workout = new Workout();

        ddAddExercise = findViewById(R.id.ddAddExercise);
        ddAddExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    exerciseToAdd = exercises.get(i - 1);
                } else {
                    exerciseToAdd = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                exerciseToAdd = null;
            }
        });
        btnAddExercise = findViewById(R.id.btnAddExerciseToWorkout);
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exerciseToAdd != null){
                    workout.addExercise(exerciseToAdd);
                    addExerciseToList(exerciseToAdd);
                }else {

                }
            }
        });
        btnSave = findViewById(R.id.btnSave);

        loadSpinner();

    }

    private void addExerciseToList(Exercise exerciseToAdd) {

    }

    private void loadSpinner() {
        ArrayList<String> exercises = getAllExercises();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, exercises);

        exercises.add(0, "--Select One--");
        ddAddExercise.setAdapter(dataAdapter);
    }

    private ArrayList<String> getAllExercises() {
        DbHandler db = new DbHandler(this);
        exercises = db.getAllExercises();
        db.close();
        ArrayList<String> exerciseDescriptions = new ArrayList<>();

        for (Exercise ex : exercises) {
            exerciseDescriptions.add(ex.getName());
        }
        return exerciseDescriptions;
    }
}
