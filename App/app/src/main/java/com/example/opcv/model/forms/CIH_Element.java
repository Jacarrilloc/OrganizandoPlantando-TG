package com.example.opcv.model.forms;

import java.util.HashMap;
import java.util.Map;

public class CIH_Element {
    int idForm;
    String nameForm;
    String tool;
    String concept;
    String incomingOutgoing;
    int toolQuantity;
    String toolStatus;
    int existenceQuantity;

    public CIH_Element(int idForm, String nameForm, String tool, String concept, String incomingOutgoing, int toolQuantity, String toolStatus, int existenceQuantity) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.tool = tool;
        this.concept = concept;
        this.incomingOutgoing = incomingOutgoing;
        this.toolQuantity = toolQuantity;
        this.toolStatus = toolStatus;
        this.existenceQuantity = existenceQuantity;
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idForm", idForm);
        map.put("nameForm", nameForm);
        map.put("tool", tool);
        map.put("concept", concept);
        map.put("incomingOutgoing", incomingOutgoing);
        map.put("toolQuantity", toolQuantity);
        map.put("toolStatus", toolStatus);
        map.put("existenceQuantity", existenceQuantity);
        return map;
    }
}
