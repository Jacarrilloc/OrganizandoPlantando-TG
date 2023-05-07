package com.example.opcv.view.forms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

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

import org.json.JSONException;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Formulario Control de Inventario de Herramientas
public class Form_CIH extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsCommunication formsUtilities;
    private Button addFormButtom, rewards, myGardens, profile, ludification;
    private EditText tool, toolQuantity, toolStatus, preexistingTool;
    private TextView formName;
    private Spinner spinnerConcept, spinner2;
    private String conceptSelectedItem, selectedItem, watch, idGarden, idCollection;
    private FirebaseFirestore database;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cih);

        database = FirebaseFirestore.getInstance();
        rewards = (Button) findViewById(R.id.rewards);
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
        ludification = (Button) findViewById(R.id.ludification);

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_CIH.this, RewardHomeActivity.class));
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
            //Aqui es cuando solo se quiere ver la informción del formulario
            Map<String, Object> infoForm = (Map<String, Object>) getIntent().getSerializableExtra("idCollecion");
            showMapInfo(infoForm,watch);

        } else if (watch.equals("edit")) {
            //Aqui es cuando se quiere editar la Informacion del Usuario
            Map<String, Object> infoForm = (Map<String, Object>) getIntent().getSerializableExtra("idCollecion");
            showMapInfo(infoForm,watch);
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


        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(Form_CIH.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(Form_CIH.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watch = getIntent().getStringExtra("watch");
                if(watch.equals("create")){
                    if (ContextCompat.checkSelfPermission(Form_CIH.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Form_CIH.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Si no se han otorgado los permisos, solicítalos.
                        requestStoragePermission();
                    } else {
                        // El permiso ya ha sido concedido, crea la instancia de la clase Forms
                        createNewForm();
                    }
                }

                if(watch.equals("edit")){
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
    }

    private void showMapInfo(Map<String, Object> info,String status){
        switch (status){
            case "true":
                addFormButtom.setVisibility(View.GONE);
                spinner2.setEnabled(false);
                spinnerConcept.setEnabled(false);
                tool.setEnabled(false);
                toolQuantity.setEnabled(false);
                toolStatus.setEnabled(false);
                preexistingTool.setEnabled(false);
                tool.setText((CharSequence) info.get("tool"));
                toolQuantity.setText((CharSequence) info.get("toolQuantity"));
                toolStatus.setText((CharSequence) info.get("toolStatus"));
                preexistingTool.setText((CharSequence) info.get("existenceQuantity"));
                if (status.equals("true")) {
                    spinnerConcept.setVisibility(View.GONE);
                    spinner2.setVisibility(View.GONE);

                    TextView infoConcetSpinner = findViewById(R.id.spinnerConceptElementView);
                    TextView infoToolSpinner = findViewById(R.id.toolSpinnerInfo);

                    infoConcetSpinner.setVisibility(View.VISIBLE);
                    infoToolSpinner.setVisibility(View.VISIBLE);

                    String incomingOutgoingData = (String) info.get("incomingOutgoing");
                    String conceptData = (String) info.get("concept");

                    infoConcetSpinner.setText(conceptData);
                    infoToolSpinner.setText(incomingOutgoingData);
                }
                break;
            case "edit":
                addFormButtom.setText("Aceptar cambios");
                tool.setText((CharSequence) info.get("tool"));
                toolQuantity.setText((CharSequence) info.get("toolQuantity"));
                toolStatus.setText((CharSequence) info.get("toolStatus"));
                preexistingTool.setText((CharSequence) info.get("existenceQuantity"));

                ArrayList<String> phaseElements = new ArrayList<>();
                phaseElements.add("Seleccione un elemento");
                phaseElements.add("Devolución de herramienta");
                phaseElements.add("Prestamo");
                phaseElements.add("Reposición herramienta dañada");
                phaseElements.add("Compra herramienta");
                String concepValue = (String) info.get("concept");
                int positionConcept = phaseElements.indexOf(concepValue);
                spinnerConcept.setSelection(positionConcept);

                ArrayList<String> choiceElements = new ArrayList<>();
                choiceElements.add("Seleccione un elemento");
                choiceElements.add("Entradas");
                choiceElements.add("Salidas");
                String spinnerValue2 = (String) info.get("incomingOutgoing");
                int positionSpinner2 = choiceElements.indexOf(spinnerValue2);
                spinner2.setSelection(positionSpinner2);
                break;
            default:
                Log.i("Error","No se reconoce la Accion");
        }
    }

    private void updateForm(Map<String, Object> oldInfo) throws JSONException, IOException {
        Map<String, Object> newInfo = new HashMap<>();
        newInfo.put("CreatedBy",oldInfo.get("CreatedBy"));
        newInfo.put("Date",oldInfo.get("Date"));
        newInfo.put("idForm",oldInfo.get("idForm"));
        newInfo.put("nameForm",oldInfo.get("nameForm"));

        String tools, quantityTools, statusTools, toolExistance,idGardenFb;
        tools = tool.getText().toString();
        quantityTools = toolQuantity.getText().toString();
        statusTools = toolStatus.getText().toString();
        toolExistance = preexistingTool.getText().toString();

        newInfo.put("tool",tools);
        newInfo.put("concept",conceptSelectedItem);
        newInfo.put("incomingOutgoing",selectedItem);
        newInfo.put("toolQuantity",quantityTools);
        newInfo.put("toolStatus",statusTools);
        newInfo.put("existenceQuantity",toolExistance);

        idGardenFb = getIntent().getStringExtra("idGardenFirebase");
        Forms updateInfo = new Forms(this);
        updateInfo.updateInfoForm(oldInfo,newInfo,idGardenFb);

        Notifications notifications = new Notifications();
        notifications.notification("Formulario Editado", "Felicidades! Actualizaste la Información de tu Formulario", Form_CIH.this);

        onBackPressed();
    }

    private void createNewForm() {
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

        Forms newForm = new com.example.opcv.business.forms.Forms(Form_CIH.this);
        newForm.createFormInfo(infoForm, idGardenFb);

        Notifications notifications = new Notifications();
        notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_CIH.this);

        Toast.makeText(Form_CIH.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Form_CIH.this, HomeActivity.class));
        finish();
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
            }
        }
    }

    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        }
        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
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