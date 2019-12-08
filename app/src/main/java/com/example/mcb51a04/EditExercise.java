package com.example.mcb51a04;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
                editExercise();
            }
        });
    }

    private void setFields() {
        edtName.setText(exercise.getName());
        edtReps.setText(Integer.toString(exercise.getReps()));
        edtSets.setText(Integer.toString(exercise.getSets()));
        edtWeight.setText(Double.toString(exercise.getWeight()));
        edtIncrement.setText(Double.toString(exercise.getIncrement()));
    }

    private void editExercise() {
        String name = edtName.getText().toString();
        int reps = Integer.parseInt(edtReps.getText().toString());
        int set = Integer.parseInt(edtSets.getText().toString());
        double weight = Double.parseDouble(edtWeight.getText().toString());
        double increment = Double.parseDouble(edtIncrement.getText().toString());

        DbHandler db = new DbHandler(this);
        db.updateExercise(exercise.getId(), increment, name, set, reps, weight);
        db.close();
        ToastMessage("Exercise was updated!");
        ReturnToMainMenu();
    }

    private void ReturnToMainMenu() {
        Intent i = new Intent(this, ManageExercises.class);
        startActivity(i);
    }

    private void ToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
