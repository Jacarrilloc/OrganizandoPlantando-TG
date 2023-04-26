package com.example.opcv.model.forms;

import java.util.HashMap;
import java.util.Map;

public class SCMPH_Element {
    int idForm;
    String nameForm;
    String itemName;
    String item;
    String units;
    String total;
    int quantity;


    public SCMPH_Element(int idForm, String nameForm, String itemName, String item, String units, String total, int quantity) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.itemName = itemName;
        this.item = item;
        this.units = units;
        this.total = total;
        this.quantity = quantity;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idForm", idForm);
        map.put("nameForm", nameForm);
        map.put("itemName", itemName);
        map.put("item", item);
        map.put("units", units);
        map.put("total", total);
        map.put("quantity", quantity);

        return map;
    }
}
