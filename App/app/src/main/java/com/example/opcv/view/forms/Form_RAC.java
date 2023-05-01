package com.example.opcv.view.forms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.business.forms.Forms;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.business.persistance.firebase.FormsCommunication;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.view.gardens.MapsActivity;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.business.notifications.Notifications;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Form_RAC extends AppCompatActivity {

    private FloatingActionButton backButtom;
    private FormsCommunication formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile, ludification;

    private TextView formsName;

    private EditText containerSize,worrmsWeightInfo,humidityInfo,amount_of_waste_info,collected_humus_info,amount_leached_info;
    private String name, watch, idGarden, idCollection;
    private FirebaseFirestore database;
    private static final int REQUEST_STORAGE_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_form);

        database = FirebaseFirestore.getInstance();
        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        formsName = findViewById(R.id.formsNameFist);
        name = getIntent().getStringExtra("Name");
        containerSize = findViewById(R.id.containerSizeInfo);
        worrmsWeightInfo = findViewById(R.id.worrmsWeightInfo);
        humidityInfo = findViewById(R.id.humidityInfo);
        amount_of_waste_info = findViewById(R.id.amount_of_waste_info);
        collected_humus_info = findViewById(R.id.collected_humus_info);
        amount_leached_info = findViewById(R.id.amount_leached_info);
        gardens = (Button) findViewById(R.id.gardens);
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        addFormButtom = findViewById(R.id.create_forms1_buttom);

        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RAC.this, MapsActivity.class));
            }
        });


        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RAC.this, HomeActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RAC.this, EditUserActivity.class));
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(Form_RAC.this, DictionaryHomeActivity.class);
                startActivity(edit);
            }
        });

        watch = getIntent().getStringExtra("watch");

        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
            containerSize.setEnabled(false);
            worrmsWeightInfo.setEnabled(false);
            humidityInfo.setEnabled(false);
            amount_of_waste_info.setEnabled(false);
            amount_leached_info.setEnabled(false);
            collected_humus_info.setEnabled(false);
            containerSize.setFocusable(false);
            containerSize.setClickable(false);
            worrmsWeightInfo.setFocusable(false);
            worrmsWeightInfo.setClickable(false);
            humidityInfo.setFocusable(false);
            humidityInfo.setClickable(false);
            amount_leached_info.setFocusable(false);
            amount_leached_info.setClickable(false);
            amount_of_waste_info.setFocusable(false);
            amount_of_waste_info.setClickable(false);
            collected_humus_info.setFocusable(false);
            collected_humus_info.setClickable(false);
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
                    if (ContextCompat.checkSelfPermission(Form_RAC.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Form_RAC.this,
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
                containerSize.setText(task.getResult().get("containerSize").toString());
                worrmsWeightInfo.setText(task.getResult().get("wormsWeight").toString());
                humidityInfo.setText(task.getResult().get("humidity").toString());
                amount_of_waste_info.setText(task.getResult().get("amount of waste").toString());
                collected_humus_info.setText(task.getResult().get("collected humus").toString());
                amount_leached_info.setText(task.getResult().get("amount leached").toString());

            }
        });

        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String container, worms, humidity, waste, humus, leached;
                container = containerSize.getText().toString();
                worms = worrmsWeightInfo.getText().toString();
                humidity = humidityInfo.getText().toString();
                waste = amount_of_waste_info.getText().toString();
                humus = collected_humus_info.getText().toString();
                leached = amount_leached_info.getText().toString();
                if(validateField(container, worms, humidity, waste, humus, leached)){
                    formsUtilities.editInfoRAC(Form_RAC.this, idGarden, idCollection, container, worms, humidity, waste, humus, leached);
                    Toast.makeText(Form_RAC.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateField(String container,String worms, String humidity, String waste, String humus, String leached){

        if(container.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el area del recipiente", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(worms.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad de lombrices", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(humidity.isEmpty()) {
            Toast.makeText(this, "Es necesario Ingresar la humedad", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(waste.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad de residuos", Toast.LENGTH_SHORT).show();
            return false;
        }else if(humus.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad de humus recogida", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(leached.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad lixiviada", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewForm(){
        String container, worms, humidity, waste, humus, nameForm, leached, idGardenFb;
        container = containerSize.getText().toString();
        worms = worrmsWeightInfo.getText().toString();
        humidity = humidityInfo.getText().toString();
        waste = amount_of_waste_info.getText().toString();
        humus = collected_humus_info.getText().toString();
        leached = amount_leached_info.getText().toString();
        nameForm = formsName.getText().toString();

        idGardenFb = getIntent().getStringExtra("idGardenFirebase");

        Map<String,Object> infoForm = new HashMap<>();
        infoForm.put("idForm",1);
        infoForm.put("nameForm",nameForm);
        infoForm.put("containerSize",container);
        infoForm.put("wormsWeight",worms);
        infoForm.put("humidity",humidity);
        infoForm.put("amount of waste",waste);
        infoForm.put("collected humus",humus);
        infoForm.put("amount leached",leached);
        if(validateField(container, worms, humidity, waste, humus, leached)){

            Forms newForm = new com.example.opcv.business.forms.Forms(Form_RAC.this);
            newForm.createFormInfo(infoForm, idGardenFb);

            Notifications notifications = new Notifications();
            notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_RAC.this);

            //newForm.insertInto_RAC(infoForm);
            Toast.makeText(Form_RAC.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Form_RAC.this, HomeActivity.class));
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