package com.example.opcv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterProfileActivity extends AppCompatActivity {

    private EditText name,lastName,email,password,confirmPassword;
    private CheckBox terms;
    private Button register;
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = name.getText().toString();
                String lastNameString = lastName.getText().toString();
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String confirmPasswordString = confirmPassword.getText().toString();
                Boolean termsBool = terms.isChecked();
                if(validationField(nameString,lastNameString,emailString,passwordString,confirmPasswordString,termsBool))
                {
                    createNewUser(emailString,passwordString);
                    addAllInfoNewUser(nameString,lastNameString,emailString);
                    Toast.makeText(RegisterProfileActivity.this, "Se ha Creado correctamente el Usuario", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterProfileActivity.this,HomeActivity.class));
                }
                else
                {
                    Toast.makeText(RegisterProfileActivity.this, "No fue posible crear el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validationField(String name,String lastName,String email,String password,String confirmPassword,Boolean terms)
    {
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "Debe rellenar todos los campos.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!validarCorreo(email)){
            Toast.makeText(this, "Ingrese un correo válido.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!validarContra(password)){
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

    private boolean validarCorreo(String email){
        if(!email.contains("@") || !email.contains(".") || email.length() < 5)
            return false;
        return true;
    }

    private boolean validarContra(String contra){
        if(contra.length() < 6)
            return false;
        return true;
    }

    private void createNewUser(String email,String password){
        autentication.createUserWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = autentication.getCurrentUser();
                    Log.i("DB","New User Created");
                }
            }
        });
    }

    private void addAllInfoNewUser(String name,String lastName,String email){
        String IdUser = autentication.getCurrentUser().getUid();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("nombre",name );
        userInfo.put("apellido",lastName );
        userInfo.put("email",email );

        database.collection("UserInfo").document(IdUser).set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("Database","User Added to Database");
            }
        });
    }
}