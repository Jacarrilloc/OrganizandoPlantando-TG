package com.example.opcv.objects;

import java.util.HashMap;
import java.util.Map;

public class RAC_Element {
    int idForm;
    String nameForm;
    String containerSize;
    String wormsWeight;
    String humidity;
    String amountOfWaste;
    String collectedHumus;
    String amountLeached;

    public RAC_Element(int idForm, String nameForm, String containerSize, String wormsWeight, String humidity, String amountOfWaste, String collectedHumus, String amountLeached) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.containerSize = containerSize;
        this.wormsWeight = wormsWeight;
        this.humidity = humidity;
        this.amountOfWaste = amountOfWaste;
        this.collectedHumus = collectedHumus;
        this.amountLeached = amountLeached;
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

    public String getContainerSize() {
        return containerSize;
    }

    public void setContainerSize(String containerSize) {
        this.containerSize = containerSize;
    }

    public String getWormsWeight() {
        return wormsWeight;
    }

    public void setWormsWeight(String wormsWeight) {
        this.wormsWeight = wormsWeight;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getAmountOfWaste() {
        return amountOfWaste;
    }

    public void setAmountOfWaste(String amountOfWaste) {
        this.amountOfWaste = amountOfWaste;
    }

    public String getCollectedHumus() {
        return collectedHumus;
    }

    public void setCollectedHumus(String collectedHumus) {
        this.collectedHumus = collectedHumus;
    }

    public String getAmountLeached() {
        return amountLeached;
    }

    public void setAmountLeached(String amountLeached) {
        this.amountLeached = amountLeached;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idForm", idForm);
        map.put("nameForm", nameForm);
        map.put("containerSize", containerSize);
        map.put("wormsWeight", wormsWeight);
        map.put("humidity", humidity);
        map.put("amountOfWaste", amountOfWaste);
        map.put("collectedHumus", collectedHumus);
        map.put("amountLeached", amountLeached);
        return map;
    }

}
