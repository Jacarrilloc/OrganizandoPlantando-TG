package com.example.opcv.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.opcv.R;
import com.example.opcv.model.persistance.firebase.AuthCommunication;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button sendEmail, returnScreen;
    private String emailAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText) findViewById(R.id.emailAddress);
        sendEmail = (Button) findViewById(R.id.addLinkbutton);
        returnScreen = (Button) findViewById(R.id.returnButtonLink);
        AuthCommunication auth = new AuthCommunication();

        returnScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailAdd = email.getText().toString();
                auth.forgottenPassword(emailAdd, ForgotPasswordActivity.this);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}