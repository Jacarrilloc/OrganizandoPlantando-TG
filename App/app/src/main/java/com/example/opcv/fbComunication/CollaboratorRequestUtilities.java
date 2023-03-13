package com.example.opcv.fbComunication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opcv.info.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CollaboratorRequestUtilities {
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
        autentication = FirebaseAuth.getInstance();


        if(action){//action true= se acepto al solicitante, false= fue rechazado
            Map<String,Object> infoColab = new HashMap<>();
            infoColab.put("idCollaborator", idUSer);
            database.collection("Gardens").document(idGardenFb).collection("Collaborators").add(infoColab);

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

}
