package com.example.opcv.model.persistance.firebase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.Map;
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

    public String getUserFullName() {
        AuthCommunication info = new AuthCommunication();
        String idUser = info.getCurrentUserUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("UserInfo").document(idUser);
        Task<DocumentSnapshot> task = ref.get();
        while (!task.isComplete()) {
            // Espera hasta que se complete la tarea
        }
        if (task.isSuccessful()) {
            String firstName = task.getResult().getString("Name");
            String lastName = task.getResult().getString("LastName");
            String fullName = firstName + " " + lastName;
            return fullName;
        } else {
            return "";
        }
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
    //este metodo es para crear una coleccion en el usuario para que si le dio a 'unirse' a una huerta, se deshabilite esa opcion para despues
    public void addUserRequests(String idUser, Map<String, Object> map){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserInfo").document(idUser).collection("UserGardenRequests").add(map);
    }

    public void userAlreadyRequested(String idUser, String idGarden, final GetUserRequest callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("UserInfo").document(idUser).collection("UserGardenRequests");

        Query query = ref.whereEqualTo("Garden", idGarden);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot.isEmpty()){
                        callback.onComplete(false);
                    }
                    else{
                        callback.onComplete(true);
                    }
                }
            }
        });
    }
    public interface GetUserRequest{
        void onComplete(Boolean response);
    }

}
