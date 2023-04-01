package com.example.opcv.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.opcv.info.User;

import java.util.HashMap;
import java.util.Map;

public class DB_User extends  DatabaseHelper{

    private static final String TABLE_USERINFO = "UserInfo";
    Context context;
    public DB_User(Context context) {
        super(context);
        this.context = context;
    }

    public long insertUserInfo(Map<String,Object> userData){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ID",(String) userData.get("ID"));
        values.put("Name",(String) userData.get("Name"));
        values.put("LastName", (String) userData.get("LastName"));
        values.put("Gender", (String) userData.get("Gender"));
        values.put("Email", (String) userData.get("Email"));
        values.put("PhoneNumber", (String) userData.get("PhoneNumber"));

        long i = db.insert("UserInfo",null,values);
        db.close();
        return i;
    }

    public Map<String, Object> getUserInfo() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Map<String,Object> userData = new HashMap<>();

        String[] projection = {"Name", "LastName", "Gender", "Email", "PhoneNumber"};

        Cursor cursor = db.query("UserInfo", projection, null, null, null, null, null, "1");

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String lastName = cursor.getString(cursor.getColumnIndex("LastName"));
            String gender = cursor.getString(cursor.getColumnIndex("Gender"));
            String email = cursor.getString(cursor.getColumnIndex("Email"));
            String phoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"));

            userData.put("Name", name);
            userData.put("LastName", lastName);
            userData.put("Gender", gender);
            userData.put("Email", email);
            userData.put("PhoneNumber", phoneNumber);
        }
        cursor.close();
        db.close();
        return userData;
    }

    public void updateUserInfo(String name, String lastName, String phoneNumber) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("LastName", lastName);
        values.put("PhoneNumber", phoneNumber);

        db.update("UserInfo", values, "id=?", new String[] { "1" });
        Toast.makeText(context, "Cambio Exitoso", Toast.LENGTH_SHORT).show();
        db.close();
    }
}
