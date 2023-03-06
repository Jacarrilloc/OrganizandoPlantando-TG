package com.example.opcv.fbComunication;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.opcv.auth.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthUtilities {

    private FirebaseAuth autentication;

    public boolean loginStatus;

    public boolean isLogeed(){
        autentication = FirebaseAuth.getInstance();
        if(autentication != null){
            return true;
        }else{
            return false;
        }
    }

    public static String getCurrentUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    public void loginUserVerify(String email, String password, Context context, final LoginCallback callback) {
        if (!ValidateInfo(email, password)) {
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

    private boolean ValidateInfo(String email,String password){
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                //Toast.makeText(this,"No se ha Ingresado el Email y la Contraseña",Toast.LENGTH_LONG).show();
            }else{
                if(TextUtils.isEmpty(email)){
                    //Toast.makeText(this,"No se ha Ingresado el Email",Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(password)){
                    //Toast.makeText(this,"No se ha Ingresado la Contraseña",Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }else{
            if(validateEmail(email) && validatePassword(password)){
                return true;
            }else{
                if(!validateEmail(email) && !validatePassword(password)) {
                    //Toast.makeText(this,"Correo y Contraseña no Validos",Toast.LENGTH_LONG).show();
                }else{
                    if(!validateEmail(email))
                        //Toast.makeText(this,"Correo Ingresado no Valido",Toast.LENGTH_LONG).show();
                    if(!validatePassword(password)){
                        //Toast.makeText(this,"Contraseña Ingresada no Valida",Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        }
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
