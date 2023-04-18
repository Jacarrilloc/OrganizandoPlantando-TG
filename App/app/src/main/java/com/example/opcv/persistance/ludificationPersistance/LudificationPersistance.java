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
import java.text.DecimalFormat;
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

    public void addLikesFirebase(String docRef, String element){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    double likeNumber = documentSnapshot.getDouble("Likes");
                    DecimalFormat df = new DecimalFormat("#");
                    likeNumber++;
                    String formated = df.format(likeNumber);
                    ref.update("Likes", Integer.parseInt(formated)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Likes actualizados exitosamente!");//en el futuro cambiar a notificacion
                        }
                    });
                }
            }
        });
    }

    public void addDislikesFirebase(String docRef, String element){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    double dislikeNumber = documentSnapshot.getDouble("DisLikes");
                    DecimalFormat df = new DecimalFormat("#");
                    dislikeNumber++;
                    String formated = df.format(dislikeNumber);
                    ref.update("DisLikes", Integer.parseInt(formated)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Dislikes actualizados exitosamente!");//en el futuro cambiar a notificacion
                        }
                    });
                }
            }
        });
    }
    public void deductUserPoints(String docRef, int points, String element){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);
        CollectionReference ref2 = db.collection("UserInfo");
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String idPublisher = task.getResult().getString("Publisher");
                    if(idPublisher != null){
                        ref2.document(idPublisher).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    double levelUser = task.getResult().getDouble("Level");
                                    DecimalFormat df = new DecimalFormat("#");
                                    if(levelUser > 1){
                                        levelUser = levelUser-points;
                                        String formated = df.format(levelUser);
                                        ref2.document(idPublisher).update("Level", Integer.parseInt(formated)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d(TAG, "Nivel actualizado exitosamente!");//en el futuro cambiar a notificacion
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void tags(String element, String docRef, final GetTagsList callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<String> list = new ArrayList<>();
                    if(element.equals("Tools")){
                        String tag1, tag2, tag3;
                        boolean care, fertilizer, tool;
                        care = (boolean) task.getResult().get("Care");
                        fertilizer = (boolean) task.getResult().get("Fertilizer");
                        tool = (boolean) task.getResult().get("Tool");
                        if(care){
                            tag1 = "Ciudado";
                            list.add(tag1);
                        }
                        if(fertilizer){
                            tag2 = "Fertilizante";
                            list.add(tag2);
                        }
                        if(tool){
                            tag3 = "Herramienta";
                            list.add(tag3);
                        }
                    }
                    else if(element.equals("Plants")){
                        String tag1, tag2, tag3, tag4, tag5, tag6;
                        boolean edible, flower, fruit, medicine, petFriendly, precaution;
                        edible = (boolean) task.getResult().get("Edible");
                        flower = (boolean) task.getResult().get("GivesFlower");
                        fruit = (boolean) task.getResult().get("GivesFruit");
                        medicine = (boolean) task.getResult().get("Medicinal");
                        petFriendly = (boolean) task.getResult().get("PetFriendly");
                        precaution = (boolean) task.getResult().get("Precaution");
                        if(edible){
                            tag1 = "Comestible";
                            list.add(tag1);
                        }
                        if(flower){
                            tag2 = "Da flor";
                            list.add(tag2);
                        }
                        if(fruit){
                            tag3 = "Da fruto";
                            list.add(tag3);
                        }
                        if(medicine){
                            tag4 = "Medicinal";
                            list.add(tag4);
                        }
                        if(petFriendly){
                            tag5 = "Pet Friendly";
                            list.add(tag5);
                        }
                        if(precaution){
                            tag6 = "Precaución";
                            list.add(tag6);
                        }
                    }
                    callback.onComplete(list);
                }
            }
        });
    }

    public interface GetTagsList{
        void onComplete(ArrayList<String> list);
    }

    public void addComments(String element, String docRef, Map<String, Object> commentsInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ref.collection("Comments").add(commentsInfo);
                }
            }
        });
    }

    public void retrieveComments(String element, String docRef, final GetComments callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);
        ref.collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    HashMap<String, String> list = new HashMap<>();
                    String comments, publisherId;
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        comments = doc.getString("Comment");
                        publisherId = doc.getString("PublisherID");
                        list.put(comments,publisherId);
                    }
                    callback.onComplete(list);
                }
            }
        });
    }

    public interface GetComments{
        void onComplete(Map<String, String> mapComments);
    }

    public void getPublisherName(String idPublisher, final GetPublisher callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("UserInfo").document(idPublisher);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()){
                   callback.onComplete(task.getResult().getString("Name"));
               }
            }
        });
    }
    public interface GetPublisher{
        void onComplete(String name);
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
