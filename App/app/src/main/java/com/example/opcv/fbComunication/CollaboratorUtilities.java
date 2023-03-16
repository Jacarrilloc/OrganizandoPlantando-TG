package com.example.opcv.fbComunication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.MotionEffect;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class CollaboratorUtilities {
    private FirebaseFirestore database, database2;
    private FirebaseAuth autentication;

    public void addRequests(Context context,String idUSer, String idGardenFb){
        database = FirebaseFirestore.getInstance();
        autentication = FirebaseAuth.getInstance();

        Map<String,Object> infoRequest = new HashMap<>();
        infoRequest.put("idUserRequest", idUSer);
        infoRequest.put("requestStatus", "SV");//Estados de solicitud: A: aceptado, R: Rechazado, SV: Sin verificar

        database.collection("Gardens").document(idGardenFb).collection("Requests").add(infoRequest);
    }

    public void acceptRequest(String idUSer, String idGardenFb, Boolean action){
        database = FirebaseFirestore.getInstance();
        database2 = FirebaseFirestore.getInstance();
        autentication = FirebaseAuth.getInstance();


        if(action){//action true= se acepto al solicitante, false= fue rechazado
            Map<String,Object> infoColab = new HashMap<>();
            infoColab.put("idCollaborator", idUSer);//AQUI HICE EL CAMBIO

            database.collection("Gardens").document(idGardenFb).collection("Collaborators").add(infoColab);
            searchUser(idUSer, idGardenFb);

           // database2.collection("UserInfo").document(idUSer).collection("GardensCollaboration").add(gardensPart);


            database.collection("Gardens").document(idGardenFb).collection("Requests").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        String idSearch;
                        for(QueryDocumentSnapshot document : task.getResult()){
                            idSearch = document.getData().get("idUserRequest").toString();
                            if(idSearch.equals(idUSer)){
                                final DocumentReference docRef = database.collection("Gardens").document(idGardenFb)
                                        .collection("Requests")
                                        .document(document.getId().toString());
                                database.runTransaction(new Transaction.Function<Void>() {
                                    @Nullable
                                    @Override
                                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                        transaction.update(docRef, "requestStatus", "A");
                                        return null;
                                    }
                                });
                            }
                        }
                    }
                }
            });



        }
        else{
            database.collection("Gardens").document(idGardenFb).collection("Requests").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                String idSearch;
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    idSearch = document.getData().get("idUserRequest").toString();
                                    if(idSearch.equals(idUSer)){
                                        final DocumentReference docRef = database.collection("Gardens").document(idGardenFb)
                                                .collection("Requests")
                                                .document(document.getId().toString());
                                        database.runTransaction(new Transaction.Function<Void>() {
                                            @Nullable
                                            @Override
                                            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                                transaction.update(docRef, "requestStatus", "R");
                                                return null;
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
        }
    }
    public void searchUser(String idUser, String idGarden){
        database = FirebaseFirestore.getInstance();
        database.collection("UserInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String idSearch;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        idSearch = (String) document.getData().get("ID");
                        if (idSearch == null) {
                            idSearch = (String) document.getData().get("id");
                        }
                        if(idSearch.equals(idUser)){
                            Map<String,Object> gardensPart = new HashMap<>();
                            gardensPart.put("idGardenCollab", idGarden);
                            database.collection("UserInfo").document(document.getId()).collection("GardensCollaboration").add(gardensPart);

                        }
                    }
                }
            }
        });
    }

    public void deleteUser(String idUser, String idGarden, Context context){
        database = FirebaseFirestore.getInstance();

        CollectionReference Ref = database.collection("Gardens").document(idGarden).collection("Collaborators");
        Query query = Ref.whereEqualTo("idCollaborator", idUser);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    documentSnapshot.getReference().delete();
                    Toast.makeText(context, "El colaborador fue eliminado correctamente ", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: ");
                // Ocurrió un error al intentar eliminar el colaborador
            }
        });


    }

    public void deleteGarden(String idUser, String idGarden){
        database = FirebaseFirestore.getInstance();
        database.collection("UserInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String userId;
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        userId = (String) document.getData().get("ID");
                        if (userId == null) {
                            userId = (String) document.getData().get("id");
                        }
                        if(userId.equals(idUser)){
                            String idCollection = document.getId().toString();
                            CollectionReference Ref =  database.collection("UserInfo").document(idCollection).collection("GardensCollaboration");
                            Query query = Ref.whereEqualTo("idGardenCollab", idGarden);
                            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        documentSnapshot.getReference().delete();
                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Error getting documents: ");
                                    // Ocurrió un error al intentar eliminar el colaborador
                                }
                            });

                        }
                    }
                }
            }
        });


    }

}
