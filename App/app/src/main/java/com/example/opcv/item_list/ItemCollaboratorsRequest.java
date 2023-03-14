package com.example.opcv.item_list;

public class ItemCollaboratorsRequest {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name, idUser, idGarden;

    public String getIdUser() {
        return idUser;
    }

    public void setIdGarden(String idGarden) {
        this.idUser = idGarden;
    }



    public String getIdGarden() {
        return idGarden;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public ItemCollaboratorsRequest(String name, String idUser, String idGarden) {
        this.name = name;
        this.idUser = idUser;
        this.idGarden = idGarden;
    }
}