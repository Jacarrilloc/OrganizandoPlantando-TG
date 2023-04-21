package com.example.opcv.business.gardenController;

import androidx.lifecycle.LiveData;
import android.app.Application;

import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.repository.local_db.Garden;
import com.example.opcv.repository.GardenRepository;


import java.util.List;
import java.util.Map;

public class GardenLogic {

    private Application application;
    public GardenLogic(Application application) {
        this.application = application;
    }

    public LiveData<List<Garden>> getGardensInfo() {
        GardenRepository gardenRepository = new GardenRepository(application);
        return gardenRepository.getAllGardens();
    }

    public LiveData<List<Garden>> getGardensUser(){
        GardenRepository gardenRepository = new GardenRepository(application);
        AuthUtilities user = new AuthUtilities();
        return gardenRepository.getGardensById(user.getCurrentUserUid());
    }

    public void createGarden(String name,String info,boolean type){
        if(validateField(name,info)){
            GardenRepository newInfo = new GardenRepository(application);
            AuthUtilities user = new AuthUtilities();
            String gardenType;
            if(type){
                gardenType = "Public";
            }else{
                gardenType = "Private";
            }
            Garden newInfoGarden = new Garden(user.getCurrentUserUid(), name,info,gardenType);
            newInfo.addGarden(newInfoGarden);
        }
    }

    private boolean validateField(String name,String info){

        if(name.isEmpty() || info.isEmpty()){
            return false;
        }
        return true;
    }
}