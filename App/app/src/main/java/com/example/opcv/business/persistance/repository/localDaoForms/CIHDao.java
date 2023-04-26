package com.example.opcv.business.persistance.repository.localDaoForms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.opcv.business.persistance.repository.localForms.CIH;

@Dao
public interface CIHDao {
    @Insert
    void insert(CIH cih);

    @Query("SELECT * FROM CIH WHERE idForm = :id")
    CIH getById(int id);

    @Update
    void update(CIH cih);

    @Delete
    void delete(CIH cih);
}

