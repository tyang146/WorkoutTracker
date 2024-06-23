package com.example.finalproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSQLHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "app_database";

    public DBSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        onCreate(super.getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creates the tables in the database
        sqLiteDatabase.execSQL(SQLAccess.accessTable("genericWorkout").createTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drops and remakes tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +
                SQLAccess.accessTable("genericWorkout").getTableName());
        onCreate(sqLiteDatabase);
    }
}