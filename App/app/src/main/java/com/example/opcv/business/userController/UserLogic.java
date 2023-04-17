package com.example.opcv.business.userController;

import android.content.Context;

import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.repository.local_db.AppDatabase;
import com.example.opcv.repository.local_db.User;
import com.example.opcv.repository.local_db.UserDao;

import java.util.Map;

public class UserLogic {
    public void createUserRoomDb(String id, String name, String lastName, String email, String phoneNumber, int level, String gender,Context context){
        AppDatabase db = AppDatabase.getDatabase(context);
        UserDao userDao = db.userDao();
        User newUser = new User(id,name,lastName,email,phoneNumber,level,gender);
        userDao.insertUser(newUser);
    }

    public Map<String, Object> getUserRoomDb(Context context){
        AppDatabase db = AppDatabase.getDatabase(context);
        UserDao userDao = db.userDao();
        User firstUser = userDao.getFirstUser();
        return firstUser.toMap();
    }

    public void updateUserRoomDb(String id, String name, String lastName, String email, String phoneNumber, int level, String gender,Context context){
        AppDatabase db = AppDatabase.getDatabase(context);
        UserDao userDao = db.userDao();
        User oldUser = userDao.getFirstUser();
        User newUser = new User(oldUser.getId(), name,lastName,email,phoneNumber,level,gender);
        userDao.updateUser(newUser);
    }
}
