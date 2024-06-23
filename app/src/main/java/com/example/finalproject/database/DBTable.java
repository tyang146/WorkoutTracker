package com.example.finalproject.database;

import android.content.ContentValues;
import android.provider.BaseColumns;

//Defines a schema for a database table
public class DBTable {
    private final String _tableName;
    private final int _numColumns;
    private final String[] columns;
    private final String[] datatype;

    //TODO add a throws statement for when the number of values don't match
    public DBTable(String tableName, int numColumns, String[] columnNames, String[] datatype)
    {
        _tableName = tableName;
        _numColumns = numColumns;
        this.columns = columnNames.clone();
        this.datatype = datatype.clone();
    }

    //Returns a query for a new table in the database
    public String createTable()
    {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS ").append(_tableName).append(" (").append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        for(int i = 0; i < _numColumns; i++)
        {
            query.append(columns[i]).append(" ").append(datatype[i]);
            if(i == _numColumns -1)
            {
                query.append(")");
            }
            else
            {
                query.append(", ");
            }
        }
        return query.toString();
    }

    //return name of table
    public String getTableName()
    {
        return _tableName;
    }

    //Returns the ContentValues necessary for a database.insert operation
    public ContentValues getInsertValues(String[] values)
    {
        if(values.length != _numColumns)
        {
            return null;//Number of values does not match number of columns
        }
        ContentValues dict = new ContentValues();
        for(int i = 0; i < _numColumns; i++)
        {
            dict.put(columns[i], values[i]);
        }
        return dict;
    }

    //Returns the ContentValues necessary for a database.replace operation
    public ContentValues getInsertValuesWithID(String[] values)
    {
        if(values.length != _numColumns + 1)
        {
            return null;//Number of values does not match number of columns
        }
        ContentValues dict = new ContentValues();
        dict.put(BaseColumns._ID, values[0]);
        for(int i = 1; i < _numColumns+1; i++)
        {
            dict.put(columns[i-1], values[i]);
        }
        return dict;
    }

    //Returns the columns in the table
    //Include the row id
    public String[] getColumns()
    {
        String[] ret = new String[_numColumns +1];
        ret[0] = BaseColumns._ID;
        if (_numColumns >= 0) System.arraycopy(columns, 0, ret, 1, _numColumns);
        return ret;
    }
}