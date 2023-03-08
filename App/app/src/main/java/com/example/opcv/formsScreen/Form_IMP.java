package com.example.opcv.formsScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.VegetablePatchAvailableActivity;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.fbComunication.FormsUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Formulario Inventario de Materia Prima
public class Form_IMP extends AppCompatActivity {

    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile;
    private Spinner spinnerMovement, spinnerUnits, spinnerConcept;
    private String movementSelectedItem, unitSelectedItem, conceptSelectedItem;
    private EditText rawMatirial, quantity, existingTool;
    private TextView formName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_imp);

        formName = (TextView) findViewById(R.id.form_IMP_name);

        rawMatirial = (EditText) findViewById(R.id.rawMaterialContainer);
        quantity = (EditText) findViewById(R.id.quantity);
        existingTool = (EditText) findViewById(R.id.existanceTool);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_IMP.this, VegetablePatchAvailableActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_IMP.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_IMP.this, EditUserActivity.class));
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
        ArrayList<String> phaseElements3 = new ArrayList<>();
        phaseElements3.add("Seleccione un elemento");
        phaseElements3.add("Donación");
        phaseElements3.add("Compra");
        phaseElements3.add("Salida");

        ArrayAdapter adap3 = new ArrayAdapter(Form_IMP.this, android.R.layout.simple_spinner_item, phaseElements3);
        adap3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConcept.setAdapter(adap3);

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
                Toast.makeText(Form_IMP.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerMovement = (Spinner) findViewById(R.id.spinnerChoice);
        ArrayList<String> phaseElements = new ArrayList<>();
        phaseElements.add("Seleccione un elemento");
        phaseElements.add("Entrada");
        phaseElements.add("Salida");

        ArrayAdapter adap = new ArrayAdapter(Form_IMP.this, android.R.layout.simple_spinner_item, phaseElements);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMovement.setAdapter(adap);

        spinnerMovement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                movementSelectedItem = spinnerMovement.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(movementSelectedItem );
                text.setTextColor(Color.BLACK); // set the text color
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_IMP.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerUnits = (Spinner) findViewById(R.id.spinnerUnits);
        ArrayList<String> phaseElements2 = new ArrayList<>();
        phaseElements2.add("Seleccione un elemento");
        phaseElements2.add("Litros");
        phaseElements2.add("Kilogramos");
        phaseElements2.add("Libras");
        phaseElements2.add("Mililitros");
        phaseElements2.add("Gramos");

        ArrayAdapter adap2 = new ArrayAdapter(Form_IMP.this, android.R.layout.simple_spinner_item, phaseElements2);
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
                Toast.makeText(Form_IMP.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        addFormButtom = findViewById(R.id.create_forms2_buttom);
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formsUtilities = new FormsUtilities();
                String rawMaterial, quantityMaterial, statusTools, existance, idGardenFb, nameForm;
                rawMaterial = rawMatirial.getText().toString();
                quantityMaterial = quantity.getText().toString();
                existance = existingTool.getText().toString();
                nameForm = formName.getText().toString();

                idGardenFb = getIntent().getStringExtra("idGardenFirebase");

                Map<String,Object> infoForm = new HashMap<>();
                infoForm.put("idForm",2);
                infoForm.put("nameForm",nameForm);
                infoForm.put("rawMaterial",rawMaterial);
                infoForm.put("concept",conceptSelectedItem);
                infoForm.put("movement",movementSelectedItem);
                infoForm.put("quantityRawMaterial",quantityMaterial);
                infoForm.put("units",unitSelectedItem);
                infoForm.put("existenceQuantity",existance);
                formsUtilities.createForm(Form_IMP.this,infoForm,idGardenFb);
                Toast.makeText(Form_IMP.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Form_IMP.this, HomeActivity.class));
                finish();
            }
        });



    }
}