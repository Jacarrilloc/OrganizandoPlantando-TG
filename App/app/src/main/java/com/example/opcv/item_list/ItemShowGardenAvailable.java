package com.example.opcv.item_list;

public class ItemShowGardenAvailable {
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

    public ItemShowGardenAvailable(String name, String idGarden) {
        this.name = name;
        this.idGarden = idGarden;
    }
}
