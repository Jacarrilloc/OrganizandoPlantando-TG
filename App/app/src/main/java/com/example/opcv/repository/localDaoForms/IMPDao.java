package com.example.opcv.repository.localDaoForms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.opcv.repository.localForms.IMP;

import java.util.List;

@Dao
public interface IMPDao {

    @Insert
    void insert(IMP imp);

    @Query("SELECT * FROM IMP")
    List<IMP> getAllImps();

    @Query("SELECT * FROM IMP WHERE idForm = :id")
    IMP getImpById(int id);

    @Update
    void update(IMP imp);

    @Delete
    void delete(IMP imp);

}

