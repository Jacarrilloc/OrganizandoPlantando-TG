package com.example.opcv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button registerButtom,loginButtom;
    private EditText emailLogin,passwordLogin;
    private FirebaseAuth autentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        autentication = FirebaseAuth.getInstance();

        emailLogin = findViewById(R.id.editTextTextEmailAddress);
        passwordLogin = findViewById(R.id.editTextTextPassword);

        registerButtom = findViewById(R.id.registerButtom);
        loginButtom = findViewById(R.id.buttonLoginActivity);

        loginButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInfo = emailLogin.getText().toString();
                String passwordInfo = passwordLogin.getText().toString();
                logginUser(emailInfo,passwordInfo);
            }
        });

        registerButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterProfileActivity.class));
            }
        });
    }

    private void logginUser(String email,String password){

        if(ValidateInfo(email,password)){
            autentication.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i("INFO","LOGGEADO CORRECTO");
                        FirebaseUser usuarioActual = autentication.getCurrentUser();
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    }else{
                        String error = task.getException().getMessage();
                        Log.i("INFO",error);
                        Toast.makeText(LoginActivity.this,"No existe este Usuario Registrado, por favor cree una cuenta", Toast.LENGTH_LONG).show();
                        emailLogin.setText("");
                        passwordLogin.setText("");
                    }
                }
            });
        }else{
            Toast.makeText(this,"Información Incorrecta, Por Favor intentelo de nuevo",Toast.LENGTH_LONG).show();
            emailLogin.setText("");
            passwordLogin.setText("");
        }
    }

    private boolean ValidateInfo(String email,String password){
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Toast.makeText(this,"No se ha Ingresado el Email y la Contraseña",Toast.LENGTH_LONG).show();
            }else{
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(this,"No se ha Ingresado el Email",Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(this,"No se ha Ingresado la Contraseña",Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }else{
            if(validateEmail(email) && validatePassword(password)){
                return true;
            }else{
                if(!validateEmail(email) && !validatePassword(password))
                    Toast.makeText(this,"Correo y Contraseña no Validos",Toast.LENGTH_LONG).show();
                else{
                    if(!validateEmail(email))
                        Toast.makeText(this,"Correo Ingresado no Valido",Toast.LENGTH_LONG).show();
                    if(!validatePassword(password))
                        Toast.makeText(this,"Contraseña Ingresada no Valida",Toast.LENGTH_LONG).show();
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