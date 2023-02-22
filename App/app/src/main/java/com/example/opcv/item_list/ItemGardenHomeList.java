package com.example.opcv.item_list;

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

    private String idGarden;

    public ItemGardenHomeList(String name, String idGarden) {
        this.name = name;
        this.idGarden = idGarden;
    }
}
