package com.example.opcv.view.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.business.persistance.firebase.AuthCommunication;

public class LoginActivity extends AppCompatActivity {

    private Button registerButtom,loginButtom;
    private EditText emailLogin,passwordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.editTextTextEmailAddress);
        passwordLogin = findViewById(R.id.editTextTextPassword);

        registerButtom = findViewById(R.id.registerButtom);
        loginButtom = findViewById(R.id.buttonLoginActivity);

        loginButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInfo = emailLogin.getText().toString();
                String passwordInfo = passwordLogin.getText().toString();
                logginUser(emailInfo,passwordInfo,LoginActivity.this);
            }
        });

        registerButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterProfileActivity.class));
            }
        });
    }

    private void logginUser(String email, String password, Context context){
        AuthCommunication auth = new AuthCommunication();
        auth.loginUserVerify(email, password, context, new AuthCommunication.LoginCallback() {
            @Override
            public void onLogin(boolean success) {
                if(success){
                    Intent intent =  new Intent(LoginActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }else{
                    Toast.makeText(context, "No fue posible Iniciar Sesion, Por Favor revise si ingresó correctamente el correo y la contraseña", Toast.LENGTH_SHORT).show();
                    emailLogin.setText("");
                    passwordLogin.setText("");
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