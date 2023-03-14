package com.example.opcv.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.TermsAndConditionsActivity;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.info.User;
import com.example.opcv.info.ValidateRegisterInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterProfileActivity extends AppCompatActivity {

    private EditText name,lastName,email,password,confirmPassword,phoneNumber;
    private CheckBox terms;
    private Button register,termsConditions;
    private FirebaseFirestore database;
    private User newUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        database = FirebaseFirestore.getInstance();

        name = findViewById(R.id.imputNameRegisterActivity);
        lastName = findViewById(R.id.imputLastNameRegisterActivity);
        email = findViewById(R.id.imputEmailRegisterActivity);
        password = findViewById(R.id.imputPasswordRegisterActivity);
        confirmPassword = findViewById(R.id.imputConfirmPaswordRegisterActivity);
        terms = findViewById(R.id.okSignalTermsRegisterActivity);
        register = findViewById(R.id.createAcountButtomRegisterActivity);

        termsConditions = findViewById(R.id.terms_condition_buttom_registerProfileActivity);

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

                ValidateRegisterInfo validate = new ValidateRegisterInfo();
                if(validate.validateFirstRegisterInfo(nameString,lastNameString,emailString,passwordString,confirmPasswordString,termsBool,RegisterProfileActivity.this)) {
                    newUser = new User(nameString, lastNameString, emailString, null, null,null);

                    Intent intent = new Intent(RegisterProfileActivity.this, RegisterMobilePhone.class);
                    intent.putExtra("mapUser", (Serializable) newUser);
                    intent.putExtra("password",passwordString);
                    startActivity(intent);
                }
            }
        });
    }
}