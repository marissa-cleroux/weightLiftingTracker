package com.example.mcb51a04;

import java.io.Serializable;
import java.util.ArrayList;

public class Workout extends NamedObject implements Serializable {
    private int id;
    private ArrayList<Exercise> exercises;

    public boolean containsExercise(Exercise exercise){
        for(Exercise ex : exercises){
            if(ex.equals(exercise))
                return true;
        }

        return false;
    }

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
        Exercise exercise = getExercise(id);

        return (exercise != null) ? exercises.remove(exercise) : false;
    }

    public Exercise getExercise(int id){
        for(Exercise ex : exercises){
            if(ex.getId() == id){
                return ex;
            }
        }

        return null;
    }
}
