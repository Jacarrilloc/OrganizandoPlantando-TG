package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewToAppActivity extends AppCompatActivity {

    Button createAcount;
    TextView goToLogin;
    private FirebaseAuth autenticacion;
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
                startActivity(new Intent(NewToAppActivity.this,LoginActivity.class));
            }
        });
        createAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewToAppActivity.this,RegisterProfileActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = autenticacion.getCurrentUser();
        if(currentUser != null) {
            startActivity(new Intent(NewToAppActivity.this, HomeActivity.class));
        }
    }
}