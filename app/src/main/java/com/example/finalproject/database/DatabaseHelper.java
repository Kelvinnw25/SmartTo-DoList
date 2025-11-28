package com.example.finalproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "priolistdb";
    private static final int DATABASE_VERSION = 1;

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

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Query SQL untuk membuat tabel
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Primary Key
                COLUMN_TITLE + " TEXT NOT NULL," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_DEADLINE + " INTEGER," + // Disimpan sebagai UNIX Timestamp (long/INTEGER)
                COLUMN_IMPORTANCE + " INTEGER," +
                COLUMN_IS_COMPLETED + " INTEGER," + // 0 = false, 1 = true
                COLUMN_PRIORITY_SCORE + " REAL" + // Angka desimal (REAL)
                ")";

        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Drop tabel lama dan buat yang baru
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }
}
