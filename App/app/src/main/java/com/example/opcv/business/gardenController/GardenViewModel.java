package com.example.opcv.business.gardenController;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.repository.GardenRepository;
import com.example.opcv.repository.local_db.Garden;

import java.util.List;

public class GardenViewModel extends AndroidViewModel {
    private GardenRepository gardenRepository;
    private LiveData<List<Garden>> gardensUser;

    public GardenViewModel(@NonNull Application application) {
        super(application);
        gardenRepository = new GardenRepository(application);
        gardensUser = gardenRepository.getGardensById(new AuthUtilities().getCurrentUserUid());
    }

    public LiveData<List<Garden>> getGardensUser() {
        return gardensUser;
    }
}
