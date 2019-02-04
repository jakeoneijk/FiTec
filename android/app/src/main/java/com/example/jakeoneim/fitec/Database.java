package com.example.jakeoneim.fitec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    public Context context;

    public Database(Context context){
        this(context, "address", null, 1);
        this.context = context;
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void createTable(SQLiteDatabase db){

        if(!isExistsTable(db)){
            String query = " CREATE TABLE address ( ";
            query += " _id INTEGER PRIMARY KEY AUTOINCREMENT, ";
            query += "name VARCHAR, ";
            query += "number VARCHAR);";

            db.execSQL(query);
        }

    }
    public boolean isExistsTable(SQLiteDatabase db){

        String query = " SELECT COUNT(NAME) " +
                " FROM sqlite_master  " +
                " WHERE type='table' " +
                " AND name='address'; ";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToNext()){
            return cursor.getInt(0) > 0;
        }
        return false;
    }
    public boolean insertColumn(String name, String number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("number",number);

        db.insert("address",null,values);
        return true;
    }

    public boolean updateColumn(long id, String name, String number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("number",number);

        db.update("address",values,"_id="+id,null);
        return true;
    }
    public boolean deleteColumn(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("address","_id="+id,null);
        return true;
    }
}

