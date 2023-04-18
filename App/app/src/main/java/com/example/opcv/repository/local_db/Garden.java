package com.example.opcv.repository.local_db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Gardens")
public class Garden {
    @PrimaryKey
    @NonNull
    private String ID_Owner;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "info")
    private String info;

    @ColumnInfo(name = "gardenType")
    private String gardenType;

    public Garden(String ID_Owner, String name, String info, String gardenType) {
        this.ID_Owner = ID_Owner;
        this.name = name;
        this.info = info;
        this.gardenType = gardenType;
    }

    @NonNull
    public String getID_Owner() {
        return ID_Owner;
    }

    public void setID_Owner(@NonNull String ID_Owner) {
        this.ID_Owner = ID_Owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getGardenType() {
        return gardenType;
    }

    public void setGardenType(String gardenType) {
        this.gardenType = gardenType;
    }
}

