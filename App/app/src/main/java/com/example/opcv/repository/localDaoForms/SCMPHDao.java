package com.example.opcv.repository.localDaoForms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.opcv.repository.localForms.SCMPH;

import java.util.List;

@Dao
public interface SCMPHDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SCMPH scmph);

    @Query("SELECT * FROM SCMPH")
    List<SCMPH> getAllSCMPHForms();

    @Query("SELECT * FROM SCMPH WHERE idForm = :idForm")
    SCMPH getSCMPHFormById(int idForm);

    @Update
    void update(SCMPH scmph);

    @Delete
    void delete(SCMPH scmph);

}

