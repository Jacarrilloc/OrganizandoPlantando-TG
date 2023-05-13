package com.example.opcv.model.persistance.firebase;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.opcv.view.ludification.ShowDictionaryItemActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class LudificationCommunication implements Serializable {

    private static final int COD_STORAGE = 200;
    private static final int COD_IMAGE = 300;

    public void addPlantDictionary(Map<String, Object> plantInfo, Context context, String idUser){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AuthCommunication auth = new AuthCommunication();
        auth.getUserDocumentId(idUser, new AuthCommunication.GetUserDocument() {
            @Override
            public void onComplete(String idDocu) {
                DocumentReference documentReference = db.collection("UserInfo").document(idDocu);//Esto sera para añadir el nivel al usuario cuando se implemente
            }
        });

        CollectionReference ref = db.collection("Plants");
        try{
            ref.add(plantInfo);
            Toast.makeText(context, "Planta añadida con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Ocurrió un error al subir la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToolDictionary(Map<String, Object> plantInfo, Context context, String idUser){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AuthCommunication auth = new AuthCommunication();
        auth.getUserDocumentId(idUser, new AuthCommunication.GetUserDocument() {
            @Override
            public void onComplete(String idDocu) {
                DocumentReference documentReference = db.collection("UserInfo").document(idDocu);//Esto sera para añadir el nivel al usuario cuando se implemente
            }
        });
        CollectionReference ref = db.collection("Tools");
        try{
            ref.add(plantInfo);
            Toast.makeText(context, "Herramienta añadida con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Ocurrió un error al subir la información", Toast.LENGTH_SHORT).show();
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

    public void searchPublisher(String element, String docRef, final GetUserId callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String idPublisher;
                    idPublisher = documentSnapshot.getString("Publisher");
                    assert idPublisher != null;
                    DocumentReference ref2 = db.collection("UserInfo").document(idPublisher);
                    ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                String name = documentSnapshot.getString("Name")+" "+documentSnapshot.getString("LastName");
                                callback.onSuccess(name);
                            }
                        }
                    });
                }
            }
        });
    }


    public void searchPublisherLevel(String element, String docRef, final GetLevel callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String idPublisher;
                    idPublisher = documentSnapshot.getString("Publisher");
                    assert idPublisher != null;
                    DocumentReference ref2 = db.collection("UserInfo").document(idPublisher);
                    ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                String level = String.valueOf(Objects.requireNonNull(documentSnapshot.getDouble("Level")).intValue());
                                callback.onSuccess(level);
                            }
                        }
                    });
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

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    double likeNumber = documentSnapshot.getDouble("Level");
                    int points = (int) map.get("Level");
                    DecimalFormat df = new DecimalFormat("#");
                    likeNumber = likeNumber+points;
                    if(likeNumber > 100){
                        likeNumber = 100;
                    }
                    String formated = df.format(likeNumber);
                    ref.update("Level", Integer.parseInt(formated)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "likes actualizados exitosamente!");//en el futuro cambiar a notificacion
                        }
                    });
                }
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

    public void getPublisherID(String element, String docRef, final GetPublisherId callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    callback.onComplete(task.getResult().getString("Publisher"));
                }
            }
        });
    }

    public interface GetPublisherId{
        void onComplete(String userID);
    }

    public void getPublisherLevel(String idPublisher, final getPublisherLevel callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("UserInfo").document(idPublisher);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String level = String.valueOf(Objects.requireNonNull(task.getResult().getDouble("Level")).intValue());
                    callback.onComplete(level);
                }
            }
        });
    }

    public interface getPublisherLevel{
        void onComplete(String name);
    }

    //a continuacion se hace el manejo de las fotos
    public void addPhoto(String name, byte[] bytes, final GetURi callback){
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        String path = "plantPhoto/";
        Uri imageUrl;
        String photo = "photo";
        String idd;

        StorageReference ref = storage.child("ludificationImages/"+ UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        callback.onSuccess(url);
                    }
                });
            }
        });
    }
    public interface GetURi{
        void onSuccess(String uri);
    }

    public void getImage(String element, String docRef, final GetURi callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(element.equals("Plants")){
                        callback.onSuccess(documentSnapshot.getString("PlantImage"));
                    }
                    else{
                        callback.onSuccess(documentSnapshot.getString("ToolImage"));
                    }
                }
            }
        });
    }

    public interface GetLevel{
        void onSuccess(String level);
    }

    public void addUserActionsPoints(String idUser, Map<String, Object> map){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserInfo").document(idUser).collection("UserActionsPoints").add(map);
    }

    public void checkIfPlantOrToolExists(String element, String name, final CheckIfItemExists callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(element);

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String nameItem = null, nameLowerCase = name.toLowerCase();
                    int a=0, b=0;
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        if(element.equals("Plants")){
                            nameItem = doc.getString("PlantName").toLowerCase();
                        } else if (element.equals("Tools")) {
                            nameItem = doc.getString("ToolName").toLowerCase();
                        }
                        if(nameItem != null){
                            if(!nameItem.equals(nameLowerCase)){
                                b++;
                            }
                        }
                        a++;
                    }
                    //System.out.println("a: "+a+" b: "+b);
                    if(a != b){
                        callback.onComplete(true);
                    }
                    else{
                        callback.onComplete(false);
                    }
                }
            }
        });
    }

    public interface CheckIfItemExists{
        void onComplete(boolean resp);
    }

    public void deleteItemDictionary(String element, String docRef, String idLogged, Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(element).document(docRef);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String idOwner = task.getResult().getString("Publisher");
                    if(idOwner != null){
                        if(idOwner.equals(idLogged)){

                            ref.collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot doc : task.getResult()){
                                            ref.collection("Comments").document(doc.getId()).delete();
                                        }
                                        ref.delete();
                                        Toast.makeText(context, "Se ha borrado con exito.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(context, "No tienes permiso para eliminar, solo el publicador.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }


}
