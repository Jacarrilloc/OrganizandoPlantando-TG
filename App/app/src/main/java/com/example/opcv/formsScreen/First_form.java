package com.example.opcv.formsScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.fbComunication.FormsUtilities;
import com.example.opcv.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class First_form extends AppCompatActivity {

    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile;

    private TextView formsName;

    private EditText containerSize,worrmsWeightInfo,humidityInfo,amount_of_waste_info,collected_humus_info,amount_leached_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_form);

        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);

        formsName = findViewById(R.id.formsNameFist);
        String name = getIntent().getStringExtra("Name");
        formsName.setText(name);

        containerSize = findViewById(R.id.containerSizeInfo);
        worrmsWeightInfo = findViewById(R.id.worrmsWeightInfo);
        humidityInfo = findViewById(R.id.humidityInfo);
        amount_of_waste_info = findViewById(R.id.amount_of_waste_info);
        collected_humus_info = findViewById(R.id.collected_humus_info);
        amount_leached_info = findViewById(R.id.amount_leached_info);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(First_form.this, VegetablePatchAvailableActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(First_form.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(First_form.this, EditUserActivity.class));
            }
        });



        addFormButtom = findViewById(R.id.create_forms1_buttom);

        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formsUtilities = new FormsUtilities();
                String container = containerSize.getText().toString();
                String worms = worrmsWeightInfo.getText().toString();
                String humidity = humidityInfo.getText().toString();
                String waste = amount_of_waste_info.getText().toString();
                String humus = collected_humus_info.getText().toString();
                String leached = amount_leached_info.getText().toString();

                String idGardenFb = getIntent().getStringExtra("idGardenFirebase");

                Map<String,Object> infoForm = new HashMap<>();
                infoForm.put("nameForm",name);
                infoForm.put("containerSize",container);
                infoForm.put("wormsWeight",worms);
                infoForm.put("humidity",humidity);
                infoForm.put("amount of waste",waste);
                infoForm.put("collected humus",humus);
                infoForm.put("amount leached",leached);

                formsUtilities.createForm(First_form.this,infoForm,idGardenFb);
                Toast.makeText(First_form.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(First_form.this, HomeActivity.class));
                finish();
            }
        });

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}