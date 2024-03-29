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


//Solicitud de compra de materia prima y/o herramientas
public class Form_SCMPH extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private Button addFormButtom, rewards, myGardens, profile, ludification;
    private EditText itemName, quantity, total;
    private Spinner spinnerUnits, spinnerItem;
    private String unitSelectedItem, itemSelectedItem, watch, idGarden, idCollection;
    private FirebaseFirestore database;
    private TextView formName;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_scmph);

        database = FirebaseFirestore.getInstance();
        formName = (TextView) findViewById(R.id.form_SCMPH_name);
        itemName = (EditText) findViewById(R.id.nameItem);
        quantity = (EditText) findViewById(R.id.amount_of_Mp);
        total = (EditText) findViewById(R.id.total_Mp);
        rewards = (Button) findViewById(R.id.rewards);
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        backButtom = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        spinnerItem = (Spinner) findViewById(R.id.itemChoice);
        spinnerUnits = (Spinner) findViewById(R.id.unitsChoice);
        addFormButtom = findViewById(R.id.create_forms3_buttom);
        ludification = (Button) findViewById(R.id.ludification);


        watch = getIntent().getStringExtra("watch");
        Map<String, Object> infoForm = (Map<String, Object>) getIntent().getSerializableExtra("idCollecion");
        showMapInfo(infoForm,watch);

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_SCMPH.this, RewardHomeActivity.class));
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

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(Form_SCMPH.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(Form_SCMPH.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watch = getIntent().getStringExtra("watch");
                if(watch.equals("create")) {
                    if (ContextCompat.checkSelfPermission(Form_SCMPH.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Form_SCMPH.this,
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

    private boolean validateField(String itemR,String quantityR, String totalR, String item, String units){

        if(itemR.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el nombre del ítem", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(item.equals("Seleccione un elemento")){
            Toast.makeText(this, "Es necesario seleccionar un ítem", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(units.equals("Seleccione un elemento")){
            Toast.makeText(this, "Es necesario seleccionar las unidades", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(quantityR.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad de materia prima/herramientas", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(totalR.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el total", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewForm(){
        String itemR, quantityR, totalR, nameForm, idGardenFb;

        itemR = itemName.getText().toString();
        quantityR = quantity.getText().toString();
        totalR = total.getText().toString();
        nameForm = formName.getText().toString();

        idGardenFb = getIntent().getStringExtra("idGardenFirebase");

        Map<String,Object> infoForm = new HashMap<>();
        infoForm.put("idForm",2);
        infoForm.put("nameForm",nameForm);
        infoForm.put("itemName",itemR);
        infoForm.put("item",itemSelectedItem);
        infoForm.put("units",unitSelectedItem);
        infoForm.put("quantity",quantityR);
        infoForm.put("total",totalR);
        if(validateField(itemR, quantityR, totalR, itemSelectedItem, unitSelectedItem)){

            Forms newForm = new com.example.opcv.business.forms.Forms(Form_SCMPH.this);
            newForm.createFormInfo(infoForm, idGardenFb);

            Notifications notifications = new Notifications();
            notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_SCMPH.this);

            // newForm.insertInto_SCMPH(infoForm);
            Toast.makeText(Form_SCMPH.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Form_SCMPH.this, HomeActivity.class));
            finish();
        }
    }

    private void showMapInfo(Map<String, Object> info,String status){
        if(info != null) {
            itemName.setText((CharSequence) info.get("itemName"));
            quantity.setText((CharSequence) info.get("quantity"));
            total.setText((CharSequence) info.get("total"));
        }
        switch (status){
            case "true":
                EditText itemChoiseSelected = findViewById(R.id.itemChoiseSelected);
                itemChoiseSelected.setVisibility(View.VISIBLE);
                itemChoiseSelected.setFocusable(false);
                itemChoiseSelected.setText((CharSequence) info.get("item"));
                spinnerItem.setVisibility(View.GONE);

                EditText UnitViewChoise = findViewById(R.id.UnitViewChoise);
                UnitViewChoise.setVisibility(View.VISIBLE);
                UnitViewChoise.setFocusable(false);
                UnitViewChoise.setText((CharSequence) info.get("units"));
                spinnerUnits.setVisibility(View.GONE);

                itemName.setEnabled(false);
                quantity.setEnabled(false);
                total.setEnabled(false);
                itemName.setFocusable(false);
                itemName.setClickable(false);
                quantity.setFocusable(false);
                quantity.setClickable(false);
                total.setFocusable(false);
                total.setClickable(false);
                addFormButtom.setVisibility(View.GONE);
                break;
            case "edit":
                addFormButtom.setText("Aceptar cambios");
                break;

        }
    }

    private void updateForm(Map<String, Object> oldInfo) throws JSONException, IOException {
        Map<String, Object> newInfo = new HashMap<>();
        newInfo.put("CreatedBy",oldInfo.get("CreatedBy"));
        newInfo.put("Date",oldInfo.get("Date"));
        newInfo.put("idForm",oldInfo.get("idForm"));
        newInfo.put("nameForm",oldInfo.get("nameForm"));

        String itemR, quantityR, totalR,idGardenFb;

        idGardenFb = getIntent().getStringExtra("idGardenFirebase");

        itemR = itemName.getText().toString();
        quantityR = quantity.getText().toString();
        totalR = total.getText().toString();

        String defaultSelected = "Seleccione un elemento";
        if (itemSelectedItem.equals(defaultSelected)){
            newInfo.put("item",oldInfo.get("item"));
        }else{
            newInfo.put("item",itemSelectedItem);
        }

        if(unitSelectedItem.equals(defaultSelected)){
            newInfo.put("units",oldInfo.get("units"));
        }else{
            newInfo.put("units",unitSelectedItem);
        }
        newInfo.put("itemName",itemR);
        newInfo.put("quantity",quantityR);
        newInfo.put("total",totalR);

        Forms updateInfo = new Forms(this);
        updateInfo.updateInfoForm(oldInfo,newInfo,idGardenFb);

        Notifications notifications = new Notifications();
        notifications.notification("Formulario Editado", "Felicidades! Actualizaste la Información de tu Formulario", Form_SCMPH.this);

        onBackPressed();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}