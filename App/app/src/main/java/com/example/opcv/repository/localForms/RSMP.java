package com.example.opcv.repository.localForms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RSMP")
public class RSMP {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "idForm")
    public int idForm;

    @ColumnInfo(name = "nameForm")
    public String nameForm;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "units")
    public String units;

    @ColumnInfo(name = "concept")
    public String concept;

    @ColumnInfo(name = "state")
    public String state;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "total")
    public int total;

    @ColumnInfo(name = "Date")
    public String Date;

    public RSMP() {
    }

    public RSMP(int idForm, String nameForm, String description, String units, String concept, String state, int quantity, int total, String date) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.description = description;
        this.units = units;
        this.concept = concept;
        this.state = state;
        this.quantity = quantity;
        this.total = total;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
