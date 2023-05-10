package com.example.opcv.view.ludification;

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
import android.widget.Toast;

import com.example.opcv.view.auth.SignOffActivity;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.view.gardens.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.business.ludification.Level;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DictionaryHomeActivity extends AppCompatActivity {

    private Button plants, tools;
    private Button profile, myGardens, rewards;
    private String idUser, idDocUSer;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_home);

        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        plants = (Button) findViewById(R.id.plants);
        tools = (Button) findViewById(R.id.tools);
        AuthCommunication auth = new AuthCommunication();
        idUser = auth.getCurrentUserUid();
        AuthCommunication authCommunication = new AuthCommunication();
        FirebaseUser user = authCommunication.guestUser();



        plants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent edit = new Intent(DictionaryHomeActivity.this, ShowDictionaryActivity.class);
                edit.putExtra("userInfo", idUser);
                edit.putExtra("element", "Plants");
                startActivity(edit);
            }
        });

        tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(DictionaryHomeActivity.this, ShowDictionaryActivity.class);
                edit.putExtra("userInfo", idUser);
                edit.putExtra("element", "Tools");
                startActivity(edit);
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DictionaryHomeActivity.this, HomeActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null && !user.isAnonymous()){
                    Intent edit = new Intent(DictionaryHomeActivity.this, EditUserActivity.class);
                    AuthCommunication auth = new AuthCommunication();
                    String userId = auth.getCurrentUserUid();
                    edit.putExtra("userInfo", userId);
                    startActivity(edit);
                }
                else{
                    startActivity(new Intent(DictionaryHomeActivity.this, SignOffActivity.class));
                }
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null && !user.isAnonymous()){
                    startActivity(new Intent(DictionaryHomeActivity.this, RewardHomeActivity.class));
                }
                else{
                    Toast.makeText(DictionaryHomeActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
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