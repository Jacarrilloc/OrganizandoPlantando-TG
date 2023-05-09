package com.example.opcv.view.ludification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SelectedPlantDiseasesActivity extends AppCompatActivity {
    private Button profile, myGardens, rewards, ludification;
    private FloatingActionButton returnArrowButton;
    private TextView textView;
    private String selectedPlant;
    private LinearLayout lettuceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_plant_diseases);

        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        returnArrowButton = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);
        textView = (TextView) findViewById(R.id.textView6);
        lettuceLayout = (LinearLayout) findViewById(R.id.lettuceLayout);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            selectedPlant = extras.getString("plant");
        }
        textView.setText(selectedPlant);

        displayPlant(selectedPlant);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(SelectedPlantDiseasesActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(SelectedPlantDiseasesActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectedPlantDiseasesActivity.this, GardensAvailableActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(SelectedPlantDiseasesActivity.this, EditUserActivity.class);
                AuthCommunication auth = new AuthCommunication();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectedPlantDiseasesActivity.this, RewardHomeActivity.class));
            }
        });

        returnArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void displayPlant(String selectedPlant) {
        switch (selectedPlant){
            case "Lechuga":
                lettuceLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
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