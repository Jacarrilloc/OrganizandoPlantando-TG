package com.example.opcv.business.persistance.firebase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class UserCommunication {

    public void getProfilePicture(String idUser, final GetUriUser callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("UserInfo").document(idUser);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    callback.onComplete(task.getResult().getString("UriPath"));
                }
            }
        });
    }

    public void getUserLevel(String idUser, final GetUserLvl callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("UserInfo").document(idUser);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String level = String.valueOf(Objects.requireNonNull(task.getResult().getDouble("Level")).intValue());
                    callback.onComplete(level);
                }
            }
        });
    }


    public interface GetUriUser{
        void onComplete(String uri);
    }

    public interface GetUserLvl{
        void onComplete(String lvl);
    }
    public void deleteUser(String idUser){
        //eliminar del authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.signOut();
        if(user != null){
            user.delete();
        }
        //eliminar primero la foto del storage
        String uri = idUser+".jpg";

        try{
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("userProfilePhoto/"+uri);
            storageReference.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND){
                        Log.i("No hay foto", e.getMessage());
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    storageReference.delete();
                }
            });
        }catch(Exception e){
            Log.i("No hay foto", e.getMessage());
        }

        //eliminar coleccion en firestore
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("UserInfo").document(idUser).delete();

    }
}
