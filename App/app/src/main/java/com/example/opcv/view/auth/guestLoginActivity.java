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

import com.example.opcv.R;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class guestLoginActivity extends AppCompatActivity {
    private Button guestLoginButton;
    private FloatingActionButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        guestLoginButton = (Button) findViewById(R.id.guestLoginButton);
        returnButton = (FloatingActionButton) findViewById(R.id.returnBotton);

        guestLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCommunication auth = new AuthCommunication();
                auth.guestLogin(guestLoginActivity.this);
                Intent intent = new Intent(guestLoginActivity.this, GardensAvailableActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("previusScreen", "false");
                startActivity(intent);
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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