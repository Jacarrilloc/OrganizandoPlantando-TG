package com.example.opcv.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.model.entity.ValidateRegisterInfo;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.view.ludification.RewardHomeActivity;

public class ChangePassword extends AppCompatActivity {

    private Button accept, rewards, profile, myGardens, returnB, ludification;
    private TextView password, repeatPassword;
    private boolean validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        profile = (Button) findViewById(R.id.profile);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        myGardens = (Button) findViewById(R.id.myGardens);
        accept = (Button) findViewById(R.id.acceptButton);
        returnB = (Button) findViewById(R.id.returnButton);
        password = (TextView) findViewById(R.id.editTextTextPassword2);
        repeatPassword = (TextView) findViewById(R.id.editTextTextPassword3);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, EditUserActivity.class));
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, RewardHomeActivity.class));
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    startActivity(new Intent(ChangePassword.this, DictionaryHomeActivity.class));
                }
                else{
                    Toast.makeText(ChangePassword.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, HomeActivity.class));
            }
        });



        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordText, confirmPassword;
                passwordText = password.getText().toString();
                confirmPassword = repeatPassword.getText().toString();
                ValidateRegisterInfo validateRegisterInfo = new ValidateRegisterInfo();
                validation = validateRegisterInfo.validateNewPassword(passwordText, ChangePassword.this, confirmPassword);
                if(validation){
                    AuthCommunication auth = new AuthCommunication();
                    auth.changePassword(confirmPassword, ChangePassword.this);
                    startActivity(new Intent(ChangePassword.this, EditUserActivity.class));
                }
            }
        });

        returnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, EditUserActivity.class));
            }
        });


    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}