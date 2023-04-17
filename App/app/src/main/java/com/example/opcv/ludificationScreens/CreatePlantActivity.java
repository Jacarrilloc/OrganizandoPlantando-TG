package com.example.opcv.ludificationScreens;

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
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.business.ludificationLogic.LudificationLogic;
import com.example.opcv.business.ludificationLogic.levelLogic;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.gardens.GardensAvailableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreatePlantActivity extends AppCompatActivity {

    private CheckBox flower, edible, fruit, medicinal, pet, precaution;
    private EditText name, descr;
    private String plantName, plantDescription, idUser;
    private Boolean flowerCheck, edibleCheck, fruitCheck, medicineCheck, petCheck, precautionCheck;
    private FloatingActionButton add;
    private Button profile, myGardens, gardensMap, ludification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plant);

        name = (EditText) findViewById(R.id.namePlant);
        descr = (EditText) findViewById(R.id.description);
        flower = (CheckBox) findViewById(R.id.option1);
        edible = (CheckBox) findViewById(R.id.option3);
        fruit = (CheckBox) findViewById(R.id.option5);
        medicinal = (CheckBox) findViewById(R.id.option2);
        pet = (CheckBox) findViewById(R.id.option4);
        precaution = (CheckBox) findViewById(R.id.option6);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        gardensMap = (Button) findViewById(R.id.gardens);
        ludification = (Button) findViewById(R.id.ludification);
        add = (FloatingActionButton) findViewById(R.id.addButton);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idUser = extras.getString("userInfo");
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LudificationLogic logic = new LudificationLogic();
                levelLogic level = new levelLogic();
                plantName = name.getText().toString();
                plantDescription = descr.getText().toString();
                flowerCheck = flower.isChecked();
                edibleCheck = edible.isChecked();
                fruitCheck = fruit.isChecked();
                medicineCheck = medicinal.isChecked();
                petCheck = pet.isChecked();
                precautionCheck = precaution.isChecked();
                if(logic.validateField(plantName, plantDescription, CreatePlantActivity.this)){
                    logic.addPlantElementsMap(plantName, plantDescription, flowerCheck, fruitCheck, edibleCheck, medicineCheck, petCheck, precautionCheck, CreatePlantActivity.this, idUser);
                    level.addLevel(idUser, true);
                    Intent edit = new Intent(CreatePlantActivity.this, DictionaryHome.class);
                    edit.putExtra("userInfo", idUser);
                    startActivity(edit);
                }
            }
        });
        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(CreatePlantActivity.this, DictionaryHome.class);
                startActivity(edit);
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreatePlantActivity.this, GardensAvailableActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(CreatePlantActivity.this, EditUserActivity.class);
                AuthUtilities auth = new AuthUtilities();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreatePlantActivity.this, MapsActivity.class));
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