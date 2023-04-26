package com.example.opcv.business.persistance.repository.localDaoForms;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.opcv.business.persistance.repository.localForms.RCC;

import java.util.List;

@Dao
public interface RCCDao  {
    @Insert
    void insert(RCC rcc);

    @Query("SELECT * FROM RCC")
    List<RCC> getAllRCCForms();

    @Query("SELECT * FROM RCC WHERE idForm = :id")
    RCC getRCCFormById(int id);

    @Query("DELETE FROM RCC WHERE idForm = :id")
    void deleteRCCFormById(int id);
}
