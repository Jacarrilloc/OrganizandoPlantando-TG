package com.example.opcv.repository.localForms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "RAC")
public class RAC {

    @ColumnInfo(name = "idForm")
    public int idForm;

    @ColumnInfo(name = "nameForm")
    public String nameForm;

    @ColumnInfo(name = "containerSize")
    public String containerSize;

    @ColumnInfo(name = "wormsWeight")
    public String wormsWeight;

    @ColumnInfo(name = "humidity")
    public String humidity;

    @ColumnInfo(name = "amountOfWaste")
    public String amountOfWaste;

    @ColumnInfo(name = "collectedHumus")
    public String collectedHumus;

    @ColumnInfo(name = "amountLeached")
    public String amountLeached;

    @ColumnInfo(name = "Date")
    public String Date;

    public RAC(int idForm, String nameForm, String containerSize, String wormsWeight, String humidity, String amountOfWaste, String collectedHumus, String amountLeached, String date) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.containerSize = containerSize;
        this.wormsWeight = wormsWeight;
        this.humidity = humidity;
        this.amountOfWaste = amountOfWaste;
        this.collectedHumus = collectedHumus;
        this.amountLeached = amountLeached;
        Date = date;
    }

    public int getIdForm() {
        return idForm;
    }

    public void setIdForm(int idForm) {
        this.idForm = idForm;
    }

    public String getNameForm() {
        return nameForm;
    }

    public void setNameForm(String nameForm) {
        this.nameForm = nameForm;
    }

    public String getContainerSize() {
        return containerSize;
    }

    public void setContainerSize(String containerSize) {
        this.containerSize = containerSize;
    }

    public String getWormsWeight() {
        return wormsWeight;
    }

    public void setWormsWeight(String wormsWeight) {
        this.wormsWeight = wormsWeight;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getAmountOfWaste() {
        return amountOfWaste;
    }

    public void setAmountOfWaste(String amountOfWaste) {
        this.amountOfWaste = amountOfWaste;
    }

    public String getCollectedHumus() {
        return collectedHumus;
    }

    public void setCollectedHumus(String collectedHumus) {
        this.collectedHumus = collectedHumus;
    }

    public String getAmountLeached() {
        return amountLeached;
    }

    public void setAmountLeached(String amountLeached) {
        this.amountLeached = amountLeached;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
