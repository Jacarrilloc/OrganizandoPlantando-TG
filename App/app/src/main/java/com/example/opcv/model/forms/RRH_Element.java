package com.example.opcv.model.forms;

import java.util.HashMap;
import java.util.Map;

public class RRH_Element {
    int idForm;
    String nameForm;
    String description;
    String concept;
    String performedBy;
    String toolStatus;
    String toolQuantity;


    public RRH_Element(int idForm, String nameForm, String description, String concept, String performedBy, String toolStatus, String toolQuantity) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.description = description;
        this.concept = concept;
        this.performedBy = performedBy;
        this.toolStatus = toolStatus;
        this.toolQuantity = toolQuantity;
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

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getToolStatus() {
        return toolStatus;
    }

    public void setToolStatus(String toolStatus) {
        this.toolStatus = toolStatus;
    }

    public String getToolQuantity() {
        return toolQuantity;
    }

    public void setToolQuantity(String toolQuantity) {
        this.toolQuantity = toolQuantity;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idForm", idForm);
        map.put("nameForm", nameForm);
        map.put("description", description);
        map.put("concept", concept);
        map.put("performedBy", performedBy);
        map.put("toolStatus", toolStatus);
        map.put("toolQuantity", toolQuantity);

        return map;
    }
}