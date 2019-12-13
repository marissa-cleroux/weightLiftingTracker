package com.example.mcb51a04;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private Exercise exercise;
    private ArrayList<Exercise> exercises;
    private Spinner ddExercise;
    TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ddExercise = findViewById(R.id.ddExercise);
        tl = findViewById(R.id.historyTable);
        loadSpinners();

        ddExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    exercise = exercises.get(i - 1);
                    loadHistory();
                } else {
                    exercise = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                exercise = null;
            }
        });
    }

    private void loadHistory() {
        DbHandler db = new DbHandler(this);
        ArrayList<ExerciseHistory> history = db.getExerciseHistory(exercise.getId());
        db.close();

        tl.removeAllViews();
        setHeaders();
        for (ExerciseHistory ex : history) {
            AddHistoryPartial(ex);
        }
    }

    private void AddHistoryPartial(ExerciseHistory ex) {
        TableLayout.LayoutParams trlp =
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        trlp.setMargins(200, 10, 20, 0);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(trlp);
        tr.setPadding(5, 5, 5, 5);

        TextView txtWeight = new TextView(this);
        txtWeight.setText(Double.toString(ex.getWeight()));
        txtWeight.setTextSize(20);

        TextView txtDate = new TextView(this);
        txtDate.setText(ex.getDate());
        txtDate.setTextSize(20);
        txtDate.setPadding(90, 0,0, 0);

        tr.addView(txtWeight);
        tr.addView(txtDate);

        tl.addView(tr);
    }

    private void setHeaders(){
        TableLayout.LayoutParams trlp =
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        trlp.setMargins(200, 10, 20, 0);

        TextView txtWeight = new TextView(this);
        txtWeight.setText("WEIGHT");
        txtWeight.setTextSize(20);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(trlp);
        tr.setPadding(5, 5, 5, 5);

        TextView txtDate = new TextView(this);
        txtDate.setText("DATE");
        txtDate.setTextSize(20);
        txtDate.setPadding(90, 0,0, 0);

        tr.addView(txtWeight);
        tr.addView(txtDate);

        tl.addView(getLine());
        tl.addView(tr);
        tl.addView(getLine());
    }

    private TableRow getLine(){
        TableRow.LayoutParams trlpLine =
                new TableRow.LayoutParams
                        (TableRow.LayoutParams.MATCH_PARENT, 1);
        trlpLine.span = 2;

        TableRow line = new TableRow(this);
        line.setLayoutParams(trlpLine);
        line.setBackgroundColor(Color.BLACK);

        TextView txtLine = new TextView(this);
        txtLine.setLayoutParams(trlpLine);

        line.addView(txtLine);
        return line;
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

    private void loadSpinners() {
        ArrayList<String> exercises = getAllExercises();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, exercises);

        exercises.add(0, "--Select One--");
        ddExercise.setAdapter(dataAdapter);
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
}
