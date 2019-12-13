package com.example.mcb51a04;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_home){
            returnToMainMenu();
        } else if (id == R.id.action_about) {
            sendToAbout();
        } else if (id == R.id.action_help) {
            sendToHelp();
        } else if (id == R.id.action_exercises) {
            sendToManageExercises();
        } else if (id == R.id.action_history) {
            sendToHistory();
        } else if (id == R.id.action_workout) {
            sendToWorkout();
        } else if (id == R.id.action_workouts) {
            sendToManageWorkouts();
        }

        return super.onOptionsItemSelected(item);
    }

    private void returnToMainMenu() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void sendToHelp() {
        Intent i = new Intent(this, Help.class);
        startActivity(i);
    }

    private void sendToAbout() {
        Intent i = new Intent(this, About.class);
        startActivity(i);
    }

    public void sendToManageExercises(){
        Intent i = new Intent(this, ManageExercises.class);
        startActivity(i);
    }

    public void sendToManageWorkouts() {
        Intent i = new Intent(this, ManageWorkouts.class);
        startActivity(i);
    }

    private void sendToHistory() {
        Intent i = new Intent(this, HistoryActivity.class);
        startActivity(i);
    }

    private void sendToWorkout() {
        Intent i = new Intent(this, PickWorkout.class);
        startActivity(i);
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
                R.layout.simple_spinner_item, workouts);

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
