package com.example.opcv.repository.localForms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RCC")
public class RCC {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "idForm")
    public int idForm;

    @ColumnInfo(name = "nameForm")
    public String nameForm;

    @ColumnInfo(name = "areaRecipient")
    public String areaRecipient;

    @ColumnInfo(name = "areaDescription")
    public String areaDescription;

    @ColumnInfo(name = "residueQuantity")
    public String residueQuantity;

    @ColumnInfo(name = "fertilizerQuantity")
    public String fertilizerQuantity;

    @ColumnInfo(name = "leachedQuantity")
    public String leachedQuantity;

    @ColumnInfo(name = "Date")
    public String Date;

    public RCC() {
    }

    public RCC(int idForm, String nameForm, String areaRecipient, String areaDescription, String residueQuantity, String fertilizerQuantity, String leachedQuantity, String date) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.areaRecipient = areaRecipient;
        this.areaDescription = areaDescription;
        this.residueQuantity = residueQuantity;
        this.fertilizerQuantity = fertilizerQuantity;
        this.leachedQuantity = leachedQuantity;
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

    public String getAreaRecipient() {
        return areaRecipient;
    }

    public void setAreaRecipient(String areaRecipient) {
        this.areaRecipient = areaRecipient;
    }

    public String getAreaDescription() {
        return areaDescription;
    }

    public void setAreaDescription(String areaDescription) {
        this.areaDescription = areaDescription;
    }

    public String getResidueQuantity() {
        return residueQuantity;
    }

    public void setResidueQuantity(String residueQuantity) {
        this.residueQuantity = residueQuantity;
    }

    public String getFertilizerQuantity() {
        return fertilizerQuantity;
    }

    public void setFertilizerQuantity(String fertilizerQuantity) {
        this.fertilizerQuantity = fertilizerQuantity;
    }

    public String getLeachedQuantity() {
        return leachedQuantity;
    }

    public void setLeachedQuantity(String leachedQuantity) {
        this.leachedQuantity = leachedQuantity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}