package com.example.opcv.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.opcv.R;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.view.base.MainActivity;
import com.example.opcv.view.gardens.GardensAvailableActivity;

public class guestLoginActivity extends AppCompatActivity {
    private Button guestLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        guestLoginButton = (Button) findViewById(R.id.guestLoginButton);


        guestLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCommunication auth = new AuthCommunication();
                auth.guestLogin(guestLoginActivity.this);
                Intent intent = new Intent(guestLoginActivity.this, GardensAvailableActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}