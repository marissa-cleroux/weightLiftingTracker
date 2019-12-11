package com.example.mcb51a04;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class PickWorkout extends AppCompatActivity {

    private Workout workoutToDo;
    private ArrayList<Workout> workouts;
    private Spinner ddWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ddWorkout = findViewById(R.id.ddWorkout);
        loadSpinners();

        ddWorkout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    workoutToDo = workouts.get(i - 1);
                } else {
                    workoutToDo = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                workoutToDo = null;
            }
        });

        Button btnWorkout = findViewById(R.id.btnWorkout);
        btnWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToDoWorkout();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void sendToDoWorkout() {
        if(workoutToDo == null) {
            ToastMessage("You must select a workout to perform!");
            return;
        }

        Intent i = new Intent(this, PerformWorkout.class);
        i.putExtra("workout", workoutToDo);
        startActivity(i);
    }

    private void loadSpinners() {
        ArrayList<String> workouts = getAllWorkouts();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, workouts);

        workouts.add(0, "--Select One--");
        ddWorkout.setAdapter(dataAdapter);
    }

    private ArrayList<String> getAllWorkouts() {
        DbHandler db = new DbHandler(this);
        workouts = db.getAllWorkouts();
        db.close();
        ArrayList<String> workoutDescriptions = new ArrayList<>();

        for (Workout w : workouts) {
            workoutDescriptions.add(w.getName());
        }

        return workoutDescriptions;
    }
    private void ToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
