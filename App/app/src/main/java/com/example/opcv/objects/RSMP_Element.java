package com.example.opcv.objects;

import java.util.HashMap;
import java.util.Map;

public class RSMP_Element {
    int idForm;
    String nameForm;
    String description;
    String units;
    String concept;
    String state;
    int quantity;
    int total;

    public RSMP_Element(int idForm, String nameForm, String description, String units, int quantity,int total, String concept, String state) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.description = description;
        this.units = units;
        this.quantity = quantity;
        this.total = total;
        this.concept = concept;
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idForm", idForm);
        map.put("nameForm", nameForm);
        map.put("description", description);
        map.put("concept", concept);
        map.put("units", units);
        map.put("state", state);
        map.put("quantity", quantity);
        map.put("total", total);

        return map;
    }
}

