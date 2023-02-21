package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class deleteGardenConfirmationActivity extends AppCompatActivity {
    private Button delete, returnP, gardens, myGardens, profile;

    private String idGarden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_garden_confirmation);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idGarden = extras.getString("idGarden");

        }

        delete = (Button) findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(deleteGardenConfirmationActivity.this,GardenEditActivity.class);
                start.putExtra("response",true);
                start.putExtra("idGarden", idGarden);

                startActivity(start);
            }
        });
        returnP = (Button) findViewById(R.id.returnButton2);
        returnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(deleteGardenConfirmationActivity.this,GardenEditActivity.class);
                start.putExtra("response",false);
                start.putExtra("idGarden", idGarden);

                startActivity(start);
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(deleteGardenConfirmationActivity.this, HomeActivity.class));
            }
        });

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(deleteGardenConfirmationActivity.this, VegetablePatchAvailableActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(deleteGardenConfirmationActivity.this, EditUserActivity.class));
            }
        });

    }
}