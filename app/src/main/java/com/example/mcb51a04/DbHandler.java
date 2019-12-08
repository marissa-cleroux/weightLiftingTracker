package com.example.mcb51a04;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StrongFolk.db";

    private static final String WORKOUTS_TABLE_NAME = "workout";
    private static final String WORKOUTS_COLUMN_ID = "id";
    private static final String WORKOUTS_COLUMN_NAME = "name";

    private static final String EXERCISES_TABLE_NAME = "exercise";
    private static final String EXERCISES_COLUMN_ID = "id";
    private static final String EXERCISES_COLUMN_NAME = "name";
    private static final String EXERCISES_COLUMN_SETS = "sets";
    private static final String EXERCISES_COLUMN_REPS = "reps";
    private static final String EXERCISES_COLUMN_WEIGHT = "weight";
    private static final String EXERCISES_COLUMN_INCREMENT = "increment";

    private static final String PLANNED_WORKOUT_TABLE_NAME = "planned_workout";
    private static final String PLANNED_WORKOUT_COLUMN_ID = "id";
    private static final String PLANNED_WORKOUT_COLUMN_WORKOUT_ID = "workout_id";
    private static final String PLANNED_WORKOUT_COLUMN_EXERCISE_ID = "exercise_id";

    private static final String EXERCISE_HISTORY_TABLE_NAME = "exercise_history";
    private static final String EXERCISE_HISTORY_COLUMN_ID = "id";
    private static final String EXERCISE_HISTORY_EXERCISE_COLUMN_ID = "exercise_id";
    private static final String EXERCISE_HISTORY_COLUMN_SETS = "sets";
    private static final String EXERCISE_HISTORY_COLUMN_REPS = "reps";
    private static final String EXERCISE_HISTORY_COLUMN_WEIGHT = "weight";
    private static final String EXERCISE_HISTORY_COLUMN_TARGET_SETS = "target_sets";
    private static final String EXERCISE_HISTORY_COLUMN_TARGET_REPS = "target_reps";
    private static final String EXERCISE_HISTORY_COLUMN_DATE = "date_completed";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + WORKOUTS_TABLE_NAME +
                        " (" + WORKOUTS_COLUMN_ID + " integer primary key, " +
                        WORKOUTS_COLUMN_NAME + " text)");

        db.execSQL(
                "create table " + EXERCISES_TABLE_NAME +
                        " (" + EXERCISES_COLUMN_ID + " integer primary key, " +
                        EXERCISES_COLUMN_NAME + " text, " +
                        EXERCISES_COLUMN_SETS + " integer, " +
                        EXERCISES_COLUMN_REPS + " integer, " +
                        EXERCISES_COLUMN_WEIGHT + " double, " +
                        EXERCISES_COLUMN_INCREMENT + " double)");

        db.execSQL(
                "create table " + PLANNED_WORKOUT_TABLE_NAME +
                        " (" + PLANNED_WORKOUT_COLUMN_ID + " integer primary key, " +
                        PLANNED_WORKOUT_COLUMN_WORKOUT_ID + " integer, " +
                        PLANNED_WORKOUT_COLUMN_EXERCISE_ID + " integer, " +
                        " foreign key  (" + PLANNED_WORKOUT_COLUMN_WORKOUT_ID + ") REFERENCES " +
                        WORKOUTS_TABLE_NAME + " (" + WORKOUTS_COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, " +
                        " foreign key (" + PLANNED_WORKOUT_COLUMN_EXERCISE_ID + ") REFERENCES " +
                        EXERCISES_TABLE_NAME + " (" + EXERCISES_COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE)");

        db.execSQL(
                "create table " + EXERCISE_HISTORY_TABLE_NAME +
                        " (" + EXERCISE_HISTORY_COLUMN_ID + " integer primary key, " +
                        EXERCISE_HISTORY_COLUMN_SETS + " integer, " +
                        EXERCISE_HISTORY_COLUMN_REPS + " integer, " +
                        EXERCISE_HISTORY_COLUMN_WEIGHT + " integer, " +
                        EXERCISE_HISTORY_COLUMN_TARGET_SETS + " double, " +
                        EXERCISE_HISTORY_COLUMN_TARGET_REPS + " double, " +
                        EXERCISE_HISTORY_COLUMN_DATE + " date, " +
                        EXERCISE_HISTORY_EXERCISE_COLUMN_ID + " integer, " +
                        " foreign key (" + EXERCISE_HISTORY_EXERCISE_COLUMN_ID + ") REFERENCES " +
                        EXERCISES_TABLE_NAME + " (" + EXERCISES_COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + PLANNED_WORKOUT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WORKOUTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_HISTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXERCISES_TABLE_NAME);

        onCreate(db);
    }

    public ArrayList<Exercise> getAllExercises() {
        ArrayList<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + EXERCISES_TABLE_NAME, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            int id = res.getInt(res.getColumnIndex(EXERCISES_COLUMN_ID));
            String name = res.getString(res.getColumnIndex(EXERCISES_COLUMN_NAME));
            int sets = res.getInt(res.getColumnIndex(EXERCISES_COLUMN_SETS));
            int reps = res.getInt(res.getColumnIndex(EXERCISES_COLUMN_REPS));
            double weight = res.getDouble(res.getColumnIndex(EXERCISES_COLUMN_WEIGHT));
            double increment = res.getDouble(res.getColumnIndex(EXERCISES_COLUMN_INCREMENT));

            Exercise exercise = new Exercise(id, name, sets, reps, weight, increment);
            exercises.add(exercise);
            res.moveToNext();
        }
        res.close();

        exercises.sort(new NameComparator());
        return exercises;
    }

    public long addNewExercise (double increment, String name, int sets, int reps, double weight)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(EXERCISES_COLUMN_INCREMENT, increment);
        contentValues.put(EXERCISES_COLUMN_NAME, name);
        contentValues.put(EXERCISES_COLUMN_SETS, sets);
        contentValues.put(EXERCISES_COLUMN_REPS, reps);
        contentValues.put(EXERCISES_COLUMN_WEIGHT, weight);

        return db.insert(EXERCISES_TABLE_NAME, null, contentValues);
    }

    public long updateExercise (int id, double increment, String name, int sets, int reps, double weight)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(EXERCISES_COLUMN_INCREMENT, increment);
        contentValues.put(EXERCISES_COLUMN_NAME, name);
        contentValues.put(EXERCISES_COLUMN_SETS, sets);
        contentValues.put(EXERCISES_COLUMN_REPS, reps);
        contentValues.put(EXERCISES_COLUMN_WEIGHT, weight);

        return db.update(EXERCISES_TABLE_NAME, contentValues, EXERCISES_COLUMN_ID + " = ?",  new String[] { Integer.toString(id) } );
    }

    public boolean deleteExercise (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXERCISES_TABLE_NAME, EXERCISES_COLUMN_ID + " = ?", new String[]{Integer.toString(id)});
        return true;
    }
}
