package com.example.opcv.view.ludification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DisplayPlantsDiseasesActivity extends AppCompatActivity {
    private Button profile, myGardens, rewards, ludification;
    private ImageView lettuce, cabbage, broccoli, radish, coriander, potato;
    private FloatingActionButton returnArrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plants_diseases);

        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        lettuce = (ImageView) findViewById(R.id.lettuce);
        cabbage = (ImageView) findViewById(R.id.cabbage);
        broccoli = (ImageView) findViewById(R.id.broccoli);
        radish = (ImageView) findViewById(R.id.radish);
        coriander = (ImageView) findViewById(R.id.coriander);
        potato = (ImageView) findViewById(R.id.potato);
        returnArrowButton = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);

        lettuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPlantsDiseasesActivity.this, LettuceDiseasesActivity.class);
                startActivity(intent);
            }
        });

        cabbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPlantsDiseasesActivity.this, CabbageDiseasesActivity.class);
                startActivity(intent);
            }
        });

        broccoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPlantsDiseasesActivity.this, BroccoliDiseaseActivity.class);
                startActivity(intent);
            }
        });

        radish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPlantsDiseasesActivity.this, RadishDiseaseActivity.class);
                startActivity(intent);
            }
        });

        coriander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPlantsDiseasesActivity.this, CorianderDiseaseActivity.class);
                startActivity(intent);
            }
        });

        potato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPlantsDiseasesActivity.this, PotatoDiseaseActivity.class);
                startActivity(intent);
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(DisplayPlantsDiseasesActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(DisplayPlantsDiseasesActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DisplayPlantsDiseasesActivity.this, GardensAvailableActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(DisplayPlantsDiseasesActivity.this, EditUserActivity.class);
                AuthCommunication auth = new AuthCommunication();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DisplayPlantsDiseasesActivity.this, RewardHomeActivity.class));
            }
        });

        returnArrowButton.setOnClickListener(new View.OnClickListener() {
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