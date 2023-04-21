package com.example.opcv.persistance.gardenPersistance;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.persistance.userPersistance.UserPersistance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class GardenPersistance {

    public void addGardenPhoto(byte[] bytes, String gardenID, final GetUriGarden callback) {
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

    public void getGardenPicture(String id, final GetUri callback){/*
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("gardenMainPhoto/" +id + ".jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                callback.onSuccess(url);
            }
        });*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Gardens").document(id);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    callback.onSuccess(documentSnapshot.getString("UriPath"));
                }
            }
        });
    }


    public interface GetUriGarden{
        void onSuccess(String uri);
    }
    public interface GetUri{
        void onSuccess(String uri);
    }
}