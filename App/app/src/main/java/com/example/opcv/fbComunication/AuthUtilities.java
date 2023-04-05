package com.example.opcv.fbComunication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.opcv.info.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
/*import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;*/

import java.io.Serializable;
import java.text.Collator;
import java.util.Map;

public class AuthUtilities implements Serializable {

    public boolean loginStatus;

    interface OnDataAddedListener {
        void onDataAdded(boolean success);
    }

    public boolean isLogeed(){
        FirebaseAuth autentication = FirebaseAuth.getInstance();
        FirebaseUser currentUser = autentication.getCurrentUser();
        if(currentUser != null){
            return true;
        }else{
            return false;
        }
    }

    public interface GetUserCallback {
        void onSuccess(User user);
    }

    public void getActuallUserObject(GetUserCallback callback){
        String userID = getCurrentUserUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userInfoCollectionRef = db.collection("UserInfo");
        Query query = userInfoCollectionRef.whereEqualTo("id", userID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getString("name");
                        String lastName = document.getString("lastName");
                        String email = document.getString("email");
                        String id = userID;
                        String phoneNumber = document.getString("phoneNumber");
                        String uriPath = document.getString("UriPath");
                        String gender = document.getString("Gender");
                        User user = new User(name,lastName,email,id,phoneNumber,gender);
                        callback.onSuccess(user);
                        return;
                    }
                } else {
                    Log.d(TAG, "Error al obtener documentos: ", task.getException());
                }
            }
        });
    }

    public String getCurrentUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    public void loginUserVerify(String email, String password, Context context, final LoginCallback callback) {
        if (!ValidateInfo(email, password, context)) {
            callback.onLogin(false);
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        boolean loginStatus;
                        if (task.isSuccessful()) {
                            Log.i("INFO", "LOGGEADO CORRECTO");
                            FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
                            loginStatus = true;
                        } else {
                            String error = task.getException().getMessage();
                            Log.i("INFO", error);
                            loginStatus = false;
                        }
                        callback.onLogin(loginStatus);
                    }
                });
    }

    public interface LoginCallback {
        void onLogin(boolean success);
    }

    private boolean ValidateInfo(String email, String password,Context context) {
        boolean isValid = true;

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(context,"No se ha Ingresado el Email y la Contraseña",Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(context,"No se ha Ingresado el Email",Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(context,"No se ha Ingresado la Contraseña",Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (!validateEmail(email) || !validatePassword(password)) {
            isValid = false;
            if (!validateEmail(email) && !validatePassword(password)) {
                Toast.makeText(context,"Correo y Contraseña no Validos",Toast.LENGTH_LONG).show();
            } else if (!validateEmail(email)) {
                Toast.makeText(context,"Correo Ingresado no Valido",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context,"Contraseña Ingresada no Valida",Toast.LENGTH_LONG).show();
            }
        }
        return isValid;
    }

    public boolean createUser(String emailRegister, String passwordRegister, User newUserInfo,Uri image, Context context) {
        if (ValidateInfo(emailRegister, passwordRegister,context)) {
            final boolean[] isUserCreated = {false};
            try{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailRegister,passwordRegister)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                newUserInfo.setId(user.getUid().toString());
                                String dataPatch = addProfilePhoto(image,user.getUid().toString());
                                addtoDataBase(newUserInfo.toMap());
                                isUserCreated[0] = true;
                            }
                        });
            }catch (Exception e){

            }
            return isUserCreated[0];
        } else {
            return false;
        }
    }

    public boolean createUser(String email,String password){
        final boolean[] isUserCreated = {false};
        try{
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    isUserCreated[0] = true;
                }
            });
        }catch (Exception e){

        }
        return isUserCreated[0];
    }


    private boolean addtoDataBase(Map<String, Object> newUserInfo){
        final boolean[] result = {false};
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = database.collection("UserInfo");
        collectionReference.add(newUserInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                result[0] = true;
            }
        });
        return result[0];
    }

    public String addProfilePhoto(Uri imageProfile, String userID) {
        String status = "";
        if(imageProfile == null){
            return status;
        }

        String imageName = userID + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("userProfilePhoto/" + imageName);
        UploadTask uploadTask = storageRef.putFile(imageProfile);
        return status;
    }

    private boolean validateEmail(String email){
        if(!email.contains("@") || !email.contains(".") || email.length() < 5)
            return false;
        return true;
    }

    private boolean validatePassword(String password){
        if(password.length() < 4)
            return false;
        return true;
    }
}