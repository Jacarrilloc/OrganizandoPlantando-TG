package com.example.opcv.objects;

import java.util.HashMap;
import java.util.Map;

public class RHC_Element {
    int idForm;
    String nameForm;
    String responsable;
    String incomeExpense;
    String type;
    String code;
    String itemName;
    String measurement;
    String comments;
    String units;
    String state;

    int totalCost ;

    public RHC_Element(int idForm, String nameForm, String responsable, String incomeExpense, String type,
                       String code, String itemName, String measurement, int totalCost, String comments, String units, String state) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.responsable = responsable;
        this.incomeExpense = incomeExpense;
        this.type = type;
        this.code = code;
        this.itemName = itemName;
        this.measurement = measurement;
        this.totalCost = totalCost;
        this.comments = comments;
        this.units = units;
        this.state = state;
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idForm", idForm);
        map.put("nameForm", nameForm);
        map.put("responsable", responsable);
        map.put("incomeExpense", incomeExpense);
        map.put("type", type);
        map.put("code", code);
        map.put("itemName", itemName);
        map.put("units", units);
        map.put("measurement", measurement);
        map.put("comments", comments);
        map.put("state", state);
        map.put("totalCost", totalCost);

        return map;
    }
}

