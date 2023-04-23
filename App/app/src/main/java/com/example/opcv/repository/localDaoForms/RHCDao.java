package com.example.opcv.repository.localDaoForms;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.opcv.repository.localForms.RHC;

import java.util.List;

@Dao
public interface RHCDao {

    @Query("SELECT * FROM RHC")
    List<RHC> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RHC form);

    @Query("DELETE FROM RHC WHERE idForm = :id")
    void deleteById(int id);
}

