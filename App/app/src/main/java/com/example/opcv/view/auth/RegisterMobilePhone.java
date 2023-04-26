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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.opcv.R;
import com.example.opcv.model.entity.User;

import java.io.Serializable;

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