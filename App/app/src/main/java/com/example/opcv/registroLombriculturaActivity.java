package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class registroLombriculturaActivity extends AppCompatActivity {
    private Button returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_lombricultura);

        //Declaracion metodos de navegacion
        returnButton = (Button) findViewById(R.id.returnBotton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registroLombriculturaActivity.this, huertaActivity.class));
            }
        });
    }
}