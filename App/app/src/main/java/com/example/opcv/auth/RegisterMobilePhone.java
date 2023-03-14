package com.example.opcv.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.opcv.R;
import com.example.opcv.info.User;

import java.io.Serializable;
import java.util.Map;

public class RegisterMobilePhone extends AppCompatActivity {
    private ImageButton continueButtom;
    private TextView laterText,welcome;
    private EditText phone;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile_phone);

        phone = findViewById(R.id.phone_Imput);
        continueButtom = findViewById(R.id.continueButtom_phone_activity);
        laterText = findViewById(R.id.later_Buttom);
        welcome = findViewById(R.id.welcome_msg);

        Intent intent = getIntent();
        User newUser = (User) intent.getSerializableExtra("mapUser");
        password = intent.getStringExtra("password");

        continueButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PhoneNumberAdded = phone.getText().toString();
                newUser.setPhoneNumber(PhoneNumberAdded);
                callSelectPhoto(newUser);
                /*
                AuthUtilities authUtilities = new AuthUtilities();
                if(!authUtilities.createUser(emailRegister,passwordRegister,newUserInfo,RegisterMobilePhone.this)){
                    Intent intent = new Intent(RegisterMobilePhone.this, HomeActivity.class);
                    intent.putExtra("authUtilities", authUtilities);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }*/
            }
        });

        laterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PhoneNumberAdded = "";
                newUser.setPhoneNumber(PhoneNumberAdded);
                callSelectPhoto(newUser);
            }
        });
    }
    private void callSelectPhoto(User newUser){
        Intent intent = new Intent(RegisterMobilePhone.this,SelectPhotoActivity.class);
        intent.putExtra("newUserInfo",(Serializable) newUser);
        intent.putExtra("password",password);
        startActivity(intent);
    }
}