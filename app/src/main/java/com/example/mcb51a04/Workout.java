package com.example.mcb51a04;

import java.io.Serializable;
import java.util.ArrayList;

public class Workout extends NamedObject implements Serializable {
    private int id;
    private String name;
    private ArrayList<Exercise> exercises;

    public void setExercise(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise){
        exercises.add(exercise);
    }

    public ArrayList<Exercise> getExercises(){
        return exercises;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Workout(int id, String name) {
        super(name);
        this.id = id;
        this.exercises = new ArrayList<>();
    }

    public Workout() {
        this.id = 0;
        this.exercises = new ArrayList<>();
    }

    public boolean removeExercise(int id){
        Exercise exercise = null;
        for(Exercise ex : exercises){
            if(ex.getId() == id){
                exercise = ex;
            }
        }

        return (exercise != null) ? exercises.remove(exercise) : false;
    }
}
