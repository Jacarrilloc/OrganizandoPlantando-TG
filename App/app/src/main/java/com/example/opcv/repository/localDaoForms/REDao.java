package com.example.opcv.repository.localDaoForms;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.opcv.repository.localForms.RE;

import java.util.List;

@Dao
public interface REDao {

    @Query("SELECT * FROM RE")
    List<RE> getAllForms();

    @Query("SELECT * FROM RE WHERE idForm = :id")
    RE getFormById(int id);

    @Insert
    void insertForm(RE reForm);

    @Query("DELETE FROM RE WHERE idForm = :id")
    void deleteFormById(int id);

}

