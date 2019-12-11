package com.example.mcb51a04;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditExercise extends AppCompatActivity {

    private Exercise exercise;
    private EditText edtName;
    private EditText edtReps;
    private EditText edtSets;
    private EditText edtWeight;
    private EditText edtIncrement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtName = findViewById(R.id.edtExercise);
        edtReps = findViewById(R.id.edtReps);
        edtSets = findViewById(R.id.edtSets);
        edtWeight = findViewById(R.id.edtStWeight);
        edtIncrement = findViewById(R.id.edtWeightIncrement);

        Intent intent = getIntent();
        if (intent != null) {
            exercise = (Exercise) intent.getSerializableExtra("exercise");
            exercise.setName(intent.getStringExtra("name"));
            setFields();
        }

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editExercise()){
                    ToastMessage("Exercise was updated!");
                    ReturnToMainMenu();
                }else {
                    ToastMessage("Exercise could not be updated, please review your details and try again!");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setFields() {
        edtName.setText(exercise.getName());
        edtReps.setText(Integer.toString(exercise.getReps()));
        edtSets.setText(Integer.toString(exercise.getSets()));
        edtWeight.setText(Double.toString(exercise.getWeight()));
        edtIncrement.setText(Double.toString(exercise.getIncrement()));
    }

    private boolean editExercise() {
        String name;
        int reps;
        int sets;
        double weight;
        double increment;

        try{
            name = edtName.getText().toString();
            reps = Integer.parseInt(edtReps.getText().toString());
            sets = Integer.parseInt(edtSets.getText().toString());
            weight = Double.parseDouble(edtWeight.getText().toString());
            increment = Double.parseDouble(edtIncrement.getText().toString());
        }catch(Exception e) {
            return false;
        }

        int valid = new Exercise(exercise.getId(), name, sets, reps, weight, increment).validateExercise();

        if(valid == 0){
            DbHandler db = new DbHandler(this);
            long id = db.updateExercise(exercise.getId(), increment, name, sets, reps, weight);
            db.close();

            return id != -1;
        }else {
            ErrorMessage(valid);
            return false;
        }
    }

    private void ReturnToMainMenu() {
        Intent i = new Intent(this, ManageExercises.class);
        startActivity(i);
    }

    private void ErrorMessage(int invalidStatus) {
        switch(invalidStatus){
            case Exercise.InvalidIncrement:
                ToastMessage("Increment must be greater than 0");
                break;
            case Exercise.InvalidName:
                ToastMessage("Name must be set");
                break;
            case Exercise.InvalidReps:
                ToastMessage("Reps must be greater than 0");
                break;
            case Exercise.InvalidSets:
                ToastMessage("Sets must be greater than 0 and less than 6.");
                break;
            case Exercise.InvalidWeight:
                ToastMessage("Weights must be greater than 0");
                break;
        }
    }

    private void ToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
