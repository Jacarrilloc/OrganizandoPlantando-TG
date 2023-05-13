package com.example.opcv.model.items;

import java.util.Map;

public class ItemRegistersList {
    private String idGarden, formName, date;
    private Map<String,Object> info;

    public ItemRegistersList(String idGarden, String formName, Map<String,Object> info, String date) {
        this.idGarden = idGarden;
        this.formName = formName;
        this.date = date;
        this.info = info;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    public String getIdGarden() {
        return idGarden;
    }

    public void setIdGarden(String idGarden) {
        this.idGarden = idGarden;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
