package com.example.opcv.formsScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.MapsActivity;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.conectionInfo.NetworkMonitorService;
import com.example.opcv.fbComunication.FormsUtilities;
import com.example.opcv.localDatabase.DB_InsertForms;
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

//FOrmulario Registro y control de Compostaje
public class Form_RCC extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile, ludification;
    private EditText recipientArea, description, residueQuant, fertilizer, leached;
    private TextView formName;
    private String watch, idGarden, idCollection;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rcc);

        database = FirebaseFirestore.getInstance();
        gardens = (Button) findViewById(R.id.gardens);
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

        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RCC.this, MapsActivity.class));
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

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(Form_RCC.this, DictionaryHome.class);
                startActivity(edit);
            }
        });

        watch = getIntent().getStringExtra("watch");

        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
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
                        NetworkMonitorService connection = new NetworkMonitorService(Form_RCC.this);

                        if(connection.isOnline(Form_RCC.this)){
                            formsUtilities.createForm(Form_RCC.this,infoForm,idGardenFb);
                        }

                        DB_InsertForms newForm = new DB_InsertForms(Form_RCC.this);
                        //newForm.insertInto_RCC(infoForm);
                        Toast.makeText(Form_RCC.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Form_RCC.this, HomeActivity.class));
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