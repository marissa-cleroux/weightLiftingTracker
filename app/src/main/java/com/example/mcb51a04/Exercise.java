package com.example.mcb51a04;

import java.io.Serializable;

public class Exercise extends NamedObject implements Serializable {

    private int id;
    private int sets;
    private int reps;
    private double weight;
    private double increment;

    public Exercise(int id, String name, int sets, int reps, double weight, double increment) {
        super(name);
        this.id = id;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.increment = increment;
    }

    public Exercise() {
        super();
        this.id = 0;
        this.sets = 0;
        this.reps = 0;
        this.weight = 0;
        this.increment = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Exercise)){
            return false;
        }

        return this.getId() == ((Exercise)o).getId();
    }

    public static final int ValidExercise = 0;
    public static final int InvalidName = -1;
    public static final int InvalidSets = -2;
    public static final int InvalidReps = -3;
    public static final int InvalidIncrement = -4;
    public static final int InvalidWeight = -5;

    public int validateExercise(){
        if(!super.validateName()){
            return InvalidName;
        }

        if(sets > 5 || sets < 0){
            return InvalidSets;
        }

        if(reps < 0){
            return InvalidReps;
        }

        if(increment < 0){
            return InvalidIncrement;
        }

        if(weight < 0){
            return InvalidWeight;
        }

        return ValidExercise;
    }

}
