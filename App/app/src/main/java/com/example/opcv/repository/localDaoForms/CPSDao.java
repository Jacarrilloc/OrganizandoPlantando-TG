package com.example.opcv.repository.localDaoForms;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.opcv.repository.localForms.CPS;

import java.util.List;

@Dao
public interface CPSDao {

    @Insert
    void insertCPS(CPS cps);

    @Query("SELECT * FROM CPS")
    List<CPS> getAllCPSForms();

    @Query("SELECT * FROM CPS WHERE nameForm = :nameForm")
    CPS getCPSFormByName(String nameForm);

}
