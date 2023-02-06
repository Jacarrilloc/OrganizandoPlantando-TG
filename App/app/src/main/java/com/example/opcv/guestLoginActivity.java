package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class guestLoginActivity extends AppCompatActivity {
    private TextView tosText;
    private Button guestLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        //Declaracion metodos de navegacion
        tosText = (TextView) findViewById(R.id.termsAndConditionsText);
        tosText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(guestLoginActivity.this, TermsAndConditionsActivity.class));
            }
        });
        guestLoginButton = (Button) findViewById(R.id.guestLoginButton);
        guestLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(guestLoginActivity.this, VegetablePatchAvailableActivity.class));
            }
        });
    }
}