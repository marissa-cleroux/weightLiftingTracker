package com.example.mcb51a04;

public class ExerciseHistory extends NamedObject  {

    private double weight;
    private String date;

    public ExerciseHistory() {
        this.weight = 0;
        this.date = "unknown";
    }

    public ExerciseHistory(String name, double weight, String date) {
        super(name);
        this.weight = weight;
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
