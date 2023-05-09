package com.example.opcv.model.persistance.repository.remote_db;


import android.util.Log;

import androidx.annotation.NonNull;

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

    public void updateInDatabase(String idGarden,Map<String, Object> newInfoForm){
        String date = (String) newInfoForm.get("Date");
        String createdBy = (String) newInfoForm.get("CreatedBy");
        int idForm = Integer.parseInt(newInfoForm.get("idForm").toString());
        mFirestore.collection("Gardens").document(idGarden).collection("Forms").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (date.equals(documentSnapshot.getString("Date")) && createdBy.equals(documentSnapshot.getString("CreatedBy")) && idForm == documentSnapshot.getLong("idForm")) {
                            documentSnapshot.getReference().update(newInfoForm);
                        }
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Update-Firebase","No fue posible Actualizar en Firebase");
                });
    }

    public void deleteInDatabase(String idGarden, Map<String, Object> infoForm) {
        String date = (String) infoForm.get("Date");
        String createdBy = (String) infoForm.get("CreatedBy");
        int idForm = (int) infoForm.get("idForm");
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

    public void getAllFormsDatabase(String idGarden, String nameForm, onFormsLoaded listener) throws FileNotFoundException, JSONException {
        CollectionReference collectionReference = mFirestore.collection("Gardens").document(idGarden).collection("Forms");
        Query query = collectionReference.whereEqualTo("nameForm", nameForm);
        List<Map<String, Object>> infoResult = new ArrayList<>();
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for( QueryDocumentSnapshot info : querySnapshot){
                    Map<String, Object> infoData = info.getData();
                    infoResult.add(infoData);
                }
                try {
                    listener.FormsLoad(infoResult);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public interface onFormsLoaded{
        void FormsLoad(List<Map<String, Object>> infoResult) throws IOException, JSONException;
    }
}
