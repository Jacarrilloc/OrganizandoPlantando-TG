package com.example.opcv.business.persistance.repository.remote_db;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class FirebaseDatabase implements FirebaseDatabaseI {

    private final FirebaseFirestore mFirestore;

    public FirebaseDatabase() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void createInDatabase(String idGarden, Map<String,Object> infoForm){
        mFirestore.collection("Gardens").document(idGarden).collection("Forms").add(infoForm).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Log.i("BD_Firebase","Se Agrego a la base de datos de firebase la información");
            }
        });
    }

    public void deleteInDatabase(String idGarden, Map<String, Object> infoForm) {
        String date = (String) infoForm.get("Date");
        String createdBy = (String) infoForm.get("CreatedBy");
        int idForm = (int) Integer.parseInt((String) infoForm.get("idForm"));
        mFirestore.collection("Gardens").document(idGarden).collection("Forms").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if(date.equals(documentSnapshot.getString("Date")) && createdBy.equals(documentSnapshot.getString("CreatedBy")) && idForm == documentSnapshot.getLong("idForm")){
                            Log.i("resultDoc","Documento para borrar Encontado con el ID: " + documentSnapshot.getId());
                            mFirestore.collection("Gardens").document(idGarden).collection("Forms").document(documentSnapshot.getId()).delete();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al obtener los documentos", e);
                });
    }

}
