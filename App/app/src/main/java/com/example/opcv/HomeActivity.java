package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private Button nextArrow, addButton, otherGardensButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Declaracion metodos de navegacion
        nextArrow = (Button) findViewById(R.id.nextArrow);
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, huertaActivity.class));
            }
        });

        addButton = (Button) findViewById(R.id.addButton);
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

    }
}