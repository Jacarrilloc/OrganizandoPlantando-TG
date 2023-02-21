package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class huertaActivity extends AppCompatActivity {
    private TextView nameText;
    private Button profile, gardens;
    private FloatingActionButton returnButton;

    private FirebaseAuth autentication;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huerta);

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        //LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));

        nameText = (TextView) findViewById(R.id.gardenNameText);
        String gardenName = getIntent().getExtras().getString("name");
        nameText.setText(gardenName);

        returnButton = (FloatingActionButton) findViewById(R.id.returnArrowButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(huertaActivity.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(huertaActivity.this, EditUserActivity.class));
            }
        });

        gardens = (Button) findViewById(R.id.myGardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(huertaActivity.this, HomeActivity.class));
            }
        });

        //System.out.println("EL nombre es : "+gardenName);
    }
    /*public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };*/
}