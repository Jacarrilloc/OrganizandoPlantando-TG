package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    private Button otherGardensButton, profile;
    private ListView gardensUser;
    private FloatingActionButton nextArrow, addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Declaracion metodos de navegacion
        gardensUser = findViewById(R.id.listAviableGardens);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);

        fillGardenUser();
        /*nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, huertaActivity.class));
            }
        });*/

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CreateGardenActivity.class));
            }
        });

        otherGardensButton = (Button) findViewById(R.id.otherGardensButton);
        otherGardensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, VegetablePatchAvailableActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, EditUserActivity.class));
            }
        });

    }

    private void fillGardenUser(){

    }
}