package com.example.opcv.business.persistance.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
