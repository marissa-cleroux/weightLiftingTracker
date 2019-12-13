package com.example.mcb51a04;

import android.content.Intent;
import android.os.Bundle;

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

public class ManageWorkouts extends AppCompatActivity {

    private Spinner ddEdits;
    private Spinner ddDeletes;
    private ArrayList<Workout> workouts;
    private Workout workoutToEdit;
    private Workout workoutToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workouts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ddEdits = findViewById(R.id.ddEditWorkout);
        ddDeletes = findViewById(R.id.ddDeleteWorkout);

        loadSpinners();

        Button btnNew = findViewById(R.id.btnNewWorkout);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToAddWorkout();
            }
        });

        Button btnEdit = findViewById(R.id.btnEditWorkout);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToEditWorkout();
            }
        });

        Button btnDelete = findViewById(R.id.btnDeleteWorkout);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWorkout();
            }
        });

        ddDeletes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    workoutToDelete = workouts.get(i - 1);
                } else {
                    workoutToDelete = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                workoutToDelete = null;
            }
        });

        ddEdits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    workoutToEdit = workouts.get(i - 1);
                } else {
                    workoutToEdit = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void deleteWorkout() {
        if (workoutToDelete == null) {
            ToastMessage("You must select an exercise to delete!");
            return;
        }

        DbHandler db = new DbHandler(this);
        if(db.deleteWorkout(workoutToDelete.getId())){
            db.close();
            ToastMessage(workoutToDelete.getName() + " was deleted!");
            loadSpinners();
            workoutToDelete = null;
            workoutToEdit = null;
        } else {
            ToastMessage(workoutToDelete.getName() + " could not be deleted!");
        }

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

    private void sendToEditWorkout() {
        if(workoutToEdit == null) {
            ToastMessage("You must select a workout to edit!");
            return;
        }

        Intent i = new Intent(this, EditWorkout.class);
        i.putExtra("workout", workoutToEdit);
        startActivity(i);
    }

    private void sendToAddWorkout() {
        Intent i = new Intent(this, AddWorkout.class);
        startActivity(i);
    }

    private void loadSpinners() {
        ArrayList<String> workouts = getAllWorkouts();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, workouts);

        workouts.add(0, "--Select One--");
        ddEdits.setAdapter(dataAdapter);
        ddDeletes.setAdapter(dataAdapter);
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
