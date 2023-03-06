package com.example.opcv.formsScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.VegetablePatchAvailableActivity;
import com.example.opcv.fbComunication.FormsUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Formulario Control Proceso de Siembra
public class Form_CPS extends AppCompatActivity {

    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile;
    private EditText responsable, phase, duration, plantsSeeds, comments;
    private TextView formName;
    private Spinner spinner;
    private String phaseSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cps);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CPS.this, VegetablePatchAvailableActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CPS.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CPS.this, EditUserActivity.class));
            }
        });

        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);

        formName = (TextView) findViewById(R.id.form_CPS_name);

        responsable = (EditText) findViewById(R.id.responsable);
        //phase = (EditText) findViewById(R.id.phase);
        duration = (EditText) findViewById(R.id.timeDuration);
        plantsSeeds = (EditText) findViewById(R.id.plants_or_seeds);
        comments = (EditText) findViewById(R.id.observation_or_comments);

        spinner = (Spinner) findViewById(R.id.spinnerPhase);
        ArrayList<String> phaseElements = new ArrayList<>();
        phaseElements.add("Seleccione un elemento");
        phaseElements.add("Planeación");
        phaseElements.add("Establecer zona de plantación");
        phaseElements.add("Preparación de zona de plantación");
        phaseElements.add("Establecer método de plantación según semillas o plántulas");
        phaseElements.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
        phaseElements.add("Regado de las semillas y plántulas");
        phaseElements.add("Verificar el tamaño de la planta");
        phaseElements.add("Cosecha");
        ArrayAdapter adap = new ArrayAdapter(Form_CPS.this, android.R.layout.simple_spinner_item, phaseElements);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adap);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                phaseSelectedItem = spinner.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(phaseSelectedItem);
                text.setTextColor(Color.BLACK); // set the text color

                //spinner.setTe
                //Toast.makeText(Form_CPS.this, phaseSelectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_CPS.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });


        addFormButtom = findViewById(R.id.create_forms1_buttom);
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formsUtilities = new FormsUtilities();
                String responsablePerson, processPhase, phaseDuration, plantsOrSeeds, commentsOrObservation, idGardenFb, nameForm;
                responsablePerson = responsable.getText().toString();
                //processPhase = phase.getText().toString();
                phaseDuration = duration.getText().toString();
                plantsOrSeeds = plantsSeeds.getText().toString();
                commentsOrObservation = comments.getText().toString();
                nameForm = formName.getText().toString();
                //System.out.println("El nombre"+nameForm);

                idGardenFb = getIntent().getStringExtra("idGardenFirebase");

                Map<String,Object> infoForm = new HashMap<>();
                infoForm.put("nameForm",nameForm);
                infoForm.put("personResponsable",responsablePerson);
                infoForm.put("processPhase",phaseSelectedItem);
                infoForm.put("phaseDuration",phaseDuration);
                infoForm.put("plants or seeds",plantsOrSeeds);
                infoForm.put("comments/observations",commentsOrObservation);
                formsUtilities.createForm(Form_CPS.this,infoForm,idGardenFb);
                Toast.makeText(Form_CPS.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Form_CPS.this, HomeActivity.class));
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