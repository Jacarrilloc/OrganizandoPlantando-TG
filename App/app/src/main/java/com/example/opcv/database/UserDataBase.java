package com.example.opcv.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.opcv.info.User;
import com.example.opcv.localDatabase.DatabaseHelper;

public class UserDataBase extends DataBaseFormsHelper{

    private Context context;
    private static final String TABLE_USERINFO = "UserInfo";

    public UserDataBase(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public boolean insertUserInfo(User user){
        boolean result = false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ID",user.getId());
        values.put("Name",user.getName());
        values.put("LastName",user.getLastName());
        values.put("Email",user.getEmail());
        values.put("Gender",user.getGender());
        values.put("PhoneNumber",user.getPhoneNumber());
        long i = db.insert(TABLE_USERINFO,null,values);
        if(i > 0){
            result = true;
        }
        return result;
    }
}
