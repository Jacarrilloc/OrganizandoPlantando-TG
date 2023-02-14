package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewToAppActivity extends AppCompatActivity {

    Button createAcount;
    TextView goToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_app);

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
}