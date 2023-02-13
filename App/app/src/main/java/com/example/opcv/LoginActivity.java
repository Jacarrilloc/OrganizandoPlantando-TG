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

    EditText email,password;
    Button registerButtom,LogginButtom;

    private FirebaseAuth autentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        autentication = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        LogginButtom = findViewById(R.id.buttonLoginActivity);
        registerButtom = findViewById(R.id.registerButtom);

        registerButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterProfileActivity.class));
            }
        });

        LogginButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logginUser(email.getText().toString(),password.getText().toString());
            }
        });
    }

    private void  logginUser(String emailSt, String passwordSt){
        if(validate(emailSt,passwordSt)){
            autentication.signInWithEmailAndPassword(emailSt,passwordSt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i("INFO","TASK OK");
                        FirebaseUser user = autentication.getCurrentUser();
                        Log.i("INFO","LOGGEADO CORRECTO");
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    }else{
                        Log.i("INFO","LOGGEADO Fallido");
                    }
                }
            });
        }else{
            email.setText("");
            password.setText("");
        }
    }

    private boolean validate(String email,String password){
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
    private boolean validatePassword(String password){
        if(password.length() < 4)
            return false;
        return true;
    }

    private boolean validateEmail(String email){
        if(!email.contains("@") || !email.contains(".") || email.length() < 5)
            return false;
        return true;
    }
}