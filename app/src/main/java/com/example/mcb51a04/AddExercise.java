package com.example.mcb51a04;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddExercise extends AppCompatActivity {

    private EditText edtName;
    private EditText edtReps;
    private EditText edtSets;
    private EditText edtWeight;
    private EditText edtIncrement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtName = findViewById(R.id.edtExercise);
        edtReps = findViewById(R.id.edtReps);
        edtSets = findViewById(R.id.edtSets);
        edtWeight = findViewById(R.id.edtStWeight);
        edtIncrement = findViewById(R.id.edtWeightIncrement);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addNewExercise()){
                    ToastMessage("Exercise successfully added!");
                    ReturnToMainMenu();
                }else {
                    ToastMessage("Exercise was not added, review the details and try again.");
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

    private void ReturnToMainMenu() {
        Intent i = new Intent(this, ManageExercises.class);
        startActivity(i);
    }

    private boolean addNewExercise() {
        String name = edtName.getText().toString();
        int reps = Integer.parseInt(edtReps.getText().toString());
        int set = Integer.parseInt(edtSets.getText().toString());
        double weight = Double.parseDouble(edtWeight.getText().toString());
        double increment = Double.parseDouble(edtIncrement.getText().toString());

        DbHandler db = new DbHandler(this);
        long id = db.addNewExercise(increment, name, set, reps, weight);

        return id != -1;
    }

    private void ToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
