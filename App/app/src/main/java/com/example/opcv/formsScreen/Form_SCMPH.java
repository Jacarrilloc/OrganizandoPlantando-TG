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
import com.example.opcv.VegetablePatchAvailableActivity;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.fbComunication.FormsUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//Solicitud de compra e materia prima y/o herramientas
public class Form_SCMPH extends AppCompatActivity {

    private FormsUtilities formsUtilities;
    private FloatingActionButton backButtom;
    private Button addFormButtom, gardens, myGardens, profile;
    private EditText itemName, quantity, total;
    private Spinner spinnerUnits, spinnerItem;
    private String unitSelectedItem, itemSelectedItem;
    private TextView formName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_scmph);

        formName = (TextView) findViewById(R.id.form_SCMPH_name);
        itemName = (EditText) findViewById(R.id.nameItem);
        quantity = (EditText) findViewById(R.id.amount_of_Mp);
        total = (EditText) findViewById(R.id.total_Mp);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_SCMPH.this, MapsActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_SCMPH.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_SCMPH.this, EditUserActivity.class));
            }
        });

        backButtom = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormOnetoFormListElement);

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        spinnerItem = (Spinner) findViewById(R.id.itemChoice);
        ArrayList<String> phaseElements = new ArrayList<>();
        phaseElements.add("Seleccione un elemento");
        phaseElements.add("Herramienta");
        phaseElements.add("Materia prima");

        ArrayAdapter adap = new ArrayAdapter(Form_SCMPH.this, android.R.layout.simple_spinner_item, phaseElements);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItem.setAdapter(adap);

        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelectedItem = spinnerItem.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(itemSelectedItem);
                text.setTextColor(Color.BLACK); // set the text color
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_SCMPH.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
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
        phaseElements2.add("No aplica");

        ArrayAdapter adap2 = new ArrayAdapter(Form_SCMPH.this, android.R.layout.simple_spinner_item, phaseElements2);
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
                Toast.makeText(Form_SCMPH.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        addFormButtom = findViewById(R.id.create_forms3_buttom);
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formsUtilities = new FormsUtilities();
                String itemR, quantityR, totalR, stateR, nameForm, idGardenFb;


                itemR = itemName.getText().toString();
                quantityR = quantity.getText().toString();
                totalR = total.getText().toString();
                nameForm = formName.getText().toString();

                idGardenFb = getIntent().getStringExtra("idGardenFirebase");

                Map<String,Object> infoForm = new HashMap<>();
                infoForm.put("idForm",3);
                infoForm.put("nameForm",nameForm);
                infoForm.put("itemName",itemR);
                infoForm.put("item",itemSelectedItem);
                infoForm.put("units",unitSelectedItem);
                infoForm.put("quantity",quantityR);
                infoForm.put("total",totalR);
                formsUtilities.createForm(Form_SCMPH.this,infoForm,idGardenFb);
                Toast.makeText(Form_SCMPH.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Form_SCMPH.this, HomeActivity.class));
                finish();
            }
        });
    }
}