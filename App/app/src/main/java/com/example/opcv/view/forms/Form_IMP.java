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
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.model.persistance.firebase.FormsCommunication;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Formulario Inventario de Materia Prima
public class Form_IMP extends AppCompatActivity {

    private FloatingActionButton backButtom;
    private FormsCommunication formsUtilities;
    private Button addFormButtom, rewards, myGardens, profile, ludification;
    private Spinner spinnerMovement, spinnerUnits, spinnerConcept;
    private String movementSelectedItem, unitSelectedItem, conceptSelectedItem, watch, idGarden, idCollection;
    private EditText rawMatirial, quantity, existingTool;
    private TextView formName;
    private FirebaseFirestore database;

    private static final int REQUEST_STORAGE_PERMISSION = 1;

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
        rewards = (Button) findViewById(R.id.rewards);
        profile = (Button) findViewById(R.id.profile);
        backButtom = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        spinnerConcept = (Spinner) findViewById(R.id.conceptChoice);
        spinnerMovement = (Spinner) findViewById(R.id.spinnerChoice);
        spinnerUnits = (Spinner) findViewById(R.id.spinnerUnits);
        addFormButtom = findViewById(R.id.create_forms2_buttom);
        ludification = (Button) findViewById(R.id.ludification);

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_IMP.this, RewardHomeActivity.class));
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

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(Form_IMP.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(Form_IMP.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
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
                    if (ContextCompat.checkSelfPermission(Form_IMP.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Form_IMP.this,
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

    private void showMapInfo(Map<String, Object> info,String status){
        if(info != null) {
            EditText showConceptInfo = findViewById(R.id.showConceptInfo);
            EditText rawMaterialChoise = findViewById(R.id.rawMaterialChoise);
            EditText unitSelected = findViewById(R.id.UnitSelectedForm);
            rawMatirial.setText((CharSequence) info.get("rawMaterial"));
            quantity.setText((CharSequence) info.get("quantityRawMaterial"));
            existingTool.setText((CharSequence) info.get("existenceQuantity"));
            switch (status) {
                case "true":
                    spinnerConcept.setVisibility(View.GONE);
                    spinnerMovement.setVisibility(View.GONE);
                    spinnerUnits.setVisibility(View.GONE);
                    rawMatirial.setEnabled(false);
                    quantity.setEnabled(false);
                    existingTool.setEnabled(false);
                    rawMatirial.setFocusable(false);
                    rawMatirial.setClickable(false);
                    existingTool.setFocusable(false);
                    existingTool.setClickable(false);
                    quantity.setFocusable(false);
                    quantity.setClickable(false);
                    showConceptInfo.setVisibility(View.VISIBLE);
                    showConceptInfo.setText((String) info.get("concept"));
                    rawMaterialChoise.setVisibility(View.VISIBLE);
                    rawMaterialChoise.setText((String) info.get("movement"));
                    unitSelected.setVisibility(View.VISIBLE);
                    unitSelected.setText((String) info.get("units"));
                    showConceptInfo.setFocusable(false);
                    rawMaterialChoise.setFocusable(false);
                    unitSelected.setFocusable(false);
                    addFormButtom.setVisibility(View.GONE);
                    break;
                case "edit":
                    addFormButtom.setText("Aceptar cambios");
                    break;
            }
        }
    }

    private void createNewForm() {
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

        Forms newForm = new com.example.opcv.business.forms.Forms(Form_IMP.this);
        newForm.createFormInfo(infoForm, idGardenFb);

        Notifications notifications = new Notifications();
        notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_IMP.this);
        //newForm.insertInto_IMP(infoForm);
        Toast.makeText(Form_IMP.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Form_IMP.this, HomeActivity.class));
        finish();
    }

    private void updateForm(Map<String, Object> oldInfo) throws JSONException, IOException{
        Map<String, Object> newInfo = new HashMap<>();
        newInfo.put("CreatedBy",oldInfo.get("CreatedBy"));
        newInfo.put("Date",oldInfo.get("Date"));
        newInfo.put("idForm",oldInfo.get("idForm"));
        newInfo.put("nameForm",oldInfo.get("nameForm"));

        String rawMaterial, quantityMaterial, existance;
        rawMaterial = rawMatirial.getText().toString();
        quantityMaterial = quantity.getText().toString();
        existance = existingTool.getText().toString();

        idGarden = getIntent().getStringExtra("idGardenFirebase");

        String defaultSelected = "Seleccione un elemento";

        if(conceptSelectedItem.equals(defaultSelected)){
            newInfo.put("concept",oldInfo.get("concept"));
        }else{
            newInfo.put("concept",conceptSelectedItem);
        }

        if(movementSelectedItem.equals(defaultSelected)){
            newInfo.put("movement",oldInfo.get("movement"));
        }else{
            newInfo.put("movement",movementSelectedItem);
        }

        if(unitSelectedItem.equals(defaultSelected)){
            newInfo.put("units",oldInfo.get("units"));
        }else{
            newInfo.put("units",unitSelectedItem);
        }

        newInfo.put("rawMaterial",rawMaterial);
        newInfo.put("quantityRawMaterial",quantityMaterial);
        newInfo.put("existenceQuantity",existance);

        Forms updateInfo = new Forms(this);
        updateInfo.updateInfoForm(oldInfo,newInfo,idGarden);

        Notifications notifications = new Notifications();
        notifications.notification("Formulario Editado", "Felicidades! Actualizaste la Información de tu Formulario", Form_IMP.this);

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //Aqui se le da el porque del permiso
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
}
