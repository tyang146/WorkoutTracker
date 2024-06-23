package com.example.finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//Class that allows for database access and modification
public class SQLAccess {
    private static SQLiteDatabase database = null;
    public static DBSQLHelper dbsqlHelper = null;
    public static Context AppContext;
    private static final Map<String, DBTable> TABLE_MAP = new HashMap<>();//Map of all databases

    // ***** DATABASE TABLES *****
    private static final DBTable genericWorkout = new DBTable("GenericWorkout", 5,
            new String[]{"Date", "Description", "Duration", "Intensity", "Calories"},
            new String[]{"TEXT", "TEXT", "INT", "INT", "DOUBLE"});

    public static void initialize(Context context, Context appContext) {
        // The strings below are how we reference the DB tables:
        TABLE_MAP.put("genericWorkout", genericWorkout);
        AppContext = appContext;
        dbsqlHelper = new DBSQLHelper(context);
        database = dbsqlHelper.getWritableDatabase();

        //TODO This is a debug line to destroy database on each launch
        // Comment out to enable data persistence between sessions
        dbsqlHelper.onUpgrade(database, 1, 1);
    }

    //Returns a cursor object containing the query results for the given date
    //Includes id of the row
    //Returns null if no data found or tablename incorrect
    public static Cursor select(String tablename, String date, String order) {
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null) {
            Log.d("SQLAccess", "No results");
            return null;
        } String selection = "Date = ?";
        String[] selectionArgs = {date};
        return database.query(
                tablename,
                table.getColumns(),
                selection,
                selectionArgs,
                null,
                null,
                "Date " + order);
    }

    //past week data
    public static Cursor selectPastWeek(String tablename, String order) {
        Log.d("SQLAccess", "Querying past week");
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null) {
            Log.d("SQLAccess", "No results");
            return null;
        } String selection = "Date BETWEEN date('now', '-7 days') AND date('now')";
        return database.query(
                tablename,
                table.getColumns(),
                selection,
                null,
                null,
                null,
                "Date " + order );
    }

    //past month data
    public static Cursor selectPastMonth(String tablename, String order) {
        Log.d("SQLAccess", "Querying past month");
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null) {
            Log.d("SQLAccess", "No results");
            return null;
        } String selection = "Date BETWEEN date('now', '-1 month') AND date('now')";
        return database.query(
                tablename,
                table.getColumns(),
                selection,
                null,
                null,
                null,
                "Date " + order );
    }

    //past 6 month data
    public static Cursor selectPast6Months(String tablename, String order) {
        Log.d("SQLAccess", "Querying past 6 months");
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null) {
            Log.d("SQLAccess", "No results");
            return null;
        } String selection = "Date BETWEEN date('now', '-6 months') AND date('now')";
        return database.query(
                tablename,
                table.getColumns(),
                selection,
                null,
                null,
                null,
                "Date " + order );
    }

    //past year data
    public static Cursor selectPastYear(String tablename, String order) {
        Log.d("SQLAccess", "Querying past year");
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null) {
            Log.d("SQLAccess", "No results");
            return null;
        } String selection = "Date BETWEEN date('now', '-1 year') AND date('now')";
        return database.query(
                tablename,
                table.getColumns(),
                selection,
                null,
                null,
                null,
                "Date " + order );
    }

    //Returns a cursor object containing all the rows of the database
    //Includes id of the row
    //Returns null if no data found or tablename incorrect
    public static Cursor selectAll(String tablename, String order) {
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null) {
            Log.d("SQLAccess", "No results");
            return null;
        } return database.query(
                tablename,
                table.getColumns(),
                null,
                null,
                null,
                null,
                "Date " + order);// either ASC or DESC
    }

    public static String selectID(String tablename, String[] values) {
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null) {
            return null;
        } String[] columns = table.getColumns();
        if(columns.length != values.length + 1) {
            return null;//Incorrect number of arguments
        } StringBuilder str = new StringBuilder();
        str.append(columns[1]).append(" = ?");
        for(int i = 2; i < columns.length; i++) {
            str.append(" and ").append(columns[i]).append(" = ?");
        } String selection = str.toString();
        Cursor cursor = database.query(
                tablename,
                table.getColumns(),
                selection,
                values,
                null,
                null,
                null);
        if(cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        assert cursor != null;
        cursor.close();
        return null;//Cursor incorrect
    }

    //Access the table of the provided name
    public static DBTable accessTable(String tablename) {
        return TABLE_MAP.get(tablename);
    }

    //Inserts data into the given table
    //Returns the long row id it was inserted into
    //A negative row id indicates failure to insert
    public static void insertData(String tablename, String[] values) {
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null) {
            return;//Table not found
        } ContentValues dict = table.getInsertValues(values);
        if(dict == null) {
            return;//Invalid number of column entries
        }
        database.insert(tablename, null, dict);
    }

    //Deletes data in the given database
    //Returns the long row id it was deleted from
    //A negative row id indicates failure to insert
    public static void deleteData(String tablename, String[] values)
    {
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null)
        {
            return;//Table not found
        }
        String id = selectID(tablename, values);//Select specific ID
        String selection = BaseColumns._ID + " = ?";
        database.delete(tablename, selection, new String[]{id});
    }

    //Replaces data in the given database
    //Returns the long row id it was inserted into
    //A negative row id indicates failure to insert
    public static void replaceData(String tablename, String[] oldValues, String[] newValues) {
        DBTable table = TABLE_MAP.get(tablename);
        if(table == null) {
            return;//Table not found
        } String id = selectID(tablename, oldValues);//Select specific ID
        String[] values = new String[newValues.length + 1];
        values[0] = id;
        System.arraycopy(newValues, 0, values, 1, values.length - 1);
        ContentValues dict = table.getInsertValuesWithID(values);
        if(dict == null) {
            return;//Invalid number of column entries
        }
        database.replace(tablename, null, dict);
    }

    public static String getTodayDate() {
        // Returns string of today's date in yyyy/mm/dd format
        String month = Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1);//MONTH 0 indexed
        if (month.length() < 2) {
            month = "0" + month;
        } String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        String day = Integer.toString(Calendar.getInstance().get(Calendar.DATE));
        if (day.length() < 2) {
            day = "0" + day;
        } return year + "-" + month + "-" + day;
    }

    //format date as YYYY/MM/DD unless specify otherwise
    public static String getFormattedDate(String dateString) {
       return dateString;
    }
}