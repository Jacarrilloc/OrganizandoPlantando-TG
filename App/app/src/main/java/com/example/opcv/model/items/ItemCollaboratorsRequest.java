package com.example.opcv.model.items;

public class ItemCollaboratorsRequest {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name, idUser, idGarden, uri;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ItemCollaboratorsRequest(String name, String idUser, String idGarden, String uri) {
        this.name = name;
        this.idUser = idUser;
        this.idGarden = idGarden;
        this.uri = uri;
    }
}