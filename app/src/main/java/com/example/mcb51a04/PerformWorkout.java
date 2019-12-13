package com.example.mcb51a04;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PerformWorkout extends AppCompatActivity {

    private Workout workout;
    LinearLayout exerciseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView txtTitle = findViewById(R.id.txtTitle);
        exerciseList = findViewById(R.id.llExercises);

        Intent intent = getIntent();
        if (intent != null) {
            workout = (Workout) intent.getSerializableExtra("workout");
            if(workout != null){
                txtTitle.setText(workout.getName());
                loadExercises();
            }
        }

        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWorkout();
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

    private void finishWorkout() {
        for(Exercise ex : workout.getExercises()){
            LinearLayout exerciseLayout = findViewById(R.id.scrollView).findViewWithTag("exercise-" + ex.getId() );
            boolean incrementExercise = true;
            for (int i = 0; i < exerciseLayout.getChildCount(); i++) {
                final View child = exerciseLayout.getChildAt(i);
                if (child instanceof TextView) {
                    TextView tv = (TextView)child;
                    if(Integer.parseInt(tv.getText().toString()) < ex.getReps())
                        incrementExercise = false;
                }
            }

            DbHandler db = new DbHandler(this);
            if(db.saveHistory(ex) == -1) ToastMessage("Something went wrong with saving history");

            if(incrementExercise){
                db = new DbHandler(this);
                if(db.incrementExercise(ex.getId(), ex.getWeight() + ex.getIncrement()) == -1) ToastMessage("Something went wrong with saving exercise increment");
            }

            db.close();
        }

        sendToMainMenu();
    }

    private void sendToMainMenu() {
        ToastMessage("Great workout!");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void loadExercises() {
        for(Exercise ex : workout.getExercises()){
            createExercisePartial(ex);
        }
    }

    private void createExercisePartial(Exercise ex) {
        setHeader(ex);

        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llParams.setMargins(100, 30, 100, 50);

        LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        edtParams.setMargins(20, 0, 20, 0);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);;
        ll.setBackground(ContextCompat.getDrawable(this, R.drawable.light_border_round));
        ll.setGravity(Gravity.CENTER);
        ll.setLayoutParams(llParams);
        ll.setTag("exercise-" + ex.getId());

        for(int i = 0; i < ex.getSets(); i++){
            TextView txtRep = new TextView(this);
            txtRep.setTag(String.format("%d-%d", ex.getId(), i));
            txtRep.setTextAppearance(R.style.EditTextTheme);
            txtRep.setText(Integer.toString(ex.getReps()));
            txtRep.setBackground(ContextCompat.getDrawable(this, R.drawable.dark_border_square));
            txtRep.setLayoutParams(edtParams);
            txtRep.setOnClickListener(updateRepsCompleted(txtRep));
            txtRep.setTag(ex);
            ll.addView(txtRep);
        }

        exerciseList.addView(ll);
    }

    private void setHeader(Exercise ex) {
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tvParams.setMargins(100, 20, 100, 20);

        TextView tvName = new TextView(this);
        tvName.setText(String.format("%s - %.1f lbs", ex.getName(), ex.getWeight()));
        tvName.setTextAppearance(R.style.TextTheme);
        tvName.setLayoutParams(tvParams);
        exerciseList.addView(tvName);

        TextView tvGoal = new TextView(this);
        tvGoal.setText(String.format("Goal: %d x %d", ex.getReps(),  ex.getSets()));
        tvGoal.setTextAppearance(R.style.TextTheme);
        tvGoal.setLayoutParams(tvParams);
        exerciseList.addView(tvGoal);
    }

    View.OnClickListener updateRepsCompleted(final TextView txtRep) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                int currentReps = Integer.parseInt(txtRep.getText().toString());
                if(currentReps > 0){
                    txtRep.setText(Integer.toString(currentReps - 1));
                }else{
                    Exercise ex = (Exercise) txtRep.getTag();
                    txtRep.setText(Integer.toString(ex.getReps()));
                }
            }
        };
    }
    private void ToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
