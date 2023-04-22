package com.example.opcv.formsScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
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
import com.example.opcv.business.formsLogic.FormsLogic;
import com.example.opcv.fbComunication.FormsUtilities;
import com.example.opcv.ludificationScreens.DictionaryHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Formulario Inventario de Materia Prima
public class Form_IMP extends AppCompatActivity {

    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile, ludification;
    private Spinner spinnerMovement, spinnerUnits, spinnerConcept;
    private String movementSelectedItem, unitSelectedItem, conceptSelectedItem, watch, idGarden, idCollection;
    private EditText rawMatirial, quantity, existingTool;
    private TextView formName;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_imp);

        database = FirebaseFirestore.getInstance();
        formName = (TextView) findViewById(R.id.form_IMP_name);
        myGardens = (Button) findViewById(R.id.myGardens);
        rawMatirial = (EditText) findViewById(R.id.rawMaterialContainer);
        quantity = (EditText) findViewById(R.id.quantity);
        existingTool = (EditText) findViewById(R.id.existanceTool);
        gardens = (Button) findViewById(R.id.gardens);
        profile = (Button) findViewById(R.id.profile);
        backButtom = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        spinnerConcept = (Spinner) findViewById(R.id.conceptChoice);
        spinnerMovement = (Spinner) findViewById(R.id.spinnerChoice);
        spinnerUnits = (Spinner) findViewById(R.id.spinnerUnits);
        addFormButtom = findViewById(R.id.create_forms2_buttom);

        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_IMP.this, MapsActivity.class));
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_IMP.this, HomeActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_IMP.this, EditUserActivity.class));
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(Form_IMP.this, DictionaryHome.class);
                startActivity(edit);
            }
        });

        watch = getIntent().getStringExtra("watch");
        System.out.println("es nulo"+watch);

        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
            spinnerConcept.setEnabled(false);
            spinnerMovement.setEnabled(false);
            spinnerUnits.setEnabled(false);
            rawMatirial.setEnabled(false);
            quantity.setEnabled(false);
            existingTool.setEnabled(false);
            rawMatirial.setFocusable(false);
            rawMatirial.setClickable(false);
            existingTool.setFocusable(false);
            existingTool.setClickable(false);
            quantity.setFocusable(false);
            quantity.setClickable(false);
            showInfo(idGarden, idCollection, "true");

        } else if (watch.equals("edit")) {
            formsUtilities = new FormsUtilities();

            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            showInfo(idGarden, idCollection, "edit");
            addFormButtom.setText("Aceptar cambios");
        }
        else {
           addFormButtom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    formsUtilities = new FormsUtilities();
                    idGarden = getIntent().getStringExtra("idGardenFirebase");
                    idCollection = getIntent().getStringExtra("idCollecion");
                    String rawMaterial, quantityMaterial, existance, idGardenFb, nameForm;
                    rawMaterial = rawMatirial.getText().toString();
                    quantityMaterial = quantity.getText().toString();
                    existance = existingTool.getText().toString();
                    nameForm = formName.getText().toString();

                    idGardenFb = getIntent().getStringExtra("idGardenFirebase");

                    Map<String,Object> infoForm = new HashMap<>();
                    infoForm.put("idForm",3);
                    infoForm.put("nameForm",nameForm);
                    infoForm.put("rawMaterial",rawMaterial);
                    infoForm.put("concept",conceptSelectedItem);
                    infoForm.put("movement",movementSelectedItem);
                    infoForm.put("quantityRawMaterial",quantityMaterial);
                    infoForm.put("units",unitSelectedItem);
                    infoForm.put("existenceQuantity",existance);

                    FormsLogic newForm = new FormsLogic(Form_IMP.this);
                    newForm.createForm(infoForm,idGardenFb);

                    //newForm.insertInto_IMP(infoForm);
                    Toast.makeText(Form_IMP.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Form_IMP.this, HomeActivity.class));
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


    }
    private void showInfo(String idGarden, String idCollection, String status){


        CollectionReference ref = database.collection("Gardens").document(idGarden).collection("Forms");
        ref.document(idCollection).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                rawMatirial.setText(task.getResult().get("rawMaterial").toString());
                quantity.setText(task.getResult().get("quantityRawMaterial").toString());
                existingTool.setText(task.getResult().get("existenceQuantity").toString());
                if(task.isSuccessful()){
                    if(status.equals("edit")){
                        String choice1 = task.getResult().get("concept").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        if(choice1.equals("Donación")){
                            elementShow.add("Donación");
                            elementShow.add("Compra");
                            elementShow.add("Salida");
                        }
                        else if(choice1.equals("Compra")){
                            elementShow.add("Compra");
                            elementShow.add("Donación");
                            elementShow.add("Salida");
                        }
                        else if(choice1.equals("Salida")){
                            elementShow.add("Salida");
                            elementShow.add("Donación");
                            elementShow.add("Compra");
                        }
                        ArrayAdapter adap2 = new ArrayAdapter(Form_IMP.this, android.R.layout.simple_spinner_item, elementShow);
                        spinnerConcept.setAdapter(adap2);
                        String choice2 = task.getResult().get("movement").toString();
                        ArrayList<String> elementShow2 = new ArrayList<>();
                        if(choice2.equals("Entrada")){
                            elementShow2.add("Entrada");
                            elementShow2.add("Salida");
                        }
                        else if(choice2.equals("Salida")){
                            elementShow2.add("Salida");
                            elementShow2.add("Entrada");
                        }
                        ArrayAdapter adap = new ArrayAdapter(Form_IMP.this, android.R.layout.simple_spinner_item, elementShow2);
                        spinnerMovement.setAdapter(adap);
                        String choice3 = task.getResult().get("units").toString();
                        ArrayList<String> elementShow3 = new ArrayList<>();
                        if(choice3.equals("Litros")){
                            elementShow3.add("Litros");
                            elementShow3.add("Kilogramos");
                            elementShow3.add("Libras");
                            elementShow3.add("Mililitros");
                            elementShow3.add("Gramos");
                        }
                        else if(choice3.equals("Kilogramos")){
                            elementShow3.add("Kilogramos");
                            elementShow3.add("Litros");
                            elementShow3.add("Libras");
                            elementShow3.add("Mililitros");
                            elementShow3.add("Gramos");
                        }
                        else if(choice3.equals("Libras")){
                            elementShow3.add("Libras");
                            elementShow3.add("Litros");
                            elementShow3.add("Kilogramos");
                            elementShow3.add("Mililitros");
                            elementShow3.add("Gramos");
                        }
                        else if(choice3.equals("Mililitros")){
                            elementShow3.add("Mililitros");
                            elementShow3.add("Litros");
                            elementShow3.add("Kilogramos");
                            elementShow3.add("Libras");
                            elementShow3.add("Gramos");
                        }
                        else if(choice3.equals("Gramos")){
                            elementShow3.add("Gramos");
                            elementShow3.add("Litros");
                            elementShow3.add("Kilogramos");
                            elementShow3.add("Libras");
                            elementShow3.add("Mililitros");
                        }
                        ArrayAdapter adap3 = new ArrayAdapter(Form_IMP.this, android.R.layout.simple_spinner_item, elementShow3);
                        spinnerUnits.setAdapter(adap3);
                    }
                    else if(status.equals("true")){

                        String choice1 = task.getResult().get("concept").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        elementShow.add(choice1);
                        ArrayAdapter adap2 = new ArrayAdapter(Form_IMP.this, android.R.layout.simple_spinner_item, elementShow);
                        spinnerConcept.setAdapter(adap2);
                        String choice2 = task.getResult().get("movement").toString();
                        ArrayList<String> elementShow2 = new ArrayList<>();
                        elementShow2.add(choice2);
                        ArrayAdapter adap = new ArrayAdapter(Form_IMP.this, android.R.layout.simple_spinner_item, elementShow2);
                        spinnerMovement.setAdapter(adap);
                        String choice3 = task.getResult().get("units").toString();
                       // System.out.println("id colec"+ task.getResult().get("units").toString());
                        ArrayList<String> elementShow3 = new ArrayList<>();
                        elementShow3.add(choice3);
                        ArrayAdapter adap3 = new ArrayAdapter(Form_IMP.this, android.R.layout.simple_spinner_item, elementShow3);
                        spinnerUnits.setAdapter(adap3);
                    }
                }
            }
        });
        /*
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rawMaterial, quantityMaterial, existance, concept, movement, units;
                rawMaterial = rawMatirial.getText().toString();
                quantityMaterial = quantity.getText().toString();
                existance = existingTool.getText().toString();
                movement = movementSelectedItem;
                concept = conceptSelectedItem;
                units = unitSelectedItem;
                formsUtilities.editInfoIMP(Form_IMP.this, idGarden, idCollection, rawMaterial, concept,movement, quantityMaterial, units, existance);
                Toast.makeText(Form_IMP.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);
        super.attachBaseContext(newBase);
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration config = new Configuration(newConfig);
        adjustFontScale(getApplicationContext(), config);
    }
    public static void adjustFontScale(Context context, Configuration configuration) {
        if (configuration.fontScale != 1) {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }
}
