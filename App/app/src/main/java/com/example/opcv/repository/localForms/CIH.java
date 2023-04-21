package com.example.opcv.repository.localForms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "CIH")
public class CIH {
    @ColumnInfo(name = "idForm")
    public int idForm;

    @ColumnInfo(name = "nameForm")
    public String nameForm;

    @ColumnInfo(name = "tool")
    public String tool;

    @ColumnInfo(name = "concept")
    public String concept;

    @ColumnInfo(name = "incomingOutgoing")
    public String incomingOutgoing;

    @ColumnInfo(name = "toolQuantity")
    public int toolQuantity;

    @ColumnInfo(name = "toolStatus")
    public String toolStatus;

    @ColumnInfo(name = "existenceQuantity")
    public int existenceQuantity;

    @ColumnInfo(name = "Date")
    public String Date;

    public CIH(int idForm, String nameForm, String tool, String concept, String incomingOutgoing, int toolQuantity, String toolStatus, int existenceQuantity, String date) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.tool = tool;
        this.concept = concept;
        this.incomingOutgoing = incomingOutgoing;
        this.toolQuantity = toolQuantity;
        this.toolStatus = toolStatus;
        this.existenceQuantity = existenceQuantity;
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

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getIncomingOutgoing() {
        return incomingOutgoing;
    }

    public void setIncomingOutgoing(String incomingOutgoing) {
        this.incomingOutgoing = incomingOutgoing;
    }

    public int getToolQuantity() {
        return toolQuantity;
    }

    public void setToolQuantity(int toolQuantity) {
        this.toolQuantity = toolQuantity;
    }

    public String getToolStatus() {
        return toolStatus;
    }

    public void setToolStatus(String toolStatus) {
        this.toolStatus = toolStatus;
    }

    public int getExistenceQuantity() {
        return existenceQuantity;
    }

    public void setExistenceQuantity(int existenceQuantity) {
        this.existenceQuantity = existenceQuantity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
