package com.example.opcv.item_list;

public class ItemCollaboratorsRequest {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getIdGarden() {
        return idUser;
    }

    public void setIdGarden(String idGarden) {
        this.idUser = idGarden;
    }

    private String idUser;

    public ItemCollaboratorsRequest(String name, String idUser) {
        this.name = name;
        this.idUser = idUser;
    }
}