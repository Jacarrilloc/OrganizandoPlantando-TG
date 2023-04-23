package com.example.opcv.repository.localDaoForms;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.opcv.repository.localForms.RRH;

import java.util.List;

@Dao
public interface RRHDao {
    @Insert
    void insert(RRH rrh);

    @Query("SELECT * FROM RRH")
    List<RRH> getAllRRHs();

    @Query("SELECT * FROM RRH WHERE idForm = :id")
    RRH getRRHById(int id);

    @Query("DELETE FROM RRH WHERE idForm = :id")
    void deleteRRHById(int id);

}
