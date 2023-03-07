package com.example.opcv.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.TermsAndConditionsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterProfileActivity extends AppCompatActivity {

    private EditText name,lastName,email,password,confirmPassword,phoneNumber;
    private CheckBox terms;
    private Button register,termsConditions;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        autentication = FirebaseAuth.getInstance();
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
                if(validationField(nameString,lastNameString,emailString,passwordString,confirmPasswordString,termsBool)){
                    createNewUser(emailString,passwordString);
                }
            }
        });
    }

    private boolean validationField(String name,String lastName,String email,String password,String confirmPassword,Boolean terms)
    {
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "Debe rellenar todos los campos.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!validateEmail(email)){
            Toast.makeText(this, "Ingrese un correo válido.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!validatePassword(password)){
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Las contraseñas no concuerdan.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!terms){
            Toast.makeText(this, "Es necesario Aceptar los terminos y Condiciones", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void createNewUser(String email,String password){
        autentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = autentication.getCurrentUser();
                    addToDataBase(user.getUid().toString());
                }
            }
        });
    }

    private void addToDataBase(String userId) {
        Map<String, Object> newUserInfo = new HashMap<>();
        newUserInfo.put("ID", userId);
        newUserInfo.put("Name", name.getText().toString());
        newUserInfo.put("LastName", lastName.getText().toString());
        newUserInfo.put("Email", email.getText().toString());

        Intent intent = new Intent(RegisterProfileActivity.this, RegisterMobilePhone.class);
        intent.putExtra("mapUser",(Serializable) newUserInfo);
        startActivity(intent);
    }

    private boolean validateEmail(String email){
        if(!email.contains("@") || !email.contains(".") || email.length() < 5)
            return false;
        return true;
    }

    private boolean validatePassword(String password){
        if(password.length() < 6)
            return false;
        return true;
    }
}