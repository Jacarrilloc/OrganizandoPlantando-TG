package com.example.opcv;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.opcv.auth.LoginActivity;
import com.example.opcv.auth.RegisterProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewToAppActivity extends AppCompatActivity {

    Button createAcount;
    TextView goToLogin;
    private FirebaseAuth autenticacion;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("¿Estás seguro de que quieres salir de Ceres?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        NewToAppActivity.super.onBackPressed();
                        finishAffinity();
                    }
                }).create().show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_app);

        autenticacion = FirebaseAuth.getInstance();

        goToLogin = findViewById(R.id.loginTextNewOnMapActivity);
        createAcount = findViewById(R.id.createProfileNewToAppActivity);

        goToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewToAppActivity.this, LoginActivity.class));
            }
        });
        createAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewToAppActivity.this, RegisterProfileActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = autenticacion.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(NewToAppActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}