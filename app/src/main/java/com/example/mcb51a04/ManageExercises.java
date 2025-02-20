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
import java.util.HashMap;

public class ManageExercises extends AppCompatActivity {

    private Spinner ddEdits;
    private Spinner ddDeletes;
    private ArrayList<Exercise> exercises;
    private Exercise exerciseToEdit;
    private Exercise exerciseToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercises);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ddEdits = findViewById(R.id.ddEditExercise);
        ddDeletes = findViewById(R.id.ddDeleteExercise);

        loadSpinners();

        Button btnNew = findViewById(R.id.btnNewExercise);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToAddExercise();
            }
        });

        Button btnEdit = findViewById(R.id.btnEditExercise);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToEditExercise();
            }
        });

        Button btnDelete = findViewById(R.id.btnDeleteExercise);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteExercise();
            }
        });

        ddDeletes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    exerciseToDelete = exercises.get(i - 1);
                } else {
                    exerciseToDelete = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ddEdits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    exerciseToEdit = exercises.get(i - 1);
                } else {
                    exerciseToEdit = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                exerciseToEdit = null;
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

    private void sendToHelp() {
        Intent i = new Intent(this, Help.class);
        startActivity(i);
    }

    private void returnToMainMenu() {
        Intent i = new Intent(this, MainActivity.class);
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

    private void deleteExercise() {
        if (exerciseToDelete == null) {
            ToastMessage("You must select an exercise to delete!");
            return;
        }

        DbHandler db = new DbHandler(this);
        db.deleteExercise(exerciseToDelete.getId());
        db.close();
        ToastMessage(exerciseToDelete.getName() + " was deleted!");
        loadSpinners();
        exerciseToDelete = null;
        exerciseToEdit = null;
    }

    private void sendToEditExercise() {
        if (exerciseToEdit == null) {
            ToastMessage("You must select an exercise to edit!");
            return;
        }

        Intent i = new Intent(this, EditExercise.class);
        i.putExtra("exercise", exerciseToEdit);
        i.putExtra("name", exerciseToEdit.getName());
        startActivity(i);
    }

    private void sendToAddExercise() {
        Intent i = new Intent(this, AddExercise.class);
        startActivity(i);
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

    private void loadSpinners() {
        ArrayList<String> exercises = getAllExercises();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, exercises);

        exercises.add(0, "--Select One--");
        ddEdits.setAdapter(dataAdapter);
        ddDeletes.setAdapter(dataAdapter);
    }

    private void ToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
