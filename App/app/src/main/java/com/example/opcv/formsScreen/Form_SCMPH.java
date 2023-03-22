package com.example.opcv.formsScreen;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private String unitSelectedItem, itemSelectedItem, watch, idGarden, idCollection;
    private FirebaseFirestore database;
    private TextView formName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_scmph);

        database = FirebaseFirestore.getInstance();
        formName = (TextView) findViewById(R.id.form_SCMPH_name);
        itemName = (EditText) findViewById(R.id.nameItem);
        quantity = (EditText) findViewById(R.id.amount_of_Mp);
        total = (EditText) findViewById(R.id.total_Mp);
        gardens = (Button) findViewById(R.id.gardens);
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        backButtom = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        spinnerItem = (Spinner) findViewById(R.id.itemChoice);
        spinnerUnits = (Spinner) findViewById(R.id.unitsChoice);
        addFormButtom = findViewById(R.id.create_forms3_buttom);

        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_SCMPH.this, MapsActivity.class));
            }
        });


        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_SCMPH.this, HomeActivity.class));
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_SCMPH.this, EditUserActivity.class));
            }
        });

        watch = getIntent().getStringExtra("watch");

        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
            spinnerItem.setEnabled(false);
            spinnerUnits.setEnabled(false);
            itemName.setEnabled(false);
            quantity.setEnabled(false);
            total.setEnabled(false);
            showInfo(idGarden, idCollection, "true");
        } else if (watch.equals("edit")) {
            formsUtilities = new FormsUtilities();

            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            showInfo(idGarden, idCollection, "edit");
            addFormButtom.setText("Aceptar cambios");

        }
        else if (watch.equals("create")){
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

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


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

    }
    private void showInfo(String idGarden, String idCollection, String status){

        CollectionReference ref = database.collection("Gardens").document(idGarden).collection("Forms");
        ref.document(idCollection).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                itemName.setText(task.getResult().get("itemName").toString());
                quantity.setText(task.getResult().get("quantity").toString());
                total.setText(task.getResult().get("total").toString());
                if(task.isSuccessful()){
                    if(status.equals("edit")){
                        String choice1 = task.getResult().get("item").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        if(choice1.equals("Herramienta")){
                            elementShow.add("Herramienta");
                            elementShow.add("Materia prima");
                        } else if (choice1.equals("Materia prima")) {
                            elementShow.add("Materia prima");
                            elementShow.add("Herramienta");
                        }
                        ArrayAdapter adap2 = new ArrayAdapter(Form_SCMPH.this, android.R.layout.simple_spinner_item, elementShow);
                        spinnerItem.setAdapter(adap2);

                        String choice2 = task.getResult().get("units").toString();
                        ArrayList<String> elementShow2 = new ArrayList<>();
                        if(choice2.equals("Litros")){
                            elementShow2.add("Litros");
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Libras");
                            elementShow2.add("Mililitros");
                            elementShow2.add("Gramos");
                            elementShow2.add("No aplica");
                        }
                        else if(choice2.equals("Kilogramos")){
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Litros");
                            elementShow2.add("Libras");
                            elementShow2.add("Mililitros");
                            elementShow2.add("Gramos");
                            elementShow2.add("No aplica");
                        }
                        else if(choice2.equals("Libras")){
                            elementShow2.add("Libras");
                            elementShow2.add("Litros");
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Mililitros");
                            elementShow2.add("Gramos");
                            elementShow2.add("No aplica");
                        }
                        else if(choice2.equals("Mililitros")){
                            elementShow2.add("Mililitros");
                            elementShow2.add("Litros");
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Libras");
                            elementShow2.add("Gramos");
                            elementShow2.add("No aplica");
                        }
                        else if(choice2.equals("Gramos")){
                            elementShow2.add("Gramos");
                            elementShow2.add("Litros");
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Libras");
                            elementShow2.add("Mililitros");
                            elementShow2.add("No aplica");
                        }
                        else if(choice2.equals("No aplica")){
                            elementShow2.add("No aplica");
                            elementShow2.add("Litros");
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Libras");
                            elementShow2.add("Mililitros");
                            elementShow2.add("Gramos");
                        }
                        ArrayAdapter adap = new ArrayAdapter(Form_SCMPH.this, android.R.layout.simple_spinner_item, elementShow2);
                        spinnerUnits.setAdapter(adap);
                    }
                    else if(status.equals("true")){

                        String choice1 = task.getResult().get("item").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        elementShow.add(choice1);
                        ArrayAdapter adap2 = new ArrayAdapter(Form_SCMPH.this, android.R.layout.simple_spinner_item, elementShow);
                        spinnerItem.setAdapter(adap2);
                        String choice2 = task.getResult().get("units").toString();
                        ArrayList<String> elementShow2 = new ArrayList<>();
                        elementShow2.add(choice2);
                        ArrayAdapter adap = new ArrayAdapter(Form_SCMPH.this, android.R.layout.simple_spinner_item, elementShow2);
                        spinnerUnits.setAdapter(adap);

                    }
                }
            }
        });
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemR, quantityR, totalR, item, units;
                itemR = itemName.getText().toString();
                quantityR = quantity.getText().toString();
                totalR = total.getText().toString();
                item = itemSelectedItem;
                units = unitSelectedItem;
                formsUtilities.editInfoSCMPH(Form_SCMPH.this, idGarden, idCollection, itemR, item, units, quantityR, totalR);
                Toast.makeText(Form_SCMPH.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}