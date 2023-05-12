package com.example.opcv.view.forms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//FOrmulario Registro y control de Compostaje
public class Form_RCC extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsCommunication formsUtilities;
    private Button addFormButtom, rewards, myGardens, profile, ludification;
    private EditText recipientArea, description, residueQuant, fertilizer, leached;
    private TextView formName;
    private String watch, idGarden, idCollection;
    private FirebaseFirestore database;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rcc);

        database = FirebaseFirestore.getInstance();
        rewards = (Button) findViewById(R.id.rewards);
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        formName = (TextView) findViewById(R.id.form_RCC_name);
        recipientArea = (EditText) findViewById(R.id.areaRecipient);
        description = (EditText) findViewById(R.id.areaDescription);
        residueQuant = (EditText) findViewById(R.id.residueQuantity);
        fertilizer = (EditText) findViewById(R.id.fertilizerQuantity);
        leached = (EditText) findViewById(R.id.amount_leached_info);
        addFormButtom = (Button) findViewById(R.id.addForm);
        ludification = (Button) findViewById(R.id.ludification);

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RCC.this, RewardHomeActivity.class));
            }
        });
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RCC.this, HomeActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RCC.this, EditUserActivity.class));
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(Form_RCC.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(Form_RCC.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        watch = getIntent().getStringExtra("watch");
        watch = getIntent().getStringExtra("watch");
        Map<String, Object> infoForm = (Map<String, Object>) getIntent().getSerializableExtra("idCollecion");
        showMapInfo(infoForm,watch);

        /*
        } else if (watch.equals("edit")) {
            formsUtilities = new FormsCommunication();

            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            showInfo(idGarden, idCollection, "edit");
            addFormButtom.setText("Aceptar cambios");

        }

        else if (watch.equals("create")){ */

        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watch = getIntent().getStringExtra("watch");
                if(watch.equals("create")) {
                    if (ContextCompat.checkSelfPermission(Form_RCC.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Form_RCC.this,
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
    }

    private void showMapInfo(Map<String, Object> info,String status){
        if(info != null) {
            recipientArea.setText((CharSequence) info.get("areaRecipient"));
            description.setText((CharSequence) info.get("areaDescription"));
            residueQuant.setText((CharSequence) info.get("residueQuantity"));
            fertilizer.setText((CharSequence) info.get("fertilizerQuantity"));
            leached.setText((CharSequence) info.get("leachedQuantity"));
            switch (status) {
                case "true":
                    recipientArea.setEnabled(false);
                    description.setEnabled(false);
                    residueQuant.setEnabled(false);
                    fertilizer.setEnabled(false);
                    leached.setEnabled(false);
                    recipientArea.setFocusable(false);
                    recipientArea.setClickable(false);
                    description.setFocusable(false);
                    description.setClickable(false);
                    residueQuant.setFocusable(false);
                    residueQuant.setClickable(false);
                    fertilizer.setFocusable(false);
                    fertilizer.setClickable(false);
                    leached.setFocusable(false);
                    leached.setClickable(false);
                    addFormButtom.setVisibility(View.GONE);
                    break;
                case "edit":
                    addFormButtom.setText("Aceptar cambios");
                    break;
            }
        }
    }

    private void showInfo(String idGarden, String idCollection, String status){
        CollectionReference ref = database.collection("Gardens").document(idGarden).collection("Forms");
        ref.document(idCollection).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                recipientArea.setText(task.getResult().get("areaRecipient").toString());
                description.setText(task.getResult().get("areaDescription").toString());
                residueQuant.setText(task.getResult().get("residueQuantity").toString());
                fertilizer.setText(task.getResult().get("fertilizerQuantity").toString());
                leached.setText(task.getResult().get("leachedQuantity").toString());
            }
        });
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String areaRecipient, descriptionProc, quantityResidue, fertilizerQuantity, quantityLeached;
                areaRecipient = recipientArea.getText().toString();
                descriptionProc = description.getText().toString();
                quantityResidue = residueQuant.getText().toString();
                fertilizerQuantity = fertilizer.getText().toString();
                quantityLeached = leached.getText().toString();
                if(validateField(areaRecipient, descriptionProc, quantityResidue, fertilizerQuantity, quantityLeached)){
                    formsUtilities.editInfoRCC(Form_RCC.this, idGarden, idCollection, areaRecipient, descriptionProc, quantityResidue, fertilizerQuantity, quantityLeached);
                    Toast.makeText(Form_RCC.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateField(String areaRecipient,String descriptionProc, String quantityResidue, String fertilizerQuantity, String quantityLeached){

        if(areaRecipient.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el area del recipiente", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(descriptionProc.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la descripción", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(quantityResidue.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad de residuos", Toast.LENGTH_SHORT).show();
            return false;
        }else if(fertilizerQuantity.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad de abono recogido", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(quantityLeached.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la cantidad lixiviada", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewForm(){
        String areaRecipient, descriptionProc, quantityResidue, fertilizerQuantity, quantityLeached, nameForm, idGardenFb;
        areaRecipient = recipientArea.getText().toString();
        descriptionProc = description.getText().toString();
        quantityResidue = residueQuant.getText().toString();
        fertilizerQuantity = fertilizer.getText().toString();
        quantityLeached = leached.getText().toString();
        nameForm = formName.getText().toString();
        idGardenFb = getIntent().getStringExtra("idGardenFirebase");

        Map<String,Object> infoForm = new HashMap<>();
        infoForm.put("idForm", 8);
        infoForm.put("nameForm",nameForm);
        infoForm.put("areaRecipient",areaRecipient);
        infoForm.put("areaDescription",descriptionProc);
        infoForm.put("residueQuantity",quantityResidue);
        infoForm.put("fertilizerQuantity",fertilizerQuantity);
        infoForm.put("leachedQuantity",quantityLeached);
        if(validateField(areaRecipient, descriptionProc, quantityResidue, fertilizerQuantity, quantityLeached)){

            Forms newForm = new com.example.opcv.business.forms.Forms(Form_RCC.this);
            newForm.createFormInfo(infoForm, idGardenFb);

            Notifications notifications = new Notifications();
            notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_RCC.this);

            //newForm.insertInto_RCC(infoForm);
            Toast.makeText(Form_RCC.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Form_RCC.this, HomeActivity.class));
            finish();
        }
    }

    private void updateForm(Map<String, Object> oldInfo) throws JSONException, IOException{
        Map<String, Object> newInfo = new HashMap<>();
        newInfo.put("CreatedBy",oldInfo.get("CreatedBy"));
        newInfo.put("Date",oldInfo.get("Date"));
        newInfo.put("idForm",oldInfo.get("idForm"));
        newInfo.put("nameForm",oldInfo.get("nameForm"));

        newInfo.put("areaRecipient",recipientArea.getText().toString());
        newInfo.put("areaDescription",description.getText().toString());
        newInfo.put("residueQuantity",residueQuant.getText().toString());
        newInfo.put("fertilizerQuantity",fertilizer.getText().toString());
        newInfo.put("leachedQuantity",leached.getText().toString());

        String idGardenFb = getIntent().getStringExtra("idGardenFirebase");

        Forms updateInfo = new Forms(this);
        updateInfo.updateInfoForm(oldInfo,newInfo,idGardenFb);

        Notifications notifications = new Notifications();
        notifications.notification("Formulario Editado", "Felicidades! Actualizaste la Información de tu Formulario", Form_RCC.this);

        onBackPressed();
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