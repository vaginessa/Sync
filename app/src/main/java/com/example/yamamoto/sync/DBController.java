package com.example.yamamoto.sync;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController  extends SQLiteOpenHelper {

    public DBController(Context applicationcontext) {
        super(applicationcontext, "user.db", null, 1);
    }
    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE user ( id INTEGER , name TEXT, PRIMARY KEY (Id))";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS user";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertValueToTable(String tableName,HashMap<String, String> queryValues) {
        int loopCount=0;
        String rowNames = new String();
        String values = new String();
        String updateValues= new String();

        for ( String key : queryValues.keySet() ) {
            if(loopCount!=0) {
                rowNames += ",";
                values += ",";
                updateValues+=",";
            }
            rowNames+=key;

            values+="'";
            values+=queryValues.get(key);
            values+="'";


            String str=String.format("%s='%s'",key,queryValues.get(key));
            updateValues+=str;
            loopCount++;
        }

        String query=String.format("insert or replace into %s(%s) values(%s)",
                tableName,rowNames,values);

        System.out.println("AAAAAAAAAAAAAAAAA-"+query);
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
        database.close();
    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM user";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                String id=cursor.getString(0);
                String name=cursor.getString(1);
                map.put("id", id);
                map.put("name", name);
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

}