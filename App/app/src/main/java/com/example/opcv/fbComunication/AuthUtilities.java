package com.example.opcv.fbComunication;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Map;

public class AuthUtilities implements Serializable {

    public boolean loginStatus;

    public boolean isLogeed(){
        FirebaseAuth autentication = FirebaseAuth.getInstance();
        FirebaseUser currentUser = autentication.getCurrentUser();
        if(currentUser != null){
            return true;
        }else{
            return false;
        }
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

    public boolean createUser(String emailRegister, String passwordRegister, Map<String, Object> newUserInfo, Context context) {
        if (ValidateInfo(emailRegister, passwordRegister,context)) {
            final boolean[] isUserCreated = {false};
            try{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailRegister,passwordRegister)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                newUserInfo.put("ID",user.getUid().toString());
                                addtoDataBase(newUserInfo);
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
