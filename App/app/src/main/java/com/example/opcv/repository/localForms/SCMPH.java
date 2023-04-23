package com.example.opcv.repository.localForms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SCMPH")
public class SCMPH {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "idForm")
    public int idForm;

    @ColumnInfo(name = "nameForm")
    public String nameForm;

    @ColumnInfo(name = "itemName")
    public String itemName;

    @ColumnInfo(name = "item")
    public String item;

    @ColumnInfo(name = "units")
    public String units;

    @ColumnInfo(name = "total")
    public String total;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "Date")
    public String Date;

    public SCMPH() {
    }

    public SCMPH(int idForm, String nameForm, String itemName, String item, String units, String total, int quantity, String date) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.itemName = itemName;
        this.item = item;
        this.units = units;
        this.total = total;
        this.quantity = quantity;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
