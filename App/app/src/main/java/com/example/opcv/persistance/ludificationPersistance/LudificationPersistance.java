package com.example.opcv.persistance.ludificationPersistance;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.opcv.fbComunication.AuthUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LudificationPersistance implements Serializable {

    public void addPlantDictionary(Map<String, Object> plantInfo, Context context, String idUser){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AuthUtilities auth = new AuthUtilities();
        auth.getUserDocumentId(idUser, new AuthUtilities.GetUserDocument() {
            @Override
            public void onComplete(String idDocu) {
                System.out.println("xd: "+idDocu);
                DocumentReference documentReference = db.collection("UserInfo").document(idDocu);//Esto sera para añadir el nivel al usuario cuando se implemente
            }
        });

        CollectionReference ref = db.collection("Plants");
        try{
            ref.add(plantInfo);
            Toast.makeText(context, "Planta añadida con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Ocurrio un error al subir la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToolDictionary(Map<String, Object> plantInfo, Context context, String idUser){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AuthUtilities auth = new AuthUtilities();
        auth.getUserDocumentId(idUser, new AuthUtilities.GetUserDocument() {
            @Override
            public void onComplete(String idDocu) {
                System.out.println("xd: "+idDocu);
                DocumentReference documentReference = db.collection("UserInfo").document(idDocu);//Esto sera para añadir el nivel al usuario cuando se implemente
            }
        });
        CollectionReference ref = db.collection("Tools");
        try{
            ref.add(plantInfo);
            Toast.makeText(context, "Herramienta añadida con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Ocurrio un error al subir la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPlantElements(final GetPlants callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Plants");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Map<String, String> list = new HashMap<>();
                    String plantId, plantName;
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        plantId = doc.getId();
                        plantName = doc.getString("PlantName");
                        list.put( plantId, plantName);
                        //list.put("plantName", plantName);
                        callback.onComplete(list);
                    }
                    return;
                }
            }
        });
    }

    public interface GetPlants{
        void onComplete(Map<String, String> map);
    }

    public void getToolElements(final GetPlants callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Tools");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    HashMap<String, String> list = new HashMap<>();
                    String toolId, toolName;
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        toolId = doc.getId();
                        toolName = doc.getString("ToolName");
                        list.put( toolId, toolName);
                        //list.put("plantName", plantName);
                        callback.onComplete(list);
                    }

                    return;
                }
            }
        });
    }
/*
    public interface GetTools{
        void onComplete(HashMap<String, String> map);
    }*/
}
