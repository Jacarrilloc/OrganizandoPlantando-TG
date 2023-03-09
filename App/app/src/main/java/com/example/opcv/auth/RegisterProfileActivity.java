package com.example.opcv.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.TermsAndConditionsActivity;
import com.example.opcv.fbComunication.AuthUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterProfileActivity extends AppCompatActivity {

    private EditText name,lastName,email,password,confirmPassword,phoneNumber;
    private CheckBox terms;
    private ImageView profilePhoto;
    private Button register,termsConditions,selectPhoto;
    private FirebaseFirestore database;

    private Map<String, Object> newUserInfo;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        database = FirebaseFirestore.getInstance();

        selectPhoto = findViewById(R.id.selectPhotoProfileActivity);
        name = findViewById(R.id.imputNameRegisterActivity);
        lastName = findViewById(R.id.imputLastNameRegisterActivity);
        email = findViewById(R.id.imputEmailRegisterActivity);
        password = findViewById(R.id.imputPasswordRegisterActivity);
        confirmPassword = findViewById(R.id.imputConfirmPaswordRegisterActivity);
        terms = findViewById(R.id.okSignalTermsRegisterActivity);
        register = findViewById(R.id.createAcountButtomRegisterActivity);

        termsConditions = findViewById(R.id.terms_condition_buttom_registerProfileActivity);

        newUserInfo = new HashMap<>();

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePhoto = new Intent(RegisterProfileActivity.this,SelectPhotoActivity.class);
                takePhoto.putExtra("newUserInfo",(Serializable) newUserInfo);
                startActivity(takePhoto);
            }
        });

        termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterProfileActivity.this, TermsAndConditionsActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = name.getText().toString();
                String lastNameString = lastName.getText().toString();
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String confirmPasswordString = confirmPassword.getText().toString();
                Boolean termsBool = terms.isChecked();
                AuthUtilities check = new AuthUtilities();

                newUserInfo.put("Name", nameString);
                newUserInfo.put("LastName", lastNameString);
                newUserInfo.put("Email", emailString);

                Intent intent = new Intent(RegisterProfileActivity.this, RegisterMobilePhone.class);
                intent.putExtra("mapUser",(Serializable) newUserInfo);
                intent.putExtra("emailRegister",emailString);
                intent.putExtra("passwordRegister",passwordString);
                startActivity(intent);
            }
        });
    }
}