package com.example.opcv.business.persistance.repository;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FirebaseDatabase {
    public void createInDatabase(String idGarden, Map<String,Object> infoForm){
        /*
        final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Gardens").document(idGarden).collection("Forms").add(infoForm).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (listener != null) {
                    listener.onFormInserted(task.getResult().getId());
                }
            } else {
                if (listener != null) {
                    listener.onFormInsertionError(task.getException());
                }
            }
        });
         */
    }
}
