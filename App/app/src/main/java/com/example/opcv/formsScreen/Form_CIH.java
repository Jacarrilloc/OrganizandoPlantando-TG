package com.example.opcv.formsScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//Formulario Control de Inventario de Herramientas
public class Form_CIH extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile;
    private EditText tool, toolQuantity, toolStatus, preexistingTool;
    private TextView formName;
    private Spinner spinnerConcept, spinner2;
    private String conceptSelectedItem, selectedItem, watch, idGarden, idCollection;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cih);

        database = FirebaseFirestore.getInstance();
        gardens = (Button) findViewById(R.id.gardens);
        addFormButtom = findViewById(R.id.create_forms1_buttom);
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        formName = (TextView) findViewById(R.id.form_CIH_name);
        tool = (EditText) findViewById(R.id.tool);
        toolQuantity = (EditText) findViewById(R.id.quantity);
        toolStatus = (EditText) findViewById(R.id.toolStatus);
        preexistingTool = (EditText) findViewById(R.id.existanceTool);
        spinnerConcept = (Spinner) findViewById(R.id.spinnerConcept);
        spinner2 = (Spinner) findViewById(R.id.spinnerChoice);

        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CIH.this, MapsActivity.class));
            }
        });
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CIH.this, HomeActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CIH.this, EditUserActivity.class));
            }
        });
        watch = getIntent().getStringExtra("watch");

        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
            spinner2.setEnabled(false);
            spinnerConcept.setEnabled(false);
            tool.setEnabled(false);
            toolQuantity.setEnabled(false);
            toolStatus.setEnabled(false);
            preexistingTool.setEnabled(false);
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
                        infoForm.put("incomingOutgoing",selectedItem);
                        infoForm.put("toolQuantity",quantityTools);
                        infoForm.put("toolStatus",statusTools);
                        infoForm.put("existenceQuantity",toolExistance);
                        formsUtilities.createForm(Form_CIH.this,infoForm,idGardenFb);
                        Toast.makeText(Form_CIH.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Form_CIH.this, HomeActivity.class));
                        finish();

                }
            });

        }
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

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    private void showInfo(String idGarden, String idCollection, String status){

        CollectionReference ref = database.collection("Gardens").document(idGarden).collection("Forms");
        ref.document(idCollection).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                tool.setText(task.getResult().get("tool").toString());
                toolQuantity.setText(task.getResult().get("toolQuantity").toString());
                toolStatus.setText(task.getResult().get("toolStatus").toString());
                preexistingTool.setText(task.getResult().get("existenceQuantity").toString());
                if(task.isSuccessful()){
                    if(status.equals("edit")){
                        String choice1 = task.getResult().get("incomingOutgoing").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        if(choice1.equals("Entradas")){
                            elementShow.add("Entradas");
                            elementShow.add("Salidas");
                        } else if (choice1.equals("Salidas")) {
                            elementShow.add("Salidas");
                            elementShow.add("Entradas");
                        }
                        ArrayAdapter adap2 = new ArrayAdapter(Form_CIH.this, android.R.layout.simple_spinner_item, elementShow);
                        spinner2.setAdapter(adap2);

                        String choice2 = task.getResult().get("concept").toString();
                        ArrayList<String> elementShow2 = new ArrayList<>();
                        if(choice2.equals("Devolución de herramienta")){
                            elementShow2.add("Devolución de herramienta");
                            elementShow2.add("Prestamo");
                            elementShow2.add("Reposición herramienta dañada");
                            elementShow2.add("Compra herramienta");
                        }
                        else if(choice2.equals("Prestamo")){
                            elementShow2.add("Prestamo");
                            elementShow2.add("Devolución de herramienta");
                            elementShow2.add("Reposición herramienta dañada");
                            elementShow2.add("Compra herramienta");
                        }
                        else if(choice2.equals("Reposición herramienta dañada")){
                            elementShow2.add("Reposición herramienta dañada");
                            elementShow2.add("Prestamo");
                            elementShow2.add("Devolución de herramienta");
                            elementShow2.add("Compra herramienta");
                        }
                        else if(choice2.equals("Compra herramienta")){
                            elementShow2.add("Compra herramienta");
                            elementShow2.add("Prestamo");
                            elementShow2.add("Devolución de herramienta");
                            elementShow2.add("Reposición herramienta dañada");
                        }

                        ArrayAdapter adap = new ArrayAdapter(Form_CIH.this, android.R.layout.simple_spinner_item, elementShow2);
                        spinnerConcept.setAdapter(adap);
                    }
                    else if(status.equals("true")){

                        String choice1 = task.getResult().get("incomingOutgoing").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        elementShow.add(choice1);
                        ArrayAdapter adap2 = new ArrayAdapter(Form_CIH.this, android.R.layout.simple_spinner_item, elementShow);
                        spinner2.setAdapter(adap2);
                        String choice2 = task.getResult().get("concept").toString();
                        ArrayList<String> elementShow2 = new ArrayList<>();
                        elementShow2.add(choice2);
                        ArrayAdapter adap = new ArrayAdapter(Form_CIH.this, android.R.layout.simple_spinner_item, elementShow2);
                        spinnerConcept.setAdapter(adap);

                    }
                }
            }
        });
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tools, quantityTools, statusTools, toolExistance, concept, incomeOutgo;
                tools = tool.getText().toString();
                quantityTools = toolQuantity.getText().toString();
                statusTools = toolStatus.getText().toString();
                toolExistance = preexistingTool.getText().toString();
                concept = conceptSelectedItem;
                incomeOutgo = selectedItem;
                formsUtilities.editInfoCIH(Form_CIH.this, idGarden, idCollection, tools, quantityTools, statusTools, toolExistance, concept, incomeOutgo);
                Toast.makeText(Form_CIH.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}