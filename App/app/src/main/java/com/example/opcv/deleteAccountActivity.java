package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class deleteAccountActivity extends AppCompatActivity {
    private Button deleteAccountButton, returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        //Declaracion metodos de navegacion
        deleteAccountButton = (Button) findViewById(R.id.deleteButton);
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(deleteAccountActivity.this, NewToAppActivity.class));
            }
        });

        returnButton = (Button) findViewById(R.id.returnButton2);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(deleteAccountActivity.this, EditUserActivity.class));
            }
        });
    }
}