package com.example.mcb51a04;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AddWorkout extends AppCompatActivity {

    private ArrayList<Exercise> exercises;
    private Exercise exerciseToAdd;
    private Spinner ddAddExercise;
    private Button btnAddExercise;
    private Button btnSave;
    private Workout workout;
    private LinearLayout exerciseList;
    private TextView edtWorkout;
    private HashMap<Integer, Exercise> exerciseDict = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        exerciseList = findViewById(R.id.exerciseList);
        edtWorkout = findViewById(R.id.edtWorkout);
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
                if(exerciseToAdd != null && !workout.getExercises().contains(exerciseToAdd)){
                    workout.addExercise(exerciseToAdd);
                    addExerciseToList(exerciseToAdd);
                } else if(exerciseToAdd == null){
                    ToastMessage("Please select an exercise to add.");
                } else {
                    ToastMessage("That exercise has already been added to your workout.");
                }
            }
        });

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(workout.getExercises().size() != 0 ){
                    if(saveWorkout()){
                        sendToManageWorkouts();
                    } else {
                        ToastMessage("Workout could not be saved");
                    }
                } else {
                    ToastMessage("You must add exercises to your workout!");
                }
            }
        });

        loadSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void sendToManageWorkouts() {

    }

    private boolean saveWorkout() {
        //TODO: Validate name is set
        workout.setName(edtWorkout.getText().toString());

        DbHandler db = new DbHandler(this);
        long id = db.addNewWorkout(workout);

        return id != -1;
    }

    private void addExerciseToList(Exercise exerciseToAdd) {
        TextView tv = new TextView(this);
        tv.setText(exerciseToAdd.getName() + " ( - )");
        tv.setTextAppearance(R.style.TextTheme);
        tv.setId(exerciseToAdd.getId());
        tv.setOnClickListener(addRemoveExerciseListener(tv));
        exerciseList.addView(tv);
        exerciseDict.put(tv.getId(), exerciseToAdd);

        ToastMessage(exerciseToAdd.getId() + " added.");
        exerciseToAdd = null;
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

    private void ToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener addRemoveExerciseListener(final TextView tv)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                String exercise = tv.getText().toString();
                workout.removeExercise(tv.getId());
                ToastMessage(tv.getId() + " removed.");
                updateList();
            }
        };
    }

    private void updateList() {
        

    }


}
