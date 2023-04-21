package com.example.opcv.repository.localForms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "RRH")
public class RRH {

    @ColumnInfo(name = "idForm")
    public int idForm;

    @ColumnInfo(name = "nameForm")
    public String nameForm;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "concept")
    public String concept;

    @ColumnInfo(name = "performedBy")
    public String performedBy;

    @ColumnInfo(name = "toolStatus")
    public String toolStatus;

    @ColumnInfo(name = "toolQuantity")
    public String toolQuantity;

    @ColumnInfo(name = "Date")
    public String Date;

    public RRH(int idForm, String nameForm, String description, String concept, String performedBy, String toolStatus, String toolQuantity, String date) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.description = description;
        this.concept = concept;
        this.performedBy = performedBy;
        this.toolStatus = toolStatus;
        this.toolQuantity = toolQuantity;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
