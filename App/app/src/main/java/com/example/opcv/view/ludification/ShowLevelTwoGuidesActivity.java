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
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShowLevelTwoGuidesActivity extends AppCompatActivity {
    private Button profile, myGardens, rewards, ludification, firstGuide, secondGuide, thirdGuide;
    private FloatingActionButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_level_two_guides);

        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        firstGuide = (Button) findViewById(R.id.firstGuide);
        secondGuide = (Button) findViewById(R.id.secondGuide);
        thirdGuide = (Button) findViewById(R.id.thirdGuide);
        back = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);

        firstGuide.setText("5 plantas medicinales para tu huerta");
        secondGuide.setText("5 repelentes orgánicos caseros");
        thirdGuide.setText("4 usos de la cáscara de huevo en la huerta");

        firstGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowLevelTwoGuidesActivity.this, DisplayPdfActivity.class);
                edit.putExtra("path", "LevelFour/5 plantas medicinales para tu huerta.pdf");

                startActivity(edit);
            }
        });
        secondGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowLevelTwoGuidesActivity.this, DisplayPdfActivity.class);

                edit.putExtra("path", "LevelFour/5 repelentes orgánicos caseros.pdf");

                startActivity(edit);
            }
        });

        thirdGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowLevelTwoGuidesActivity.this, DisplayPdfActivity.class);

                edit.putExtra("path", "LevelFour/4 usos de la cáscara de huevo en la huerta.pdf");

                startActivity(edit);
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(ShowLevelTwoGuidesActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(ShowLevelTwoGuidesActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowLevelTwoGuidesActivity.this, GardensAvailableActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowLevelTwoGuidesActivity.this, EditUserActivity.class);
                AuthCommunication auth = new AuthCommunication();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowLevelTwoGuidesActivity.this, RewardHomeActivity.class));
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