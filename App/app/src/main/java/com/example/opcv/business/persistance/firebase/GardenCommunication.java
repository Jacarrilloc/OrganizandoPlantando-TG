package com.example.opcv.business.persistance.firebase;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opcv.R;
import com.example.opcv.model.items.ItemGardenHomeList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class GardenCommunication {


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

    public interface OnGardenUserFilledListener {
        void onGardenUserFilled(List<ItemGardenHomeList> gardenNames);
    }

    private void fillGardenUser(String ID, OnGardenUserFilledListener listener) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference Ref = database.collection("Gardens");
        String userID = ID;
        Query query = Ref.whereEqualTo("ID_Owner", userID);
        query.whereEqualTo("ID_Owner", userID).addSnapshotListener(new EventListener<QuerySnapshot>() {

            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Se genero error: ", e);
                    return;
                }
                List<ItemGardenHomeList> gardenNames = new ArrayList<>();
                for(DocumentSnapshot documentSnapshot : value){
                    if(documentSnapshot.exists()){
                        String name = documentSnapshot.getString("GardenName");
                        String gardenId = documentSnapshot.getId();
                        GardenCommunication persistance = new GardenCommunication();
                        /*persistance.getGardenPicture(gardenId, this, new GardenPersistance.GetUri() {
                            @Override
                            public void onSuccess(String uri) {
                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, uri);
                                gardenNames.add(newItem);
                            }
                        });*/


                    } else {

                    }
                }
                listener.onGardenUserFilled(gardenNames);
            }
        });
    }
}