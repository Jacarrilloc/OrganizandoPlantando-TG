package com.example.opcv.entity_classes;

public class GardenClass {
    String gardenName;
    String gardenInfo;
    String publicGarden;
    String idGarden;
    String idOwner;

    public GardenClass(String gardenName, String gardenInfo, String publicGarden,String idGarden, String idOwner){
        this.gardenInfo = gardenInfo;
        this.gardenName= gardenName;
        this.publicGarden = publicGarden;
        this.idGarden = idGarden;
        this.idOwner = idOwner;
    }

    public String getGardenName() {
        return gardenName;
    }

    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }

    public String getGardenInfo() {
        return gardenInfo;
    }

    public void setGardenInfo(String gardenInfo) {
        this.gardenInfo = gardenInfo;
    }

    public String getPublicGarden() {
        return publicGarden;
    }

    public void setPublicGarden(String publicGarden) {
        this.publicGarden = publicGarden;
    }

    public String getIdGarden() {
        return idGarden;
    }

    public void setIdGarden(String idGarden) {
        this.idGarden = idGarden;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }
}
