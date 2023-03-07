package com.example.opcv.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.fbComunication.AuthUtilities;

import java.util.Map;

public class RegisterMobilePhone extends AppCompatActivity {
    private ImageButton continueButtom;
    private TextView laterText,welcome;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile_phone);

        phone = findViewById(R.id.phone_Imput);
        continueButtom = findViewById(R.id.continueButtom_phone_activity);
        laterText = findViewById(R.id.later_Buttom);
        welcome = findViewById(R.id.welcome_msg);

        Intent intent = getIntent();
        Map<String, Object> newUserInfo = (Map<String, Object>) intent.getSerializableExtra("mapUser");
        String emailRegister = intent.getStringExtra("emailRegister");
        String passwordRegister = intent.getStringExtra("passwordRegister");

        String nameUser = newUserInfo.get("Name").toString();

        String mesage = "Hola " + nameUser + ", Bienvenido(a) a Ceres";

        welcome.setText(mesage);

        continueButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PhoneNumberAdded = phone.getText().toString();
                newUserInfo.put("PhoneNumber",PhoneNumberAdded);
                AuthUtilities authUtilities = new AuthUtilities();
                if(!authUtilities.createUser(emailRegister,passwordRegister,newUserInfo,RegisterMobilePhone.this)){
                    Intent intent = new Intent(RegisterMobilePhone.this, HomeActivity.class);
                    intent.putExtra("authUtilities", authUtilities);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        laterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PhoneNumberAdded = "";
                newUserInfo.put("PhoneNumber",PhoneNumberAdded);
                AuthUtilities authUtilities = new AuthUtilities();
                if(!authUtilities.createUser(emailRegister,passwordRegister,newUserInfo,RegisterMobilePhone.this)){
                    Intent intent = new Intent(RegisterMobilePhone.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}