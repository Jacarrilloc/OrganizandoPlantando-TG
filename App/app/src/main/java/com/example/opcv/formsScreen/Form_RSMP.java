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
import com.example.opcv.conectionInfo.NetworkMonitorService;
import com.example.opcv.fbComunication.FormsUtilities;
import com.example.opcv.localDatabase.DB_InsertForms;
import com.example.opcv.ludificationScreens.DictionaryHome;
import com.example.opcv.notifications.Notifications;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Recepción y salida de materia Prima
public class Form_RSMP extends AppCompatActivity {

    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile, ludification;

    private Spinner spinnerUnits, spinnerConcept;
    private String unitSelectedItem, conceptSelectedItem, watch, idGarden, idCollection;
    private FirebaseFirestore database;
    private EditText description, quantity, total, state;
    private TextView formName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rsmp);

        database = FirebaseFirestore.getInstance();
        formName = (TextView) findViewById(R.id.form_RSMP_name);
        description = (EditText) findViewById(R.id.description);
        quantity = (EditText) findViewById(R.id.amount_of_Mp);
        total = (EditText) findViewById(R.id.total_Mp);
        state = (EditText) findViewById(R.id.state);
        gardens = (Button) findViewById(R.id.gardens);
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        backButtom = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        spinnerConcept = (Spinner) findViewById(R.id.conceptChoice);
        spinnerUnits = (Spinner) findViewById(R.id.unitsChoice);
        addFormButtom = findViewById(R.id.create_forms4_buttom);

        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RSMP.this, MapsActivity.class));
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RSMP.this, HomeActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RSMP.this, EditUserActivity.class));
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(Form_RSMP.this, DictionaryHome.class);
                startActivity(edit);
            }
        });

        watch = getIntent().getStringExtra("watch");

        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
            spinnerUnits.setEnabled(false);
            spinnerConcept.setEnabled(false);
            description.setEnabled(false);
            quantity.setEnabled(false);
            total.setEnabled(false);
            state.setEnabled(false);
            quantity.setFocusable(false);
            quantity.setClickable(false);
            total.setFocusable(false);
            total.setClickable(false);
            state.setFocusable(false);
            state.setClickable(false);
            description.setFocusable(false);
            description.setClickable(false);
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
                    if(validateField(descriptionR, quantityR, totalR, stateR, conceptSelectedItem, unitSelectedItem)){
                        NetworkMonitorService connection = new NetworkMonitorService(Form_RSMP.this);

                        if(connection.isOnline(Form_RSMP.this)){
                            formsUtilities.createForm(Form_RSMP.this,infoForm,idGardenFb);
                        }

                        DB_InsertForms newForm = new DB_InsertForms(Form_RSMP.this);
                        //newForm.insertInto_RSMP(infoForm);
                        Notifications notifications = new Notifications();
                        notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_RSMP.this);
                        //Toast.makeText(Form_RSMP.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Form_RSMP.this, HomeActivity.class));
                        finish();
                    }
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
    }

    private void showInfo(String idGarden, String idCollection, String status){

        CollectionReference ref = database.collection("Gardens").document(idGarden).collection("Forms");
        ref.document(idCollection).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                description.setText(task.getResult().get("description").toString());
                quantity.setText(task.getResult().get("quantity").toString());
                total.setText(task.getResult().get("total").toString());
                state.setText(task.getResult().get("state").toString());
                if(task.isSuccessful()){
                    if(status.equals("edit")){
                        String choice1 = task.getResult().get("concept").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        if(choice1.equals("Recepción")){
                            elementShow.add("Recepción");
                            elementShow.add("Salida");
                        } else if (choice1.equals("Salida")) {
                            elementShow.add("Salida");
                            elementShow.add("Recepción");
                        }
                        ArrayAdapter adap2 = new ArrayAdapter(Form_RSMP.this, android.R.layout.simple_spinner_item, elementShow);
                        spinnerConcept.setAdapter(adap2);

                        String choice2 = task.getResult().get("units").toString();
                        ArrayList<String> elementShow2 = new ArrayList<>();
                        if(choice2.equals("Litros")){
                            elementShow2.add("Litros");
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Libras");
                            elementShow2.add("Mililitros");
                            elementShow2.add("Gramos");
                        }
                        else if(choice2.equals("Kilogramos")){
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Litros");
                            elementShow2.add("Libras");
                            elementShow2.add("Mililitros");
                            elementShow2.add("Gramos");
                        }
                        else if(choice2.equals("Libras")){
                            elementShow2.add("Libras");
                            elementShow2.add("Litros");
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Mililitros");
                            elementShow2.add("Gramos");
                        }
                        else if(choice2.equals("Mililitros")){
                            elementShow2.add("Mililitros");
                            elementShow2.add("Litros");
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Libras");
                            elementShow2.add("Gramos");
                        }
                        else if(choice2.equals("Gramos")){
                            elementShow2.add("Gramos");
                            elementShow2.add("Litros");
                            elementShow2.add("Kilogramos");
                            elementShow2.add("Libras");
                            elementShow2.add("Mililitros");
                        }
                        ArrayAdapter adap = new ArrayAdapter(Form_RSMP.this, android.R.layout.simple_spinner_item, elementShow2);
                        spinnerUnits.setAdapter(adap);
                    }
                    else if(status.equals("true")){
                        String choice1 = task.getResult().get("concept").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        elementShow.add(choice1);
                        ArrayAdapter adap2 = new ArrayAdapter(Form_RSMP.this, android.R.layout.simple_spinner_item, elementShow);
                        spinnerConcept.setAdapter(adap2);
                        String choice2 = task.getResult().get("units").toString();
                        ArrayList<String> elementShow2 = new ArrayList<>();
                        elementShow2.add(choice2);
                        ArrayAdapter adap = new ArrayAdapter(Form_RSMP.this, android.R.layout.simple_spinner_item, elementShow2);
                        spinnerUnits.setAdapter(adap);
                    }
                }
            }
        });
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descriptionR, quantityR, totalR, stateR, concept, units;
                descriptionR = description.getText().toString();
                quantityR = quantity.getText().toString();
                totalR = total.getText().toString();
                stateR = state.getText().toString();
                concept = conceptSelectedItem;
                units = unitSelectedItem;
                if(validateField(descriptionR, quantityR, totalR, stateR, concept, units)){
                    formsUtilities.editInfoRSMP(Form_RSMP.this, idGarden, idCollection, descriptionR, quantityR, totalR, stateR, concept, units);
                    Toast.makeText(Form_RSMP.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateField(String descriptionR,String quantityR, String totalR, String stateR, String concept, String units){

        if(descriptionR.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la descripción", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(units.equals("Seleccione un elemento")){
            Toast.makeText(this, "Es necesario seleccionar una unidad", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(quantityR.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad de materia prima", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(totalR.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el total", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(concept.equals("Seleccione un elemento")){
            Toast.makeText(this, "Es necesario seleccionar un concepto", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(stateR.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el estado de la materia prima", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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