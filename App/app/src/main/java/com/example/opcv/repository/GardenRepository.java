package com.example.opcv.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.opcv.repository.local_db.Garden;
import com.example.opcv.repository.local_db.GardenDao;
import com.example.opcv.repository.local_db.GardenDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class GardenRepository {

    private GardenDao gardenDao;
    private CollectionReference gardenCollection;
    private MutableLiveData<Boolean> isNetworkConnected = new MutableLiveData<>();

    public GardenRepository(Context context) {
        GardenDatabase db = GardenDatabase.getInstance(context);
        gardenDao = db.gardenDao();
        gardenCollection = FirebaseFirestore.getInstance().collection("Gardens");

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isNetworkConnected.setValue(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public LiveData<List<Garden>> getAllGardens() {
        if (isNetworkConnected.getValue()) {
            // Si hay conexión a internet, obtener los datos de Firebase Firestore
            gardenCollection.whereEqualTo("collectionName", "Gardens").get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
                List<Garden> gardens = queryDocumentSnapshots.toObjects(Garden.class);
                // Actualizar la base de datos local con los datos obtenidos de Firebase Firestore
                GardenDatabase.databaseWriteExecutor.execute(() -> {
                    gardenDao.deleteAll();
                    gardenDao.insertAll(gardens);
                });
            });
        }
        // Devolver los datos de la base de datos local
        return gardenDao.getAllGardens();
    }

    public LiveData<List<Garden>> getGardensById(String ownerId) {
        /*if (isNetworkConnected.getValue()) {
            // Si hay conexión a internet, obtener los datos de Firebase Firestore
            gardenCollection.whereEqualTo("ID_Owner", ownerId).get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
                List<Garden> gardens = queryDocumentSnapshots.toObjects(Garden.class);
                // Actualizar la base de datos local con los datos obtenidos de Firebase Firestore
                GardenDatabase.databaseWriteExecutor.execute(() -> {
                    gardenDao.deleteAll();
                    gardenDao.insertAll(gardens);
                });
            });
        }*/
        // Devolver los datos de la base de datos local
        return gardenDao.getGardensById(ownerId);
    }

    public LiveData<Garden> getGarden(String gardenId) {
        return gardenDao.getGarden(gardenId);
    }

    public void addGarden(Garden garden) {
        /*if (isNetworkConnected.getValue()) {
            // Si hay conexión a internet, agregar el jardín a Firebase Firestore
            gardenCollection.add(garden);
        }*/

        // Agregar el jardín a la base de datos local
        GardenDatabase.databaseWriteExecutor.execute(() -> {
            gardenDao.insert(garden);
        });
    }

    public void updateGarden(Garden garden) {
        if (isNetworkConnected.getValue()) {
            // Si hay conexión a internet, actualizar el jardín en Firebase Firestore
            gardenCollection.document(garden.getID_Owner()).set(garden);
        }

        // Actualizar el jardín en la base de datos local
        GardenDatabase.databaseWriteExecutor.execute(() -> {
            gardenDao.update(garden);
        });
    }
}
