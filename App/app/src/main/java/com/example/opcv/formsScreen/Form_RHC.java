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

//Formulario Registro Historico Caja
public class Form_RHC extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile;
    private EditText responsable, code, units, measurement, totalCost, comments, itemName;
    private TextView formName;
    private Spinner spinnerConcept, spinnerType;
    private String conceptSelectedItem=null, selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rhc);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RHC.this, MapsActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RHC.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RHC.this, EditUserActivity.class));
            }
        });

        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);

        formName = (TextView) findViewById(R.id.form_RHC_name);

        responsable = (EditText) findViewById(R.id.responsable);
        code = (EditText) findViewById(R.id.code);
        units = (EditText) findViewById(R.id.units);
        itemName=(EditText) findViewById(R.id.itemName);
        measurement = (EditText) findViewById(R.id.measurement);
        totalCost = (EditText) findViewById(R.id.cost);
        comments = (EditText) findViewById(R.id.comments);

        spinnerConcept = (Spinner) findViewById(R.id.spinner1);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        ArrayList<String> phaseElements = new ArrayList<>();
        phaseElements.add("Seleccione un elemento");
        phaseElements.add("Ingreso");
        phaseElements.add("Egreso");
        ArrayAdapter adap = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, phaseElements);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConcept.setAdapter(adap);
        spinnerConcept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                conceptSelectedItem = spinnerConcept.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(conceptSelectedItem);
                text.setTextColor(Color.BLACK); // set the text color
                if(conceptSelectedItem != null){
                    if(conceptSelectedItem.equals("Ingreso")){
                        ArrayList<String> choiceElementsIncome = new ArrayList<>();
                        choiceElementsIncome.add("Seleccione un elemento");
                        choiceElementsIncome.add("Venta");
                        choiceElementsIncome.add("Donativo");
                        choiceElementsIncome.add("Mano de obra");
                        choiceElementsIncome.add("Aporte comunitario");
                        choiceElementsIncome.add("Otro");
                        ArrayAdapter adap2 = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, choiceElementsIncome);
                        adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerType.setAdapter(adap2);
                    }
                    else if(conceptSelectedItem.equals("Egreso")){
                        ArrayList<String> choiceElementsExpenses = new ArrayList<>();
                        choiceElementsExpenses.add("Seleccione un elemento");
                        choiceElementsExpenses.add("Compra");
                        choiceElementsExpenses.add("Obligación fiscal");
                        choiceElementsExpenses.add("Cobro");
                        choiceElementsExpenses.add("Devolución");
                        choiceElementsExpenses.add("Otro");
                        ArrayAdapter adap2 = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, choiceElementsExpenses);
                        adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerType.setAdapter(adap2);
                    }
                }
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedType = spinnerType.getAdapter().getItem(i).toString();
                        TextView text = (TextView) view;
                        text.setText(selectedType);
                        text.setTextColor(Color.BLACK); // set the text color
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(Form_RHC.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_RHC.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        addFormButtom = findViewById(R.id.create_forms1_buttom);
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formsUtilities = new FormsUtilities();
                String personResponsable, codeC, itemNameC, unitsC, measurementC, totalCostC, commentsC, idGardenFb, nameForm;
                personResponsable = responsable.getText().toString();
                codeC = code.getText().toString();
                itemNameC = itemName.getText().toString();
                unitsC = units.getText().toString();
                measurementC = measurement.getText().toString();
                totalCostC = totalCost.getText().toString();
                commentsC = comments.getText().toString();
                nameForm = formName.getText().toString();

                idGardenFb = getIntent().getStringExtra("idGardenFirebase");

                Map<String,Object> infoForm = new HashMap<>();
                infoForm.put("idForm",11);
                infoForm.put("nameForm",nameForm);
                infoForm.put("responsable",personResponsable);
                infoForm.put("income/expense",conceptSelectedItem);
                infoForm.put("type",selectedType);
                infoForm.put("code",codeC);
                infoForm.put("itemName",itemNameC);
                infoForm.put("measurement",measurementC);
                infoForm.put("totalCost",totalCostC);
                infoForm.put("comments",commentsC);
                infoForm.put("units",unitsC);
                formsUtilities.createForm(Form_RHC.this,infoForm,idGardenFb);
                Toast.makeText(Form_RHC.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Form_RHC.this, HomeActivity.class));
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