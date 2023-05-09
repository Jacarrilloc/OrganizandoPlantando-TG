package com.example.opcv.view.forms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.opcv.business.forms.Forms;
import com.example.opcv.view.gardens.MapsActivity;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.business.persistance.firebase.FormsCommunication;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Formulario Control Proceso de Siembra
public class Form_CPS extends AppCompatActivity {

    private FloatingActionButton backButtom;
    private FormsCommunication formsUtilities;
    private Button addFormButtom, rewards, myGardens, profile, ludification;
    private EditText responsable, phase, duration, plantsSeeds, comments;
    private TextView formName;
    private Spinner spinner;
    private String phaseSelectedItem, watch, idGarden, idCollection;
    private FirebaseFirestore database;

    private static final int REQUEST_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cps);

        database = FirebaseFirestore.getInstance();
        rewards = (Button) findViewById(R.id.rewards);
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        formName = (TextView) findViewById(R.id.form_CPS_name);
        responsable = (EditText) findViewById(R.id.responsable);
        //phase = (EditText) findViewById(R.id.phase);
        duration = (EditText) findViewById(R.id.timeDuration);
        plantsSeeds = (EditText) findViewById(R.id.plants_or_seeds);
        comments = (EditText) findViewById(R.id.observation_or_comments);
        spinner = (Spinner) findViewById(R.id.spinnerPhase);
        addFormButtom = findViewById(R.id.create_forms1_buttom);
        ludification = (Button) findViewById(R.id.ludification);

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CPS.this, RewardHomeActivity.class));
            }
        });
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CPS.this, HomeActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CPS.this, EditUserActivity.class));
            }
        });
        watch = getIntent().getStringExtra("watch");
        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
            responsable.setEnabled(false);
            duration.setEnabled(false);
            plantsSeeds.setEnabled(false);
            comments.setEnabled(false);
            spinner.setEnabled(false);
            responsable.setFocusable(false);
            responsable.setClickable(false);
            duration.setFocusable(false);
            duration.setClickable(false);
            plantsSeeds.setFocusable(false);
            plantsSeeds.setClickable(false);
            comments.setFocusable(false);
            comments.setClickable(false);
            showInfo(idGarden, idCollection, "true");

        } else if (watch.equals("edit")) {
            formsUtilities = new FormsCommunication();
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            showInfo(idGarden, idCollection, "edit");
            addFormButtom.setText("Aceptar cambios");
        }
        else if (watch.equals("create")){
            addFormButtom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(Form_CPS.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Form_CPS.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Si no se han otorgado los permisos, solicítalos.
                        requestStoragePermission();
                    } else {
                        // El permiso ya ha sido concedido, crea la instancia de la clase Forms
                        createNewForm();
                    }
                }
            });
        }

        ArrayList<String> phaseElements = new ArrayList<>();
        phaseElements.add("Seleccione un elemento");
        phaseElements.add("Planeación");
        phaseElements.add("Establecer zona de plantación");
        phaseElements.add("Preparación de zona de plantación");
        phaseElements.add("Establecer método de plantación según semillas o plántulas");
        phaseElements.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
        phaseElements.add("Regado de las semillas y plántulas");
        phaseElements.add("Verificar el tamaño de la planta");
        phaseElements.add("Cosecha");
        ArrayAdapter adap = new ArrayAdapter(Form_CPS.this, android.R.layout.simple_spinner_item, phaseElements);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adap);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                phaseSelectedItem = spinner.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(phaseSelectedItem);
                text.setTextColor(Color.BLACK); // set the text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_CPS.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(Form_CPS.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(Form_CPS.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
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
                responsable.setText(task.getResult().get("personResponsable").toString());
                duration.setText(task.getResult().get("phaseDuration").toString());
                plantsSeeds.setText(task.getResult().get("plants or seeds").toString());
                comments.setText(task.getResult().get("commentsObservations").toString());
                if(task.isSuccessful()){
                    if(status.equals("edit")){
                        String choice1 = task.getResult().get("processPhase").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        if(choice1.equals("Planeación")){
                            elementShow.add("Planeación");
                            elementShow.add("Establecer zona de plantación");
                            elementShow.add("Preparación de zona de plantación");
                            elementShow.add("Establecer método de plantación según semillas o plántulas");
                            elementShow.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
                            elementShow.add("Regado de las semillas y plántulas");
                            elementShow.add("Verificar el tamaño de la planta");
                            elementShow.add("Cosecha");
                        }
                        else if(choice1.equals("Establecer zona de plantación")){
                            elementShow.add("Establecer zona de plantación");
                            elementShow.add("Planeación");
                            elementShow.add("Preparación de zona de plantación");
                            elementShow.add("Establecer método de plantación según semillas o plántulas");
                            elementShow.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
                            elementShow.add("Regado de las semillas y plántulas");
                            elementShow.add("Verificar el tamaño de la planta");
                            elementShow.add("Cosecha");
                        }
                        else if(choice1.equals("Establecer método de plantación según semillas o plántulas")){
                            elementShow.add("Establecer método de plantación según semillas o plántulas");
                            elementShow.add("Establecer zona de plantación");
                            elementShow.add("Planeación");
                            elementShow.add("Preparación de zona de plantación");
                            elementShow.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
                            elementShow.add("Regado de las semillas y plántulas");
                            elementShow.add("Verificar el tamaño de la planta");
                            elementShow.add("Cosecha");
                        }
                        else if(choice1.equals("Preparación de zona de plantación")){
                            elementShow.add("Preparación de zona de plantación");
                            elementShow.add("Planeación");
                            elementShow.add("Establecer zona de plantación");
                            elementShow.add("Establecer método de plantación según semillas o plántulas");
                            elementShow.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
                            elementShow.add("Regado de las semillas y plántulas");
                            elementShow.add("Verificar el tamaño de la planta");
                            elementShow.add("Cosecha");
                        }
                        else if(choice1.equals("Revisar el crecimiento de semillas y/o plántulas y control de plántulas")){
                            elementShow.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
                            elementShow.add("Planeación");
                            elementShow.add("Establecer zona de plantación");
                            elementShow.add("Preparación de zona de plantación");
                            elementShow.add("Establecer método de plantación según semillas o plántulas");
                            elementShow.add("Regado de las semillas y plántulas");
                            elementShow.add("Verificar el tamaño de la planta");
                            elementShow.add("Cosecha");
                        }
                        else if(choice1.equals("Regado de las semillas y plántulas")){
                            elementShow.add("Regado de las semillas y plántulas");
                            elementShow.add("Planeación");
                            elementShow.add("Establecer zona de plantación");
                            elementShow.add("Preparación de zona de plantación");
                            elementShow.add("Establecer método de plantación según semillas o plántulas");
                            elementShow.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
                            elementShow.add("Verificar el tamaño de la planta");
                            elementShow.add("Cosecha");
                        }
                        else if(choice1.equals("Verificar el tamaño de la planta")){
                            elementShow.add("Verificar el tamaño de la planta");
                            elementShow.add("Planeación");
                            elementShow.add("Establecer zona de plantación");
                            elementShow.add("Preparación de zona de plantación");
                            elementShow.add("Establecer método de plantación según semillas o plántulas");
                            elementShow.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
                            elementShow.add("Regado de las semillas y plántulas");
                            elementShow.add("Cosecha");
                        }
                        else if(choice1.equals("Cosecha")){
                            elementShow.add("Cosecha");
                            elementShow.add("Planeación");
                            elementShow.add("Establecer zona de plantación");
                            elementShow.add("Preparación de zona de plantación");
                            elementShow.add("Establecer método de plantación según semillas o plántulas");
                            elementShow.add("Revisar el crecimiento de semillas y/o plántulas y control de plántulas");
                            elementShow.add("Regado de las semillas y plántulas");
                            elementShow.add("Verificar el tamaño de la planta");
                        }
                        ArrayAdapter adap2 = new ArrayAdapter(Form_CPS.this, android.R.layout.simple_spinner_item, elementShow);
                        spinner.setAdapter(adap2);
                    }
                    else if(status.equals("true")){
                        String choice1 = task.getResult().get("processPhase").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        elementShow.add(choice1);
                        ArrayAdapter adap2 = new ArrayAdapter(Form_CPS.this, android.R.layout.simple_spinner_item, elementShow);
                        spinner.setAdapter(adap2);
                    }
                }
            }
        });
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personRep, durationT, plantsOrSeed, commentsC, idGardenFb, nameForm, phase;
                personRep = responsable.getText().toString();
                durationT = duration.getText().toString();
                plantsOrSeed = plantsSeeds.getText().toString();
                commentsC = comments.getText().toString();
                phase = phaseSelectedItem;
                if(validateField(personRep, durationT, plantsOrSeed, commentsC, phase)){
                    formsUtilities.editInfoCPS(Form_CPS.this, idGarden, idCollection, personRep, durationT, plantsOrSeed, commentsC, phase);
                    Toast.makeText(Form_CPS.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean validateField(String personRep,String duration, String plantsOrSeed, String commentsC, String phase){

        if(personRep.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la persona responsable", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(duration.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la duración", Toast.LENGTH_SHORT).show();
            return false;
        }else if(plantsOrSeed.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la planta o semilla", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(commentsC.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar comentarios", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(phase.equals("Seleccione un elemento")){
            Toast.makeText(this, "Es necesario seleccionar una fase", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewForm() {
        String personResp, durationT, plantsOrSeeds, commentsC, idGardenFb, nameForm;
        personResp = responsable.getText().toString();
        durationT = duration.getText().toString();
        plantsOrSeeds = plantsSeeds.getText().toString();
        commentsC = comments.getText().toString();
        nameForm = formName.getText().toString();

        idGardenFb = getIntent().getStringExtra("idGardenFirebase");

        Map<String,Object> infoForm = new HashMap<>();
        infoForm.put("idForm",7);
        infoForm.put("nameForm",nameForm);
        infoForm.put("personResponsable",personResp);
        infoForm.put("processPhase",phaseSelectedItem);
        infoForm.put("phaseDuration",durationT);
        infoForm.put("plants or seeds",plantsOrSeeds);
        infoForm.put("commentsObservations",commentsC);
        if(validateField(personResp, durationT, plantsOrSeeds, commentsC, phaseSelectedItem)){

            Forms newForm = new com.example.opcv.business.forms.Forms(Form_CPS.this);
            newForm.createFormInfo(infoForm, idGardenFb);

            Notifications notifications = new Notifications();
            notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_CPS.this);

            Toast.makeText(Form_CPS.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Form_CPS.this, HomeActivity.class));
            finish();
        }
    }

    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Aquí puedes proporcionar una explicación al usuario sobre por qué necesitas el permiso.
            // Esta explicación solo se mostrará si el usuario ha denegado previamente los permisos.
        }
        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // El usuario concedió los permisos, continúa con la ejecución de la aplicación.
                createNewForm();
            } else {
                // El usuario denegó los permisos, muestra un mensaje apropiado.
                // También puedes proporcionar una opción para que
            }
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}