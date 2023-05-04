package com.example.opcv.business.persistance.garden;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.opcv.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class GardenPersistance {
    int countGardens = 0;

    public void deletePhotoGarden(String gardenId){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference Ref = database.collection("Gardens").document(gardenId);
        Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String uri = task.getResult().getString("UriPath");
                    String path = gardenId+".jpg";
                    //if(uri != null){
                        StorageReference storage = FirebaseStorage.getInstance().getReference("gardenMainPhoto/"+path);
                        storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("borro");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("no borro");
                            }
                        });
                   // }
                }
            }
        });
    }


    public void addGardenPhoto(byte[] bytes, String gardenID, final GetUriGarden callback) {
        try {
            if (bytes != null) {
                StorageReference storage = FirebaseStorage.getInstance().getReference();
                String imageName = gardenID + ".jpg";
                StorageReference ref = storage.child("gardenMainPhoto/" + imageName);
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
        }catch (Exception e){
            Log.i("Error: ", e.getMessage());
        }
    }

    public void getGardenPicture(String id, Context context, final GetUri callback){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference Ref = database.collection("Gardens").document(id);
        Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String uri = documentSnapshot.getString("UriPath");
                    if(uri != null){
                        callback.onSuccess(uri);
                    }
                    else{
                        String imageString = "android.resource://" + context.getPackageName() + "/drawable/im_logo_ceres_green";
                        callback.onFailure(imageString);
                    }
                }
            }
        });
        //lo siguiente era como se hacia antes, producia el StorageException
/*
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("gardenMainPhoto/" +id + ".jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                callback.onSuccess(url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String imageString = "android.resource://" + context.getPackageName() + "/drawable/im_logo_ceres_green";
                callback.onFailure(imageString);
            }
        });*/
    }



    public interface GetUriGarden{
        void onSuccess(String uri);
    }
    public interface GetUri{
        void onSuccess(String uri);

        void onFailure(String imageString);
    }

    public void retrieveCrops(final GetNumber callback){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference Ref = database.collection("Gardens");

        Ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot q : task.getResult()){
                        if(q!=null){
                            Ref.document(q.getId()).collection("Forms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                    if(task1.isSuccessful()){
                                        for(QueryDocumentSnapshot q : task1.getResult()){
                                            String name = q.getString("nameForm");
                                            if(name.equals("Control de Procesos de Siembra")){
                                                countGardens++;
                                                callback.onSuccess(countGardens);
                                                break;
                                            }
                                        }

                                    }
                                }
                            });
                        }
                    }

                }
            }
        });

    }
    public interface GetNumber{
        void onSuccess(int count);
    }
}