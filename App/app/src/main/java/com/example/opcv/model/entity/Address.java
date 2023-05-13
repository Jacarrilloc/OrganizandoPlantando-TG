package com.example.opcv.model.entity;

import org.osmdroid.util.GeoPoint;

public class Address {
    private String gardenName;
    private GeoPoint point;

    public Address(String gardenName, GeoPoint point) {
        this.gardenName = gardenName;
        this.point = point;
    }

    public String getGardenName() {
        return gardenName;
    }

    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }
}
