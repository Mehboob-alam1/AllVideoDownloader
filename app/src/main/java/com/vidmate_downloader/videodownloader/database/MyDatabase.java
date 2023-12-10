package com.vidmate_downloader.videodownloader.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {

    private static final String DatabaseName = "database_name";

    private static final String TableName = "table_name";

    public MyDatabase(Context context) {
        super(context, DatabaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table " + TableName + "(id INTEGER PRIMARY KEY, txt TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }


    public void addData(String url) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("txt", url);
        sqLiteDatabase.insert(TableName, null, contentValues);
        sqLiteDatabase.close();
    }

    @SuppressLint("Range")
    public boolean isExist(String url) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TableName + " WHERE " + "txt" + "=?", new String[]{url});
        String txt = "";
        if (cursor.moveToNext()) {
            txt = cursor.getString(cursor.getColumnIndex("txt"));
        }
        cursor.close();
        sqLiteDatabase.close();


        return url.equals(txt);
    }

    @SuppressLint("Range")
    public String getLastUrl() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TableName+" ORDER BY "+"txt"+" DESC LIMIT 1;",null);
        String txt = "";
        if (cursor.moveToNext()) {
            txt = cursor.getString(cursor.getColumnIndex("txt"));
        }
        cursor.close();
        sqLiteDatabase.close();


        return txt;
    }

    public void removeData(String url) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TableName, "txt" + "=?", new String[]{url});
        sqLiteDatabase.close();
    }


    public void removeAllData() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TableName, null, null);
        sqLiteDatabase.close();
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllData() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TableName, null);
        if (cursor.moveToNext()) {
            while (!cursor.isAfterLast()) {
                String txt = cursor.getString(cursor.getColumnIndex("txt"));
                list.add(txt);
                cursor.moveToNext();
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

}
