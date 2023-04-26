package com.example.opcv.model.items;

public class ItemGardenHomeList {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getIdGarden() {
        return idGarden;
    }

    public void setIdGarden(String idGarden) {
        this.idGarden = idGarden;
    }

    private String idGarden, uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ItemGardenHomeList(String name, String idGarden, String uri) {
        this.name = name;
        this.idGarden = idGarden;
        this.uri = uri;
    }
}
