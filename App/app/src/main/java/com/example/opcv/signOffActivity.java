package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class signOffActivity extends AppCompatActivity {
    private Button returnScreen, signOff, gardensMap, profile, myGarden;

    private FirebaseAuth autentication;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_off);

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signOffActivity.this, EditUserActivity.class));
            }
        });
        myGarden = (Button) findViewById(R.id.myGardens);
        myGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signOffActivity.this, HomeActivity.class));
            }
        });

        gardensMap = (Button) findViewById(R.id.gardens);

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signOffActivity.this, MapsActivity.class));
            }
        });

        returnScreen = (Button) findViewById(R.id.returnButton2);
        returnScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signOffActivity.this, EditUserActivity.class));
            }
        });

        signOff = (Button) findViewById(R.id.closeButton);
        signOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autentication.signOut();
                Intent intent = new Intent(signOffActivity.this, NewToAppActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}