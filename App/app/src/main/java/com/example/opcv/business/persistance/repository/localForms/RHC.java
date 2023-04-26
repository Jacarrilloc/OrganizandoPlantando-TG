package com.example.opcv.business.persistance.repository.localForms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RHC")
public class RHC {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "idForm")
    public int idForm;

    @ColumnInfo(name = "nameForm")
    public String nameForm;

    @ColumnInfo(name = "responsable")
    public String responsable;

    @ColumnInfo(name = "incomeExpense")
    public String incomeExpense;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "code")
    public String code;

    @ColumnInfo(name = "itemName")
    public String itemName;

    @ColumnInfo(name = "measurement")
    public String measurement;

    @ColumnInfo(name = "comments")
    public String comments;

    @ColumnInfo(name = "units")
    public String units;

    @ColumnInfo(name = "state")
    public String state;

    @ColumnInfo(name = "totalCost")
    public int totalCost;

    @ColumnInfo(name = "Date")
    public String Date;

    public RHC() {
    }

    public RHC(int idForm, String nameForm, String responsable, String incomeExpense, String type, String code, String itemName, String measurement, String comments, String units, String state, int totalCost, String date) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.responsable = responsable;
        this.incomeExpense = incomeExpense;
        this.type = type;
        this.code = code;
        this.itemName = itemName;
        this.measurement = measurement;
        this.comments = comments;
        this.units = units;
        this.state = state;
        this.totalCost = totalCost;
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

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getIncomeExpense() {
        return incomeExpense;
    }

    public void setIncomeExpense(String incomeExpense) {
        this.incomeExpense = incomeExpense;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
