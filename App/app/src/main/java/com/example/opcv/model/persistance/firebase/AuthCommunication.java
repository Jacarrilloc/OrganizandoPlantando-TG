package com.example.opcv.model.persistance.firebase;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.opcv.model.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AuthCommunication implements Serializable {

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
                        int level = Integer.parseInt(Objects.requireNonNull(document.getString("Level")));
                        User user = new User(name,lastName,email,id,phoneNumber,uriPath,gender, level);
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

    public void getUserDocumentId(String userId, final GetUserDocument callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String idCollection;
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        if(Objects.equals(doc.getString("ID"), userId)){
                            idCollection = doc.getId();
                            callback.onComplete(idCollection);
                            return;
                        }
                    }
                }
            }
        });
    }

    public interface GetUserDocument{
        void onComplete(String idDocu);
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

    public boolean createUser(String emailRegister, String passwordRegister, User newUserInfo,byte[] bytes, Context context) {
        if (ValidateInfo(emailRegister, passwordRegister,context)) {
            final boolean[] isUserCreated = {false};
            try{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailRegister, passwordRegister).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            newUserInfo.setId(user.getUid().toString());
                            newUserInfo.setUriPath(null);
                            addtoDataBase(newUserInfo.toMap(), bytes);
                            isUserCreated[0] = true;
                        }
                    }
                });
            }catch (Exception e){
                Log.i("ERROR CREAR CUENTA: ",e.getMessage().toString());
            }
            return isUserCreated[0];
        } else {
            return false;
        }
    }

    private void addtoDataBase(Map<String, Object> newUserInfo, byte[] bytes){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        database.collection("UserInfo").document(userId).set(newUserInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            addProfilePhoto(bytes, userId, new GetUriUser() {
                                @Override
                                public void onSuccess(String uri) {
                                    database.collection("UserInfo").document(userId).update("UriPath", uri);
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("No creo", "No se crea la base de datos");
            }
        });
    }

    public void addProfilePhoto(byte[] bytes, String userID, final GetUriUser callback) {
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        String imageName = userID + ".jpg";
        StorageReference ref = storage.child("userProfilePhoto/" + imageName);
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

    public void validateEmailAlreadyInUse(String email, final ValidateEmail callback){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Task<SignInMethodQueryResult> task = mAuth.fetchSignInMethodsForEmail(email);
        task.addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    List<String> signInMethod = result.getSignInMethods();
                    if(signInMethod != null && !signInMethod.isEmpty()) {
                        callback.onComplete(false);
                    }
                    else{
                        callback.onComplete(true);
                    }
                }
            }
        });

    }

    public void changePassword(String password, Context context){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(context, "La contraseña se actualizó exitosamente", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Ocurrio un error al cambiar la contraseña, Intente mas tarde", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface GetUriUser{
        void onSuccess(String uri);
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

    public interface ValidateEmail{
        void onComplete(boolean resp);
    }

    public void guestLogin(Context context){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Se ha ingresado como invitado", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Ocurrio un error al ingresar como invitado. Intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public FirebaseUser guestUser(){
        FirebaseAuth autentication = FirebaseAuth.getInstance();
        return autentication.getCurrentUser();
    }
}