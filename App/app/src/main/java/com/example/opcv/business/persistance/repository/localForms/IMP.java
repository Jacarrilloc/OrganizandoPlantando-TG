package com.example.opcv.business.persistance.repository.localForms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "IMP")
public class IMP {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "idForm")
    public int idForm;

    @ColumnInfo(name = "nameForm")
    public String nameForm;

    @ColumnInfo(name = "rawMaterial")
    public String rawMaterial;

    @ColumnInfo(name = "concept")
    public String concept;

    @ColumnInfo(name = "movement")
    public String movement;

    @ColumnInfo(name = "quantityRawMaterial")
    public String quantityRawMaterial;

    @ColumnInfo(name = "units")
    public String units;

    @ColumnInfo(name = "existenceQuantity")
    public String existenceQuantity;

    @ColumnInfo(name = "Date")
    public String Date;

    public IMP() {
    }

    public IMP(int idForm, String nameForm, String rawMaterial, String concept, String movement, String quantityRawMaterial, String units, String existenceQuantity, String date) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.rawMaterial = rawMaterial;
        this.concept = concept;
        this.movement = movement;
        this.quantityRawMaterial = quantityRawMaterial;
        this.units = units;
        this.existenceQuantity = existenceQuantity;
        Date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(String rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public String getQuantityRawMaterial() {
        return quantityRawMaterial;
    }

    public void setQuantityRawMaterial(String quantityRawMaterial) {
        this.quantityRawMaterial = quantityRawMaterial;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getExistenceQuantity() {
        return existenceQuantity;
    }

    public void setExistenceQuantity(String existenceQuantity) {
        this.existenceQuantity = existenceQuantity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
