package com.example.opcv.repository.local_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GardenDao {
    @Insert
    void insert(Garden garden);

    @Update
    void update(Garden garden);

    @Delete
    void delete(Garden garden);

    /*
    @Query("SELECT * FROM Gardens WHERE ID_Owner = :ID_Owner")
    Garden getGarden(String ID_Owner);*/

    @Query("SELECT * FROM Gardens")
    LiveData<List<Garden>> getAllGardens();

    @Query("DELETE FROM gardens")
    void deleteAll();

    @Insert
    void insertAll(List<Garden> gardens);

    @Query("SELECT * FROM Gardens WHERE ID_Owner = :ownerId")
    LiveData<List<Garden>> getGardensById(String ownerId);

    @Query("SELECT * FROM gardens WHERE id = :gardenId")
    LiveData<Garden> getGarden(String gardenId);
}
