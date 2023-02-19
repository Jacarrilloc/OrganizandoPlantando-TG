package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class RegisterMobilePhone extends AppCompatActivity {

    private FirebaseFirestore database;
    private ImageButton continueButtom;
    private TextView laterText;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile_phone);

        phone = findViewById(R.id.phone_Imput);
        continueButtom = findViewById(R.id.continueButtom_phone_activity);
        laterText = findViewById(R.id.later_Buttom);

        database = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        Map<String, Object> myMap = (Map<String, Object>) intent.getSerializableExtra("mapUser");


        continueButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PhoneNumberAdded = phone.getText().toString();
                myMap.put("PhoneNumber",PhoneNumberAdded);
                registerUser(myMap,PhoneNumberAdded);
            }
        });

        laterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PhoneNumberAdded = "";
                myMap.put("PhoneNumber",PhoneNumberAdded);
                registerUser(myMap,PhoneNumberAdded);
            }
        });
    }

    private void registerUser(Map<String, Object> newUserInfo,String phoneNumber){
        CollectionReference collectionRef = database.collection("UserInfo");
        collectionRef.add(newUserInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent intent = new Intent(RegisterMobilePhone.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}