package com.example.opcv.persistance.ludificationPersistance;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.ludificationScreens.ShowDictionaryItemActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public void searchPlantName(String idDocumentPlant, final GetPlantName callback){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Plants").document(idDocumentPlant);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String plantName;
                    plantName = documentSnapshot.getString("PlantName");
                    callback.onSuccess(plantName);
                }
            }
        });
    }
    public interface GetPlantName{
        void onSuccess(String name);
    }

    public void searchToolName(String idDocumentTool, final GetToolName callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Tools").document(idDocumentTool);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String toolName;
                toolName = documentSnapshot.getString("ToolName");
                callback.onSuccess(toolName);
            }
        });
    }

    public interface GetToolName{
        void onSuccess(String name);
    }

    public void searchPublisher(String id, final GetUserId callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("UserInfo").document(id);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name;
                    name = documentSnapshot.getString("Name")+ " "+documentSnapshot.getString("LastName");
                    callback.onSuccess(name);
                }
            }
        });
    }

    public interface GetUserId{
        void onSuccess(String name);
    }

    public void getLikesDislikes(String idDocumentTool, String element, Context context, final GetLikesDislikes callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(element.equals("Plants")){
            DocumentReference ref = db.collection("Plants").document(idDocumentTool);
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, String> listLikes = new HashMap<>();
                    String likes, dislikes, description;
                    if(documentSnapshot.exists()){
                        try{
                            likes = (String) documentSnapshot.get("Likes").toString();
                            dislikes = (String) documentSnapshot.get("DisLikes").toString();
                            description = documentSnapshot.getString("PlantDescription");
                            listLikes.put("Likes", likes);
                            listLikes.put("Dislikes", dislikes);
                            listLikes.put("Description", description);
                            callback.onSuccess(listLikes);
                        }catch (Exception e){
                            Toast.makeText(context, "No se pudo mostrar toda la información", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
        else if(element.equals("Tools")){
            DocumentReference ref = db.collection("Tools").document(idDocumentTool);
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, String> listLikes = new HashMap<>();
                    String likes, dislikes, description;
                    if(documentSnapshot.exists()){
                        try{
                            likes = (String) documentSnapshot.get("Likes").toString();
                            dislikes = (String) documentSnapshot.get("DisLikes").toString();
                            description = documentSnapshot.getString("ToolDescription");
                            listLikes.put("Likes", likes);
                            listLikes.put("Dislikes", dislikes);
                            listLikes.put("Description", description);
                            callback.onSuccess(listLikes);
                        }catch (Exception e){
                            Toast.makeText(context, "No se pudo mostrar toda la información", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    public interface GetLikesDislikes{
        void onSuccess(Map<String, String> publicLikes);
    }

    public void addLevelUser(String idUser, Map<String, Object> map){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("UserInfo").document(idUser);
        ref.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Nivel actualizado exitosamente!");//en el futuro cambiar a notificacion
            }
        });
    }
/*
    public void getDislikesUser(String idUser, final DeductLevel callback){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Plants");
        CollectionReference ref2 = db.collection("Plants");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    String publisherId;
                    double plantsDislikes=0;
                    for(QueryDocumentSnapshot q : queryDocumentSnapshots){
                        publisherId = q.getString("Publisher");
                        if(Objects.equals(publisherId, idUser)){
                            plantsDislikes=plantsDislikes+q.getDouble("DisLikes");
                        }
                    }
                    callback.onSuccess(plantsDislikes);
                }
            }
        });
        ref2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    String publisherId;
                    double toolsDislikes=0;
                    for(QueryDocumentSnapshot q : queryDocumentSnapshots){
                        publisherId = q.getString("Publisher");
                        if(Objects.equals(publisherId, idUser)){
                            toolsDislikes=toolsDislikes+q.getDouble("DisLikes");
                        }
                    }
                    callback.onSuccess1(toolsDislikes);
                }
            }
        });
    }
    public interface DeductLevel{
        void onSuccess(double plantsDislikes);
        void onSuccess1(double toolsDislikes);
    }

    public void deductLeveluser(String idUser, int points){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("UserInfo").document(idUser);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    try{
                        double currentLevel = documentSnapshot.getDouble("Level");
                        double newLevel = currentLevel-points;

                        ref.update("Level", newLevel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "Nivel actualizado exitosamente!");//en el futuro cambiar a notificacion
                            }
                        });
                    }catch (Exception e){
                        Log.d(TAG, "No se pudo actualizar");//en el futuro cambiar a notificacion
                    }

                }
            }
        });
    }*/
}
