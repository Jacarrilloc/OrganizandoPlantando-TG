package com.example.opcv.repository.localDaoForms;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.opcv.repository.localForms.RSMP;

import java.util.List;

@Dao
public interface RSMPDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RSMP rsmp);

    @Query("SELECT * FROM RSMP")
    LiveData<List<RSMP>> getAllRSMPForms();

    @Query("SELECT * FROM RSMP WHERE idForm = :idForm")
    LiveData<RSMP> getRSMPFormById(int idForm);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(RSMP rsmp);

    @Delete
    void delete(RSMP rsmp);
}

