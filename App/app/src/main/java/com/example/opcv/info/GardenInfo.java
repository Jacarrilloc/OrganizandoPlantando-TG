package com.example.opcv.info;

public class GardenInfo {
    String ID_Owner;
    String name;
    String info;
    String gardenType;

    public GardenInfo() {
    }

    public GardenInfo(String ID_Owner, String name, String info, String gardenType) {
        this.ID_Owner = ID_Owner;
        this.name = name;
        this.info = info;
        this.gardenType = gardenType;
    }

    public String getID_Owner() {
        return ID_Owner;
    }

    public void setID_Owner(String ID_Owner) {
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
