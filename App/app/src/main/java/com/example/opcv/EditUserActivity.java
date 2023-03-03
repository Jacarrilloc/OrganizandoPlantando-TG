package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditUserActivity extends AppCompatActivity {
    private Button signOff, delete;
    private Button gardensMap, profile, myGardens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        signOff = (Button) findViewById(R.id.options);
        signOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, signOffActivity.class));
            }
        });

        delete = (Button) findViewById(R.id.options3);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, deleteAccountActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, EditUserActivity.class));
            }
        });
        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, HomeActivity.class));
            }
        });

        gardensMap = (Button) findViewById(R.id.globalMap);

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, MapsActivity.class));
            }
        });

    }
}