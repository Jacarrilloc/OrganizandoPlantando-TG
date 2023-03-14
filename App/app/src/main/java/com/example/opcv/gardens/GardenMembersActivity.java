package com.example.opcv.gardens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.opcv.R;

public class GardenMembersActivity extends AppCompatActivity {
    private String gardenID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_members);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            gardenID = extras.getString("idGarden");
        }

    }
}