package com.example.opcv.business.persistance.garden;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.opcv.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
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

    public void getGardenPicture(String id, Context context, final GetUri callback){
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
                Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getApplicationContext().getPackageName() + "/" + R.drawable.im_logo_ceres_green);
                callback.onSuccess(uri.toString());
            }
        });

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Gardens").document(id);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    callback.onSuccess(documentSnapshot.getString("UriPath"));
                }
            }
        });*/
    }


    public interface GetUriGarden{
        void onSuccess(String uri);
    }
    public interface GetUri{
        void onSuccess(String uri);
    }
}