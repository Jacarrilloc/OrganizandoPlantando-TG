package com.example.opcv.repository.localForms;

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

    @ColumnInfo(name = "personResponsable")
    public String personResponsable;

    @ColumnInfo(name = "processPhase")
    public String processPhase;

    @ColumnInfo(name = "phaseDuration")
    public String phaseDuration;

    @ColumnInfo(name = "commentsObservations")
    public String commentsObservations;

    @ColumnInfo(name = "plantsOrSeeds")
    public String plantsOrSeeds;

    @ColumnInfo(name = "Date")
    public String Date;

    public IMP() {
    }

    public IMP(int idForm, String nameForm, String personResponsable, String processPhase, String phaseDuration, String commentsObservations, String plantsOrSeeds, String date) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.personResponsable = personResponsable;
        this.processPhase = processPhase;
        this.phaseDuration = phaseDuration;
        this.commentsObservations = commentsObservations;
        this.plantsOrSeeds = plantsOrSeeds;
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

    public String getPersonResponsable() {
        return personResponsable;
    }

    public void setPersonResponsable(String personResponsable) {
        this.personResponsable = personResponsable;
    }

    public String getProcessPhase() {
        return processPhase;
    }

    public void setProcessPhase(String processPhase) {
        this.processPhase = processPhase;
    }

    public String getPhaseDuration() {
        return phaseDuration;
    }

    public void setPhaseDuration(String phaseDuration) {
        this.phaseDuration = phaseDuration;
    }

    public String getCommentsObservations() {
        return commentsObservations;
    }

    public void setCommentsObservations(String commentsObservations) {
        this.commentsObservations = commentsObservations;
    }

    public String getPlantsOrSeeds() {
        return plantsOrSeeds;
    }

    public void setPlantsOrSeeds(String plantsOrSeeds) {
        this.plantsOrSeeds = plantsOrSeeds;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
