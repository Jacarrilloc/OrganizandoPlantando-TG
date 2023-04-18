package com.example.opcv.repository.local_db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM UserInfo")
    List<User> getAllUsers();

    @Query("SELECT * FROM UserInfo WHERE id = :id")
    User getUserById(String id);

    @Query("SELECT * FROM UserInfo ORDER BY id ASC LIMIT 1")
    public User getFirstUser();

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}

