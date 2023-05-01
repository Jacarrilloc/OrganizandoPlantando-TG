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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Formulario Recepcion y requisicion de herramientas
public class Form_RRH extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsCommunication formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile, ludification;
    private EditText description, quantity, performedBy, status;
    private TextView formName;
    private Spinner spinner;
    private String conceptSelectedItem, watch, idGarden, idCollection;
    private FirebaseFirestore database;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rrh);

        database = FirebaseFirestore.getInstance();
        gardens = (Button) findViewById(R.id.gardens);
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        formName = (TextView) findViewById(R.id.form_RRH_name);
        quantity = (EditText) findViewById(R.id.toolsQuantity);
        description = (EditText) findViewById(R.id.description);
        performedBy = (EditText) findViewById(R.id.performedBy);
        status = (EditText) findViewById(R.id.status);
        spinner = (Spinner) findViewById(R.id.spinnerConcept);
        addFormButtom = findViewById(R.id.create_forms1_buttom);

        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RRH.this, MapsActivity.class));
            }
        });


        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RRH.this, HomeActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RRH.this, EditUserActivity.class));
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(Form_RRH.this, DictionaryHomeActivity.class);
                startActivity(edit);
            }
        });

        watch = getIntent().getStringExtra("watch");

        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
            spinner.setEnabled(false);
            description.setEnabled(false);
            quantity.setEnabled(false);
            performedBy.setEnabled(false);
            status.setEnabled(false);
            quantity.setFocusable(false);
            quantity.setClickable(false);
            description.setFocusable(false);
            description.setClickable(false);
            performedBy.setFocusable(false);
            performedBy.setClickable(false);
            status.setFocusable(false);
            status.setClickable(false);
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
                    if (ContextCompat.checkSelfPermission(Form_RRH.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Form_RRH.this,
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


        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void showInfo(String idGarden, String idCollection, String status1){

        CollectionReference ref = database.collection("Gardens").document(idGarden).collection("Forms");
        ref.document(idCollection).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                description.setText(task.getResult().get("description").toString());
                quantity.setText(task.getResult().get("toolQuantity").toString());
                performedBy.setText(task.getResult().get("performedBy").toString());
                status.setText(task.getResult().get("toolStatus").toString());
                if(task.isSuccessful()){
                    if(status1.equals("edit")){
                        String choice1 = task.getResult().get("concept").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        if(choice1.equals("Recepción")){
                            elementShow.add("Recepción");
                            elementShow.add("Salida");
                        } else if (choice1.equals("Salida")) {
                            elementShow.add("Salida");
                            elementShow.add("Recepción");
                        }
                        ArrayAdapter adap2 = new ArrayAdapter(Form_RRH.this, android.R.layout.simple_spinner_item, elementShow);
                        spinner.setAdapter(adap2);
                    }
                    else if(status1.equals("true")){
                        String choice1 = task.getResult().get("concept").toString();
                        System.out.println("el concepto"+choice1);
                        ArrayList<String> elementShow = new ArrayList<>();
                        elementShow.add(choice1);
                        ArrayAdapter adap2 = new ArrayAdapter(Form_RRH.this, android.R.layout.simple_spinner_item, elementShow);
                        spinner.setAdapter(adap2);
                    }
                }
            }
        });
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String processDescription, toolQuantity, processPerformedBy, processStatus, concept;
                processDescription = description.getText().toString();
                toolQuantity = quantity.getText().toString();
                processPerformedBy = performedBy.getText().toString();
                processStatus = status.getText().toString();
                concept = conceptSelectedItem;
                if(validateField(processDescription, toolQuantity, processPerformedBy, processStatus, concept)){
                    formsUtilities.editInfoRRH(Form_RRH.this, idGarden, idCollection, processDescription, toolQuantity, processPerformedBy, processStatus, concept);
                    Toast.makeText(Form_RRH.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateField(String processDescription,String toolQuantity, String processPerformedBy, String processStatus, String concept){

        if(processDescription.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la descripción", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(concept.equals("Seleccione un elemento")){
            Toast.makeText(this, "Es necesario seleccionar un concepto", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(toolQuantity.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(processPerformedBy.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el nombre del responsable", Toast.LENGTH_SHORT).show();
            return false;
        }else if(processStatus.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el estado del proceso", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewForm(){
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
        if(validateField(processDescription, toolQuantity, processPerformedBy, processStatus, conceptSelectedItem)){

            Forms newForm = new com.example.opcv.business.forms.Forms(Form_RRH.this);
            newForm.createFormInfo(infoForm, idGardenFb);

            Notifications notifications = new Notifications();
            notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_RRH.this);

            //newForm.insertInto_RRH(infoForm);
            Toast.makeText(Form_RRH.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Form_RRH.this, HomeActivity.class));
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
}