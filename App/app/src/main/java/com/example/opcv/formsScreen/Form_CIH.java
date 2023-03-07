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

//Formulario Control de Inventario de Herramientas
public class Form_CIH extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile;
    private EditText tool, toolQuantity, toolStatus, preexistingTool;
    private TextView formName;
    private Spinner spinnerConcept, spinner2;
    private String conceptSelectedItem, selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cih);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CIH.this, VegetablePatchAvailableActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CIH.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CIH.this, EditUserActivity.class));
            }
        });

        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);

        formName = (TextView) findViewById(R.id.form_CIH_name);

        tool = (EditText) findViewById(R.id.tool);
        toolQuantity = (EditText) findViewById(R.id.quantity);
        toolStatus = (EditText) findViewById(R.id.toolStatus);
        preexistingTool = (EditText) findViewById(R.id.existanceTool);

        spinnerConcept = (Spinner) findViewById(R.id.spinnerConcept);
        ArrayList<String> phaseElements = new ArrayList<>();
        phaseElements.add("Seleccione un elemento");
        phaseElements.add("Devolución de herramienta");
        phaseElements.add("Prestamo");
        phaseElements.add("Reposición herramienta dañada");
        phaseElements.add("Compra herramienta");
        ArrayAdapter adap = new ArrayAdapter(Form_CIH.this, android.R.layout.simple_spinner_item, phaseElements);
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
                Toast.makeText(Form_CIH.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        spinner2 = (Spinner) findViewById(R.id.spinnerChoice);
        ArrayList<String> choiceElements = new ArrayList<>();
        choiceElements.add("Seleccione un elemento");
        choiceElements.add("Entradas");
        choiceElements.add("Salidas");
        ArrayAdapter adap2 = new ArrayAdapter(Form_CIH.this, android.R.layout.simple_spinner_item, choiceElements);
        adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adap2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = spinner2.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(selectedItem);
                text.setTextColor(Color.BLACK); // set the text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_CIH.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        addFormButtom = findViewById(R.id.create_forms1_buttom);
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formsUtilities = new FormsUtilities();
                String tools, quantityTools, statusTools, toolExistance, idGardenFb, nameForm;
                tools = tool.getText().toString();
                quantityTools = toolQuantity.getText().toString();
                statusTools = toolStatus.getText().toString();
                toolExistance = preexistingTool.getText().toString();
                nameForm = formName.getText().toString();

                idGardenFb = getIntent().getStringExtra("idGardenFirebase");

                Map<String,Object> infoForm = new HashMap<>();
                infoForm.put("idForm",10);
                infoForm.put("nameForm",nameForm);
                infoForm.put("tool",tools);
                infoForm.put("concept",conceptSelectedItem);
                infoForm.put("incoming/outgoing",selectedItem);
                infoForm.put("toolQuantity",quantityTools);
                infoForm.put("toolStatus",statusTools);
                infoForm.put("existenceQuantity",toolExistance);
                formsUtilities.createForm(Form_CIH.this,infoForm,idGardenFb);
                Toast.makeText(Form_CIH.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Form_CIH.this, HomeActivity.class));
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