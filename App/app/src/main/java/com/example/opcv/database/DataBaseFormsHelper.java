package com.example.opcv.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseFormsHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "offline_Database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERINFO = "UserInfo";

    String UserInfoTable = "CREATE TABLE " + TABLE_USERINFO + "(" +
            "ID TEXT PRIMARY KEY, " +
            "Name TEXT, " +
            "LastName TEXT, " +
            "Email TEXT, " +
            "Gender TEXT, " +
            "PhoneNumber TEXT," +
            "Level INTEGER)";

    public DataBaseFormsHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UserInfoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERINFO);
        sqLiteDatabase.execSQL(TABLE_USERINFO);
    }
}
