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
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.model.persistance.firebase.FormsCommunication;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Formulario Recepcion y requisicion de herramientas
public class Form_RRH extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsCommunication formsUtilities;
    private Button addFormButtom, rewards, myGardens, profile, ludification;
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
        rewards = (Button) findViewById(R.id.rewards);
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
        ludification = (Button) findViewById(R.id.ludification);

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RRH.this, RewardHomeActivity.class));
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

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(Form_RRH.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(Form_RRH.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        watch = getIntent().getStringExtra("watch");
        Map<String, Object> infoForm = (Map<String, Object>) getIntent().getSerializableExtra("idCollecion");
        showMapInfo(infoForm,watch);

        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watch = getIntent().getStringExtra("watch");
                if(watch.equals("create")) {
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
                if (watch.equals("edit")){
                    try {
                        updateForm((Map<String, Object>) getIntent().getSerializableExtra("idCollecion"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

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

    private void showMapInfo(Map<String, Object> info,String statusInfo){
        if (info != null) {
            description.setText((CharSequence) info.get("description"));
            quantity.setText((CharSequence) info.get("toolQuantity"));
            performedBy.setText((CharSequence) info.get("performedBy"));
            status.setText((CharSequence) info.get("toolStatus"));
            switch (statusInfo) {
                case "true":
                    addFormButtom.setVisibility(View.GONE);
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

                    spinner.setVisibility(View.GONE);
                    TextView conceptSelectedSpinner = findViewById(R.id.conceptSelectedSpinner);
                    conceptSelectedSpinner.setVisibility(View.VISIBLE);
                    conceptSelectedSpinner.setText((CharSequence) info.get("concept"));

                    break;
                case "edit":
                    addFormButtom.setText("Aceptar cambios");
                    break;
            }
        }
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

    private void updateForm(Map<String, Object> oldInfo) throws JSONException, IOException{
        Map<String, Object> newInfo = new HashMap<>();
        newInfo.put("CreatedBy", oldInfo.get("CreatedBy"));
        newInfo.put("Date", oldInfo.get("Date"));
        newInfo.put("idForm", oldInfo.get("idForm"));
        newInfo.put("nameForm", oldInfo.get("nameForm"));

        String defaultSelected = "Seleccione un elemento";
        if(conceptSelectedItem.equals(defaultSelected)){
            newInfo.put("concept",oldInfo.get("concept"));
        }else{
            newInfo.put("concept",conceptSelectedItem);
        }

        newInfo.put("description",description.getText().toString());
        newInfo.put("toolQuantity",quantity.getText().toString());
        newInfo.put("performedBy",performedBy.getText().toString());
        newInfo.put("toolStatus",status.getText().toString());

        String idGardenFb = getIntent().getStringExtra("idGardenFirebase");
        Forms updateInfo = new Forms(this);
        updateInfo.updateInfoForm(oldInfo,newInfo,idGardenFb);

        Notifications notifications = new Notifications();
        notifications.notification("Formulario Editado", "Felicidades! Actualizaste la Información de tu Formulario", Form_RRH.this);

        onBackPressed();
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