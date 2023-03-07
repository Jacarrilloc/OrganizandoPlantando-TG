package com.example.opcv.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.fbComunication.AuthUtilities;

public class LoginActivity extends AppCompatActivity {

    private Button registerButtom,loginButtom;
    private EditText emailLogin,passwordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.editTextTextEmailAddress);
        passwordLogin = findViewById(R.id.editTextTextPassword);

        registerButtom = findViewById(R.id.registerButtom);
        loginButtom = findViewById(R.id.buttonLoginActivity);

        loginButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInfo = emailLogin.getText().toString();
                String passwordInfo = passwordLogin.getText().toString();
                logginUser(emailInfo,passwordInfo,LoginActivity.this);
            }
        });

        registerButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterProfileActivity.class));
            }
        });
    }

    private void logginUser(String email, String password, Context context){
        AuthUtilities auth = new AuthUtilities();
        auth.loginUserVerify(email, password, context, new AuthUtilities.LoginCallback() {
            @Override
            public void onLogin(boolean success) {
                if(success){
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }else{
                    Toast.makeText(context, "No fue posible Iniciar Sesion, Por Favor revise si ingresó correctamente el correo y la contraseña", Toast.LENGTH_SHORT).show();
                    emailLogin.setText("");
                    passwordLogin.setText("");
                }
            }
        });
    }
}