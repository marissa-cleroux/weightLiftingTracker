package com.example.mcb51a04;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditWorkout extends AppCompatActivity {
    private ArrayList<Exercise> exercises;
    private Exercise exerciseToAdd;
    private Spinner ddAddExercise;
    private Button btnAddExercise;
    private Button btnSave;
    private Workout workout;
    private LinearLayout exerciseList;
    private TextView edtWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        exerciseList = findViewById(R.id.exerciseList);
        edtWorkout = findViewById(R.id.edtWorkout);

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
                if (exerciseToAdd != null && !workout.containsExercise(exerciseToAdd)) {
                    workout.addExercise(exerciseToAdd);
                    exerciseToAdd = null;
                    updateList();
                } else if (exerciseToAdd == null) {
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
                if (workout.getExercises().size() != 0) {
                    if (updateWorkout()) {
                        ToastMessage("Workout updates saved!");
                        sendToManageWorkouts();
                    } else {
                        ToastMessage("Workout could not be saved");
                    }
                } else {
                    ToastMessage("You must add exercises to your workout!");
                }
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            workout = (Workout) intent.getSerializableExtra("workout");
            setFields();
        }

        loadSpinner();
    }

    public void setFields(){
        edtWorkout.setText(workout.getName());
        updateList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void sendToManageWorkouts() {
        Intent i = new Intent(this, ManageWorkouts.class);
        startActivity(i);
    }

    private boolean updateWorkout() {
        //TODO: Validate name is set
        workout.setName(edtWorkout.getText().toString());

        DbHandler db = new DbHandler(this);
        long id = db.updateWorkout(workout);

        return id != -1;
    }

//    private void addExerciseToList(Exercise exerciseToAdd) {
//        TextView tv = new TextView(this);
//        tv.setText(exerciseToAdd.getName() + " ( - )");
//        tv.setTextAppearance(R.style.TextTheme);
//        tv.setId(exerciseToAdd.getId());
//        ll.setOnClickListener(addRemoveExerciseListener(ll));
//        exerciseList.addView(tv);
//        exerciseToAdd = null;
//    }

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

    View.OnClickListener addRemoveExerciseListener(final LinearLayout ll) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                workout.removeExercise(ll.getId());
                updateList();
            }
        };
    }

    private void updateList() {
        exerciseList.removeAllViews();
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(75, 75);
        imgParams.setMargins(10, 0, 0, 0);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 15, 0, 15);

        for (Exercise ex : workout.getExercises()) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setLayoutParams(layoutParams);

            TextView tv = new TextView(this);
            tv.setText(ex.getName());
            tv.setTextAppearance(R.style.TextTheme);

            ImageView trash = new ImageView(this);
            trash.setBackgroundResource(R.drawable.trash);
            trash.setLayoutParams(imgParams);
            ll.setId(ex.getId());
            ll.setOnClickListener(addRemoveExerciseListener(ll));

            ll.addView(trash);
            ll.addView(tv);
            exerciseList.addView(ll);
        }
    }
}
