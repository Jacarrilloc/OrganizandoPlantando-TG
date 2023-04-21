package com.example.opcv.repository.localDaoForms;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.opcv.repository.localForms.RAC;

import java.util.List;

@Dao
public interface RACDao {

    @Insert
    void insert(RAC rac);

    @Update
    void update(RAC rac);

    @Query("DELETE FROM RAC WHERE idForm = :idForm")
    void deleteById(int idForm);

    @Query("SELECT * FROM RAC")
    List<RAC> getAllRACs();

    @Query("SELECT * FROM RAC WHERE idForm = :idForm")
    RAC getRACById(int idForm);

}

