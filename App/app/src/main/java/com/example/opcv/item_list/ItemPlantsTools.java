package com.example.opcv.item_list;

public class ItemPlantsTools {
    private String name, id, uri;

    public ItemPlantsTools(String name, String id, String uriPhoto) {
        this.name = name;
        this.id = id;
        this.uri = uriPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
