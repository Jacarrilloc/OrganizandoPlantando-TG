package com.example.opcv.item_list;

public class ItemRegistersList {
    public String idGarden, formName, idFormCollection;

    public ItemRegistersList(String idGarden, String formName, String idFormCollection) {
        this.idGarden = idGarden;
        this.formName = formName;
        this.idFormCollection = idFormCollection;
    }

    public String getIdFormCollection() {
        return idFormCollection;
    }

    public void setIdFormCollection(String idFormCollection) {
        this.idFormCollection = idFormCollection;
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
}
