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

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.fbComunication.FormsUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Recepción y salida de materia Prima
public class Form_RSMP extends AppCompatActivity {

    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile;

    private Spinner spinnerUnits, spinnerConcept;
    private String unitSelectedItem, conceptSelectedItem;
    private EditText description, quantity, total, state;
    private TextView formName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rsmp);

        formName = (TextView) findViewById(R.id.form_RSMP_name);
        description = (EditText) findViewById(R.id.description);
        quantity = (EditText) findViewById(R.id.amount_of_Mp);
        total = (EditText) findViewById(R.id.total_Mp);
        state = (EditText) findViewById(R.id.state);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RSMP.this, MapsActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RSMP.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RSMP.this, EditUserActivity.class));
            }
        });

        backButtom = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormOnetoFormListElement);

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        spinnerConcept = (Spinner) findViewById(R.id.conceptChoice);
        ArrayList<String> phaseElements = new ArrayList<>();
        phaseElements.add("Seleccione un elemento");
        phaseElements.add("Recepción");
        phaseElements.add("Salida");

        ArrayAdapter adap = new ArrayAdapter(Form_RSMP.this, android.R.layout.simple_spinner_item, phaseElements);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConcept.setAdapter(adap);

        spinnerConcept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                conceptSelectedItem = spinnerConcept.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(conceptSelectedItem);
                text.setTextColor(Color.BLACK); // set the text color
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_RSMP.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerUnits = (Spinner) findViewById(R.id.unitsChoice);
        ArrayList<String> phaseElements2 = new ArrayList<>();
        phaseElements2.add("Seleccione un elemento");
        phaseElements2.add("Litros");
        phaseElements2.add("Kilogramos");
        phaseElements2.add("Libras");
        phaseElements2.add("Mililitros");
        phaseElements2.add("Gramos");

        ArrayAdapter adap2 = new ArrayAdapter(Form_RSMP.this, android.R.layout.simple_spinner_item, phaseElements2);
        adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adap2);

        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unitSelectedItem = spinnerUnits.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(unitSelectedItem );
                text.setTextColor(Color.BLACK); // set the text color
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_RSMP.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        addFormButtom = findViewById(R.id.create_forms4_buttom);
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formsUtilities = new FormsUtilities();
                String descriptionR, quantityR, totalR, stateR, nameForm, idGardenFb;

                descriptionR = description.getText().toString();
                quantityR = quantity.getText().toString();
                totalR = total.getText().toString();
                stateR = state.getText().toString();
                nameForm = formName.getText().toString();

                idGardenFb = getIntent().getStringExtra("idGardenFirebase");

                Map<String,Object> infoForm = new HashMap<>();
                infoForm.put("idForm",4);
                infoForm.put("nameForm",nameForm);
                infoForm.put("description",descriptionR);
                infoForm.put("units",unitSelectedItem);
                infoForm.put("quantity",quantityR);
                infoForm.put("total",totalR);
                infoForm.put("concept",conceptSelectedItem);
                infoForm.put("state",stateR);
                formsUtilities.createForm(Form_RSMP.this,infoForm,idGardenFb);
                Toast.makeText(Form_RSMP.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Form_RSMP.this, HomeActivity.class));
                finish();
            }
        });



    }
}