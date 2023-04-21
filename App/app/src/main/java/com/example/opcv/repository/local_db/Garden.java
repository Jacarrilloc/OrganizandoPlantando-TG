package com.example.opcv.repository.local_db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Gardens")
public class Garden {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "ID_Owner")
    private String ID_Owner;

    @ColumnInfo(name = "GardenName")
    private String GardenName;

    @ColumnInfo(name = "InfoGarden")
    private String InfoGarden;

    @ColumnInfo(name = "GardenType")
    private String GardenType;

    public Garden() {}

    public Garden(String ID_Owner, String name, String info, String gardenType) {
        this.ID_Owner = ID_Owner;
        this.GardenName = name;
        this.InfoGarden = info;
        this.GardenType = gardenType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getID_Owner() {
        return ID_Owner;
    }

    public void setID_Owner(String ID_Owner) {
        this.ID_Owner = ID_Owner;
    }

    public String getGardenName() {
        return GardenName;
    }

    public void setGardenName(String gardenName) {
        GardenName = gardenName;
    }

    public String getInfoGarden() {
        return InfoGarden;
    }

    public void setInfoGarden(String infoGarden) {
        InfoGarden = infoGarden;
    }

    public String getGardenType() {
        return GardenType;
    }

    public void setGardenType(String gardenType) {
        GardenType = gardenType;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("ID_Owner", ID_Owner);
        map.put("GardenName", GardenName);
        map.put("InfoGarden", InfoGarden);
        map.put("GardenType", GardenType);
        return map;
    }

}

