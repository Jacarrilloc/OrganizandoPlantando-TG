package com.example.opcv.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.opcv.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "databaseOfflineOPCV.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERINFO = "UserInfo";
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USERINFO + "(" +
                "Email TEXT, " +
                "Gender TEXT, " +
                "ID TEXT PRIMARY KEY, " +
                "LastName TEXT, " +
                "Name TEXT, " +
                "PhoneNumber TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_USERINFO);
        onCreate(sqLiteDatabase);
    }
}
