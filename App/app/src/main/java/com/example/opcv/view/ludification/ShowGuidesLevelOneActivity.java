package com.example.opcv.view.ludification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShowGuidesLevelOneActivity extends AppCompatActivity {
    private Button profile, myGardens, rewards, ludification, firstGuide, secondGuide, thirdGuide, fourthGuide;
    private FloatingActionButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_level_one_guides);

        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        firstGuide = (Button) findViewById(R.id.firstGuide);
        secondGuide = (Button) findViewById(R.id.secondGuide);
        thirdGuide = (Button) findViewById(R.id.thirdGuide);
        fourthGuide = (Button) findViewById(R.id.fourthGuide);
        back = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);

        firstGuide.setText("Pasos basicos para establecer y manejar tu huerta");
        secondGuide.setText("¿Cómo preparar el suelo para sembrar?");
        thirdGuide.setText("Inicia tus huertas con estas 5 plantas");
        fourthGuide.setText("Polinizadores atráelos a tu huerta con estas plantas");


        firstGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowGuidesLevelOneActivity.this, DisplayPdfActivity.class);
                edit.putExtra("path", "LevelOne/Pasos_basicos_para_establecer_y_manejar_tu_huerta.pdf");
                edit.putExtra("relative", "Pasos_basicos_para_establecer_y_manejar_tu_huerta.pdf");
                startActivity(edit);
            }
        });
        secondGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowGuidesLevelOneActivity.this, DisplayPdfActivity.class);
                edit.putExtra("path", "LevelOne/¿Cómo preparar el suelo para sembrar.pdf");
                edit.putExtra("relative", "¿Cómo preparar el suelo para sembrar.pdf");
                startActivity(edit);
            }
        });

        thirdGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowGuidesLevelOneActivity.this, DisplayPdfActivity.class);
                edit.putExtra("path", "LevelOne/Inicia tus huertas con estas 5 plantas.pdf");
                edit.putExtra("relative", "Inicia tus huertas con estas 5 plantas.pdf");
                startActivity(edit);
            }
        });

        fourthGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowGuidesLevelOneActivity.this, DisplayPdfActivity.class);
                edit.putExtra("path", "LevelOne/Polinizadores atráelos a tu huerta con estas plantas.pdf");
                edit.putExtra("relative", "Polinizadores atráelos a tu huerta con estas plantas.pdf");
                startActivity(edit);
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(ShowGuidesLevelOneActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(ShowGuidesLevelOneActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowGuidesLevelOneActivity.this, GardensAvailableActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowGuidesLevelOneActivity.this, EditUserActivity.class);
                AuthCommunication auth = new AuthCommunication();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowGuidesLevelOneActivity.this, RewardHomeActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}