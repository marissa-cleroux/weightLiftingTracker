package com.example.mcb51a04;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PerformWorkout extends AppCompatActivity {

    private Workout workout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView txtTitle = findViewById(R.id.txtTitle);

        Intent intent = getIntent();
        if (intent != null) {
            workout = (Workout) intent.getSerializableExtra("workout");
            txtTitle.setText(workout.getName());
            loadExercises();
        }
    }

    private void loadExercises() {
        for(Exercise ex : workout.getExercises()){
            createExercisePartial(ex);
        }
    }

    private void createExercisePartial(Exercise ex) {
        LinearLayout ll = new LinearLayout(this);


    }
}
