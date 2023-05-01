package com.example.opcv.business.persistance.repository.remote_db;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
