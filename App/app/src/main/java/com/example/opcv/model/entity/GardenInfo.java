package com.example.opcv.model.entity;

public class GardenInfo {
    String ID_Owner;
    String name;
    String info;
    String gardenType;
    String address;

    public GardenInfo() {
    }

    public GardenInfo(String ID_Owner, String name, String info, String gardenType, String address) {
        this.ID_Owner = ID_Owner;
        this.name = name;
        this.info = info;
        this.gardenType = gardenType;
        this.address = address;
    }

    public String getID_Owner() {
        return ID_Owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
