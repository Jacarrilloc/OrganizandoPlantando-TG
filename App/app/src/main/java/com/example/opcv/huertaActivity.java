package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class huertaActivity extends AppCompatActivity {
    private Button returnArrow;
    private ImageButton vermicultureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huerta);

        //Declaracion metodos de navegacion
        returnArrow =(Button) findViewById(R.id.returnArrowButton);
        returnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(huertaActivity.this, HomeActivity.class));
            }
        });

        vermicultureButton = (ImageButton) findViewById(R.id.vermicultureButton);
        vermicultureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(huertaActivity.this, registroLombriculturaActivity.class));
            }
        });
    }
}