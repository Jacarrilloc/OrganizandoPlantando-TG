package com.example.opcv.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.TermsAndConditionsActivity;
import com.example.opcv.adapter.SpinnerAdapter;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.info.User;
import com.example.opcv.info.ValidateRegisterInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterProfileActivity extends AppCompatActivity {

    private EditText name,lastName,email,password,confirmPassword,phoneNumber;
    private CheckBox terms;
    private Button register,termsConditions;
    private Spinner spinnerGender;
    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        spinnerGender = findViewById(R.id.spinnerGenderRegisterActivity);
        name = findViewById(R.id.imputNameRegisterActivity);
        lastName = findViewById(R.id.imputLastNameRegisterActivity);
        email = findViewById(R.id.imputEmailRegisterActivity);
        password = findViewById(R.id.imputPasswordRegisterActivity);
        confirmPassword = findViewById(R.id.imputConfirmPaswordRegisterActivity);
        terms = findViewById(R.id.okSignalTermsRegisterActivity);
        register = findViewById(R.id.createAcountButtomRegisterActivity);
        termsConditions = findViewById(R.id.terms_condition_buttom_registerProfileActivity);

        int textColor = ContextCompat.getColor(this,R.color.black);

        List<String> genders = new ArrayList<>();
        genders.add("Hombre");
        genders.add("Mujer");
        genders.add("Otro");

        SpinnerAdapter adatperSpinner = new SpinnerAdapter(this, android.R.layout.simple_spinner_item,genders,textColor);
        spinnerGender.setAdapter(adatperSpinner);
        /*ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.gendersElements, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterSpinner);*/

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
                String gender = spinnerGender.getSelectedItem().toString();
                Boolean termsBool = terms.isChecked();

                ValidateRegisterInfo validate = new ValidateRegisterInfo();
                if(validate.validateFirstRegisterInfo(nameString,lastNameString,emailString,passwordString,confirmPasswordString,termsBool,RegisterProfileActivity.this)) {
                    newUser = new User(nameString, lastNameString, emailString, null, null,null,gender);

                    Intent intent = new Intent(RegisterProfileActivity.this, RegisterMobilePhone.class);
                    intent.putExtra("mapUser", (Serializable) newUser);
                    intent.putExtra("password",passwordString);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);
        super.attachBaseContext(newBase);
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration config = new Configuration(newConfig);
        adjustFontScale(getApplicationContext(), config);
    }
    public static void adjustFontScale(Context context, Configuration configuration) {
        if (configuration.fontScale != 1) {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }
}