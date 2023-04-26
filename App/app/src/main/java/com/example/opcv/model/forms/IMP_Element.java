package com.example.opcv.model.forms;

import java.util.HashMap;
import java.util.Map;

public class IMP_Element {
    int idForm;
    String nameForm;
    String personResponsable;
    String processPhase;
    String phaseDuration;
    String commentsObservations;
    String plantsOrSeeds;

    public IMP_Element(int idForm, String nameForm, String personResponsable, String processPhase, String phaseDuration, String commentsObservations, String plantsOrSeeds) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.personResponsable = personResponsable;
        this.processPhase = processPhase;
        this.phaseDuration = phaseDuration;
        this.commentsObservations = commentsObservations;
        this.plantsOrSeeds = plantsOrSeeds;
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idForm", idForm);
        map.put("nameForm", nameForm);
        map.put("personResponsable", personResponsable);
        map.put("processPhase", processPhase);
        map.put("phaseDuration", phaseDuration);
        map.put("plantsOrSeeds", plantsOrSeeds);
        map.put("commentsObservations", commentsObservations);
        return map;
    }
}
