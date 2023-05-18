package com.example.opcv.model.persistance.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.opcv.model.entity.User;
import com.example.opcv.view.auth.EditUserActivity;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
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
            deleteDatabase(user.getUid());
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



    }
    //este metodo es para crear una coleccion en el usuario para que si le dio a 'unirse' a una huerta, se deshabilite esa opcion para despues
    public void addUserRequests(String idUser, Map<String, Object> map){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserInfo").document(idUser).collection("UserGardenRequests").add(map);
    }

    public void deleteDatabase(String idUser){
        //eliminar coleccion en firestore
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("UserInfo").whereEqualTo("ID", idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot query : task.getResult()){
                        String document = query.getId();
                        database.collection("UserInfo").document(document).delete();
                        break;
                    }
                }
            }
        });
      /*  database.collection("UserInfo").document(idUser).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("User borrado", "Se elimino la base de datos del user");
                }
            }
        });*/
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

    public void deleteUserCollections(String idUser){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("UserInfo").document(idUser);

        ref.collection("UserActionsPoints").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        doc.getReference().delete();
                    }
                    Log.i("UserActionsPoints", "Se elimino la coleccion de UserActionsPoints");
                }
            }
        });

        ref.collection("GardensCollaboration").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        doc.getReference().delete();
                    }
                    Log.i("GardensCollaboration", "Se elimino la coleccion de GardensCollaboration");
                }
            }
        });

        ref.collection("UserGardenRequests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        doc.getReference().delete();
                    }
                    Log.i("UserGardenRequests", "Se elimino la coleccion de UserGardenRequests");
                }
            }
        });
    }

    //metodos de EditUser
    private void changePhotoProfile(Boolean IsChangedPhoto, String userID_Recived, ImageView profilePhoto, Context context, final GetImageUri callback){
        if(IsChangedPhoto) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("userProfilePhoto/" + userID_Recived + ".jpg");
            profilePhoto.setDrawingCacheEnabled(true);
            Bitmap bitmap = profilePhoto.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream stream = new ByteArrayInputStream(baos.toByteArray());
            UploadTask uploadTask = storageRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            callback.onSuccess(url);
                            Toast.makeText(context, "Se Cambio la Foto de Perfil Exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // La carga de la foto ha fallado, manejar el error aquí
                }
            });
        }
    }

    public interface GetImageUri{
        void onSuccess(String uri);
    }

    public void editUserInfo(String name, String lastName, String phoneNumber, String userId, Boolean IsChangedPhoto, ImageView profilePhoto, Context context){
        FirebaseAuth autentication = FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        if(IsChangedPhoto){

            changePhotoProfile(IsChangedPhoto, userId, profilePhoto, context, new GetImageUri() {
                @Override
                public void onSuccess(String uri) {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Name", name);
                    userInfo.put("LastName", lastName);
                    userInfo.put("PhoneNumber", phoneNumber);
                    userInfo.put("UriPath", uri);
                    database.collection("UserInfo").document(userId).update(userInfo);
                }
            });
        }
        else{
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("Name", name);
            userInfo.put("LastName", lastName);
            userInfo.put("PhoneNumber", phoneNumber);
            database.collection("UserInfo").document(userId).update(userInfo);
        }
    }

    public void searchUserInfo(String userID_Recived, TextView userNameTV, EditText userName, EditText userLastName, EditText userEmail, EditText userPhone){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("UserInfo");
        Query query = collectionRef.whereEqualTo("ID", userID_Recived);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getData().get("Name").toString();
                        String email = document.getData().get("Email").toString();
                        String lastname = document.getData().get("LastName").toString();
                        String phoneNumber = document.getData().get("PhoneNumber").toString();
                        int level;
                        try {
                            level = (int) document.getData().get("Level");
                        }catch (Exception e){
                            level = 0;
                        }
                        User userActive =  new User(name, lastname, email, userID_Recived, phoneNumber,null,null, level);
                        userNameTV.setText(userActive.getName());
                        userName.setText(userActive.getName());
                        userLastName.setText(userActive.getLastName());
                        userEmail.setText("Comabaquinta");
                        userPhone.setText(userActive.getPhoneNumber());
                    }
                }
            }
        });
    }
}
