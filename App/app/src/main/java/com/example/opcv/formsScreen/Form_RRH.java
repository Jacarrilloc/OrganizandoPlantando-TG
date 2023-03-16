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

import com.example.opcv.MapsActivity;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.fbComunication.FormsUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Formulario Recepcion y requisicion de herramientas
public class Form_RRH extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile;
    private EditText description, quantity, performedBy, status;
    private TextView formName;
    private Spinner spinner;
    private String conceptSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rrh);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RRH.this, MapsActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RRH.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RRH.this, EditUserActivity.class));
            }
        });

        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);

        formName = (TextView) findViewById(R.id.form_RRH_name);

        quantity = (EditText) findViewById(R.id.toolsQuantity);
        description = (EditText) findViewById(R.id.description);
        performedBy = (EditText) findViewById(R.id.performedBy);
        status = (EditText) findViewById(R.id.status);

        spinner = (Spinner) findViewById(R.id.spinnerConcept);
        ArrayList<String> phaseElements = new ArrayList<>();
        phaseElements.add("Seleccione un elemento");
        phaseElements.add("Recepción");
        phaseElements.add("Salida");

        ArrayAdapter adap = new ArrayAdapter(Form_RRH.this, android.R.layout.simple_spinner_item, phaseElements);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adap);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                conceptSelectedItem = spinner.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(conceptSelectedItem);
                text.setTextColor(Color.BLACK); // set the text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_RRH.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });
        addFormButtom = findViewById(R.id.create_forms1_buttom);
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formsUtilities = new FormsUtilities();
                String processDescription, toolQuantity, processPerformedBy, processStatus, idGardenFb, nameForm;
                processDescription = description.getText().toString();
                toolQuantity = quantity.getText().toString();
                processPerformedBy = performedBy.getText().toString();
                processStatus = status.getText().toString();
                nameForm = formName.getText().toString();

                idGardenFb = getIntent().getStringExtra("idGardenFirebase");

                Map<String,Object> infoForm = new HashMap<>();
                infoForm.put("idForm",9);
                infoForm.put("nameForm",nameForm);
                infoForm.put("description",processDescription);
                infoForm.put("toolQuantity",toolQuantity);
                infoForm.put("concept",conceptSelectedItem);
                infoForm.put("performedBy",processPerformedBy);
                infoForm.put("toolStatus",processStatus);
                formsUtilities.createForm(Form_RRH.this,infoForm,idGardenFb);
                Toast.makeText(Form_RRH.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Form_RRH.this, HomeActivity.class));
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