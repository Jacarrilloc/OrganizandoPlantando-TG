package com.example.opcv.objects;

import java.util.HashMap;
import java.util.Map;

public class RCC_Element {
    int idForm;
    String nameForm;
    String areaRecipient;
    String areaDescription;
    String residueQuantity;
    String fertilizerQuantity;
    String leachedQuantity;

    public RCC_Element(int idForm, String nameForm, String areaRecipient, String areaDescription, String residueQuantity, String fertilizerQuantity, String leachedQuantity) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.areaRecipient = areaRecipient;
        this.areaDescription = areaDescription;
        this.residueQuantity = residueQuantity;
        this.fertilizerQuantity = fertilizerQuantity;
        this.leachedQuantity = leachedQuantity;
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

    public String getAreaRecipient() {
        return areaRecipient;
    }

    public void setAreaRecipient(String areaRecipient) {
        this.areaRecipient = areaRecipient;
    }

    public String getAreaDescription() {
        return areaDescription;
    }

    public void setAreaDescription(String areaDescription) {
        this.areaDescription = areaDescription;
    }

    public String getResidueQuantity() {
        return residueQuantity;
    }

    public void setResidueQuantity(String residueQuantity) {
        this.residueQuantity = residueQuantity;
    }

    public String getFertilizerQuantity() {
        return fertilizerQuantity;
    }

    public void setFertilizerQuantity(String fertilizerQuantity) {
        this.fertilizerQuantity = fertilizerQuantity;
    }

    public String getLeachedQuantity() {
        return leachedQuantity;
    }

    public void setLeachedQuantity(String leachedQuantity) {
        this.leachedQuantity = leachedQuantity;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idForm", idForm);
        map.put("nameForm", nameForm);
        map.put("areaRecipient", areaRecipient);
        map.put("areaDescription", areaDescription);
        map.put("residueQuantity", residueQuantity);
        map.put("fertilizerQuantity", fertilizerQuantity);
        map.put("leachedQuantity", leachedQuantity);
        return map;
    }
}

