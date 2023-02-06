package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class signOffActivity extends AppCompatActivity {
    private Button signOffBuuton, returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_off);

        //Declaracion metodos de navegacion
        signOffBuuton = (Button) findViewById(R.id.closeButton);
        signOffBuuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signOffActivity.this, NewToAppActivity.class));
            }
        });

        returnButton = (Button) findViewById(R.id.returnButton2);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signOffActivity.this, EditUserActivity.class));
            }
        });
    }
}