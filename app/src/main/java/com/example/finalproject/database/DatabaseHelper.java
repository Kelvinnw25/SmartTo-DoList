package com.example.finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.finalproject.model.Task;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "priolistdb";
    private static final int DATABASE_VERSION = 2;

    // Table Name
    public static final String TABLE_TASKS = "tasks";

    // Task Table Columns
    public static final String COLUMN_ID = "_id"; // Primary Key standar
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DEADLINE = "deadline_timestamp";
    public static final String COLUMN_IMPORTANCE = "importance_level";
    public static final String COLUMN_IS_COMPLETED = "is_completed";
    public static final String COLUMN_PRIORITY_SCORE = "priority_score";
    public static final String COLUMN_CATEGORY = "category";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL Query for create table
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT NOT NULL," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_DEADLINE + " INTEGER," +
                COLUMN_IMPORTANCE + " INTEGER," +
                COLUMN_IS_COMPLETED + " INTEGER," +
                COLUMN_PRIORITY_SCORE + " REAL," +
                COLUMN_CATEGORY + " TEXT" +
                ")";

        db.execSQL(CREATE_TASKS_TABLE);
    }

    public void updateTaskScore(int id, double newScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRIORITY_SCORE, newScore); // Kita hanya update kolom skor

        //update task score which has same ID with parameter ID
        db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        db.close();
    }

    //method for update task status
    public void updateTaskStatus(int id, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_COMPLETED, isCompleted ? 1 : 0);

        db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            //drop old table and create the new one
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    //method for add new task
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DEADLINE, task.getDeadlineTimestamp());
        values.put(COLUMN_IMPORTANCE, task.getImportanceLevel());
        values.put(COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_PRIORITY_SCORE, task.getPriorityScore());
        values.put(COLUMN_CATEGORY, task.getCategory());

        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();

        //descending order
        String SELECT_QUERY = "SELECT * FROM " + TABLE_TASKS +
                " ORDER BY " + COLUMN_PRIORITY_SCORE + " DESC";

        //readable
        SQLiteDatabase db = this.getReadableDatabase();

        //store query
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        //loop the row and add to list
        if (cursor.moveToFirst()) {
            do {
                //take the value in every column
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                long deadlineTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE));
                int importanceLevel = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMPORTANCE));
                //convert int to boolean
                boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1;
                double priorityScore = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY_SCORE));
                int catIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
                String category = (catIndex != -1) ? cursor.getString(catIndex) : "General";
                if(category == null) category = "General";

                //static factory for create object Task
                Task task = Task.fromDatabase(
                        id,
                        title,
                        description,
                        deadlineTimestamp,
                        importanceLevel,
                        isCompleted,
                        priorityScore,
                        category
                );

                taskList.add(task);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return taskList;
    }

    //method for delete all completed task
    public void deleteCompletedTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        //delete row where column is_completed = 1
        db.delete(TABLE_TASKS, COLUMN_IS_COMPLETED + " = ?", new String[]{"1"});
        db.close();
    }
}
