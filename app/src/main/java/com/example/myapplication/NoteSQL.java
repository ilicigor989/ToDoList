package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteSQL extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "notedb.db";
    public static final String DATABASE_TABLE = "note_table";
    public static final String COLUMN_1 = "ID";
    public static final String COLUMN_2 = "NOTE_TITLE";
    public static final String COLUMN_3 = "NOTE_DESCRIPTION";
    public static final String COLUMN_4 = "THUMBNSIL_PATH";
    public static final String COLUMN_5 = "NOTE_TYPE";


    public NoteSQL(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "
                + DATABASE_TABLE + "(" + COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_2 + " TEXT, " + COLUMN_3 + " TEXT, " + COLUMN_4 + " TEXT, " + COLUMN_5 + " BIT)");
    }

    public void insertDataTwo(String addTitle, String addDescription,String thumbnailPath, boolean addType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_2, addTitle);
        cv.put(COLUMN_3, addDescription);
        cv.put(COLUMN_4, thumbnailPath);
        cv.put(COLUMN_5, addType);

        db.insert(DATABASE_TABLE, null, cv);
    }
    public void insertDataOne(String addTitle, String addDescription, boolean addType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_2, addTitle);
        cv.put(COLUMN_3, addDescription);
        cv.put(COLUMN_5, addType);

        db.insert(DATABASE_TABLE, null, cv);
    }


    public void deleteRow(int get_ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DATABASE_TABLE+ " WHERE "+COLUMN_1+"='"+get_ID+"'");
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public Cursor getData() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        return data;
    }

    public int getID(){

        String selectQuery= "SELECT * FROM " + DATABASE_TABLE+" ORDER BY " + COLUMN_1 + " DESC LIMIT 2";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int id = 0;
        if(cursor.moveToFirst())
            id  =  cursor.getInt( cursor.getColumnIndex(COLUMN_1) );
        cursor.close();
        return id;
    }


}
