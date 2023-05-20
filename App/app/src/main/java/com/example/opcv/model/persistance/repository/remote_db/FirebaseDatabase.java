package com.example.opcv.model.persistance.repository.remote_db;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.opcv.model.persistance.repository.FormsRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDatabase implements FirebaseDatabaseI {

    private final FirebaseFirestore mFirestore;
    private boolean createJsonFormCalled = false;

    public FirebaseDatabase() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    public boolean isCreateJsonFormCalled() {
        return createJsonFormCalled;
    }

    @Override
    public void createInDatabase(String idGarden, Map<String,Object> infoForm){
        createJsonFormCalled = true;
        mFirestore.collection("Gardens").document(idGarden).collection("Forms").add(infoForm).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Log.i("BD_Firebase","Se Agrego a la base de datos de firebase la información");
            }
        });
    }

    public void updateInDatabase(String idGarden, Map<String, Object> newInfoForm) {
        createJsonFormCalled = true;
        String date = (String) newInfoForm.get("Date");
        String createdBy = (String) newInfoForm.get("CreatedBy");
        int idForm = (int) Math.floor(Double.parseDouble(newInfoForm.get("idForm").toString()));

        mFirestore.collection("Gardens")
                .document(idGarden)
                .collection("Forms")
                .whereEqualTo("Date", date)
                .whereEqualTo("CreatedBy", createdBy)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().update(newInfoForm);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Update-Firebase", "No fue posible actualizar en Firebase: " + e.getMessage());
                });
    }

    public void deleteInDatabase(String idGarden, Map<String, Object> infoForm) {
        createJsonFormCalled = true;
        String date = (String) infoForm.get("Date");
        String createdBy = (String) infoForm.get("CreatedBy");
        CollectionReference collectionReference = mFirestore.collection("Gardens").document(idGarden).collection("Forms");
        Query query = collectionReference.whereEqualTo("Date",date).whereEqualTo("CreatedBy",createdBy);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()){
                    document.getReference().delete().addOnSuccessListener(aVoid ->{
                        Log.i("DeleteFirebase","Se booro la informacion de Firebase");
                    }).addOnFailureListener(e -> {
                       Log.w("DeleteFirebase","Ocurrio un error al eliminar la Informacion:" + e.getMessage().toString());
                    });
                }
            }
        });
    }

    public void getAllInfoFormDatabase(String idGarden, FormsRepository.OnDataLoadedListener listener) {
        createJsonFormCalled = true;
        CollectionReference collectionReference = mFirestore.collection("Gardens").document(idGarden).collection("Forms");

        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<Map<String, Object>> result = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    Map<String, Object> data = documentSnapshot.getData();
                    result.add(data);
                }
                if (listener != null) {
                    try {
                        listener.onDataLoaded(result);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                Exception exception = task.getException();
                Log.i("FirebaseForms", "Error: " + exception.getMessage());
                if (listener != null) {
                    try {
                        listener.onDataLoaded(null);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
