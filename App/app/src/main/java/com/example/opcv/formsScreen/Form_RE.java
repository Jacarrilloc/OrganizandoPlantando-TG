package com.example.opcv.formsScreen;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.fbComunication.FormsUtilities;
import com.example.opcv.ludificationScreens.DictionaryHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//Formulario Registro de eventos
public class Form_RE extends AppCompatActivity {

    private EditText date, eventName, totalPerson, femaleNumber, maleNumber, noSpcNumber, infantNumber, chilhoodNumber, teenNumber, youthNumber, adultNumber, elderlyNumber, afroNumber,nativeNumber, lgtbiNumber, romNumber, victimNumber, disabilityNumber, desmobilizedNumber, mongrelNumber, foreignNumber, peasantNumber, otherNumber;
    private FirebaseFirestore database;
    private Button gardens, myGardens, profile, addFormButtom, ludification;
    private String watch, idGarden, idCollection;
    private FormsUtilities formsUtilities;
    private TextView formName;
    private FloatingActionButton backButtom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_re);

        database = FirebaseFirestore.getInstance();

        //Información del formulario
        date = (EditText) findViewById(R.id.eventDate);
        eventName = (EditText) findViewById(R.id.eventName);
        totalPerson = (EditText) findViewById(R.id.totalPerson);
        femaleNumber = (EditText) findViewById(R.id.female);
        maleNumber = (EditText) findViewById(R.id.male);
        noSpcNumber = (EditText) findViewById(R.id.noSpecificated);
        infantNumber = (EditText) findViewById(R.id.infant);
        chilhoodNumber = (EditText) findViewById(R.id.childhood);
        teenNumber = (EditText) findViewById(R.id.teen);
        youthNumber = (EditText) findViewById(R.id.youth);
        adultNumber = (EditText) findViewById(R.id.adult);
        elderlyNumber = (EditText) findViewById(R.id.elderly);
        afroNumber = (EditText) findViewById(R.id.afroPoblation);
        nativeNumber = (EditText) findViewById(R.id.nativePoblation);
        lgtbiNumber = (EditText) findViewById(R.id.LGBTIPoblation);
        romNumber = (EditText) findViewById(R.id.RomPoblation);
        victimNumber = (EditText) findViewById(R.id.victimPoblation);
        disabilityNumber = (EditText) findViewById(R.id.disabilityPoblation);
        desmobilizedNumber = (EditText) findViewById(R.id.demobilizedPoblation);
        mongrelNumber = (EditText) findViewById(R.id.mongrelPoblation);
        foreignNumber = (EditText) findViewById(R.id.foreignPoblation);
        peasantNumber = (EditText) findViewById(R.id.peasantPoblation);
        otherNumber = (EditText) findViewById(R.id.otherPoblation);

        // Botones
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        gardens = (Button) findViewById(R.id.gardens);
        addFormButtom = findViewById(R.id.create_forms1_buttom);
        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);

        // Nombre del formulario
        formName = (TextView) findViewById(R.id.form_RE_name);


        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RE.this, MapsActivity.class));
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RE.this, HomeActivity.class));
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RE.this, EditUserActivity.class));
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(Form_RE.this, DictionaryHome.class);
                startActivity(edit);
            }
        });

        //-------------- Verificar qué campos están llenos---------

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Form_RE.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Actualizar el valor del EditText con la fecha seleccionada
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });



        watch = getIntent().getStringExtra("watch");
        System.out.println("el watch: ");

        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
            date.setEnabled(false);
            eventName.setEnabled(false);
            totalPerson.setEnabled(false);
            femaleNumber.setEnabled(false);
            maleNumber.setEnabled(false);
            noSpcNumber.setEnabled(false);
            infantNumber.setEnabled(false);
            chilhoodNumber.setEnabled(false);
            teenNumber.setEnabled(false);
            youthNumber.setEnabled(false);
            adultNumber.setEnabled(false);
            elderlyNumber.setEnabled(false);
            afroNumber.setEnabled(false);
            nativeNumber.setEnabled(false);
            lgtbiNumber.setEnabled(false);
            romNumber.setEnabled(false);
            victimNumber.setEnabled(false);
            disabilityNumber.setEnabled(false);
            desmobilizedNumber.setEnabled(false);
            mongrelNumber.setEnabled(false);
            foreignNumber.setEnabled(false);
            peasantNumber.setEnabled(false);
            otherNumber.setEnabled(false);
            showInfo(idGarden, idCollection, "true");
        } else if (watch.equals("edit")) {
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            showInfo(idGarden, idCollection, "edit");
            addFormButtom.setText("Aceptar cambios");
        }
        else if (watch.equals("create")){
            addFormButtom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(femaleNumber.getText().toString().equals("")){
                        femaleNumber.setText("0");
                    }
                    if (maleNumber.getText().toString().equals("")) {
                        maleNumber.setText("0");
                    }
                    if (noSpcNumber.getText().toString().equals("")) {
                        noSpcNumber.setText("0");
                    }
                    if (infantNumber.getText().toString().equals("")){
                        infantNumber.setText("0");
                    }
                    if (chilhoodNumber.getText().toString().equals("")) {
                        chilhoodNumber.setText("0");
                    }
                    if (teenNumber.getText().toString().equals("")) {
                        teenNumber.setText("0");
                    }
                    if (youthNumber.getText().toString().equals("")) {
                        youthNumber.setText("0");
                    }
                    if (adultNumber.getText().toString().equals("")) {
                        adultNumber.setText("0");
                    }
                    if (elderlyNumber.getText().toString().equals("")) {
                        elderlyNumber.setText("0");
                    }
                    if (afroNumber.getText().toString().equals("")) {
                        afroNumber.setText("0");
                    }
                    if (nativeNumber.getText().toString().equals("")) {
                        nativeNumber.setText("0");
                    }
                    if (lgtbiNumber.getText().toString().equals("")) {
                        lgtbiNumber.setText("0");
                    }
                    if (romNumber.getText().toString().equals("")) {
                        romNumber.setText("0");
                    }
                    if (victimNumber.getText().toString().equals("")) {
                        victimNumber.setText("0");
                    }
                    if (disabilityNumber.getText().toString().equals("")) {
                        disabilityNumber.setText("0");
                    }
                    if (desmobilizedNumber.getText().toString().equals("")) {
                        desmobilizedNumber.setText("0");
                    }
                    if (mongrelNumber.getText().toString().equals("")) {
                        mongrelNumber.setText("0");
                    }
                    if (foreignNumber.getText().toString().equals("")) {
                        foreignNumber.setText("0");
                    }
                    if (peasantNumber.getText().toString().equals("")) {
                        peasantNumber.setText("0");
                    }
                    if (otherNumber.getText().toString().equals("")) {
                        otherNumber.setText("0");
                    }

                    // Crear el formulario
                    formsUtilities = new FormsUtilities();
                    String idGardenFb;
                    idGardenFb = getIntent().getStringExtra("idGardenFirebase");
                    Map<String,Object> infoForm = new HashMap<>();
                    infoForm.put("idForm",12);
                    infoForm.put("nameForm", formName.getText().toString());
                    infoForm.put("date",date.getText().toString());
                    infoForm.put("eventName", eventName.getText().toString());
                    infoForm.put("totalPerson", totalPerson.getText().toString());
                    infoForm.put("womenNumber", femaleNumber.getText().toString());
                    infoForm.put("menNumber", maleNumber.getText().toString());
                    infoForm.put("noSpcNumber", noSpcNumber.getText().toString());
                    infoForm.put("infantNumber", infantNumber.getText().toString());
                    infoForm.put("childhoodNumber",chilhoodNumber.getText().toString());
                    infoForm.put("teenNumber",teenNumber.getText().toString());
                    infoForm.put("youthNumber", youthNumber.getText().toString());
                    infoForm.put("adultNumber",adultNumber.getText().toString());
                    infoForm.put("elderlyNumber", elderlyNumber.getText().toString());
                    infoForm.put("afroNumber", afroNumber.getText().toString());
                    infoForm.put("nativeNumber", nativeNumber.getText().toString());
                    infoForm.put("lgtbiNumber", lgtbiNumber.getText().toString());
                    infoForm.put("romNumber",romNumber.getText().toString());
                    infoForm.put("victimNumber", victimNumber.getText().toString());
                    infoForm.put("disabilityNumber", disabilityNumber.getText().toString());
                    infoForm.put("demobilizedNumber", desmobilizedNumber.getText().toString());
                    infoForm.put("mongrelNumber",mongrelNumber.getText().toString());
                    infoForm.put("foreignNumber",foreignNumber.getText().toString());
                    infoForm.put("peasantNumber", peasantNumber.getText().toString());
                    infoForm.put("otherNumber", otherNumber.getText().toString());

                    //newForm.insertInto_RE(infoForm);
                    Toast.makeText(Form_RE.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Form_RE.this, HomeActivity.class));
                    finish();

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

    private void showInfo(String idGarden, String idCollection, String status) {

        CollectionReference ref = database.collection("Gardens").document(idGarden).collection("Forms");

        ref.document(idCollection).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    date.setText(Objects.requireNonNull(task.getResult().get("date")).toString());
                    eventName.setText(Objects.requireNonNull(task.getResult().get("eventName")).toString());
                    totalPerson.setText(Objects.requireNonNull(task.getResult().get("totalPerson")).toString());
                    femaleNumber.setText(Objects.requireNonNull(task.getResult().get("womenNumber")).toString());
                    maleNumber.setText(Objects.requireNonNull(task.getResult().get("menNumber")).toString());
                    noSpcNumber.setText(Objects.requireNonNull(task.getResult().get("noSpcNumber")).toString());
                    infantNumber.setText(Objects.requireNonNull(task.getResult().get("infantNumber")).toString());
                    chilhoodNumber.setText(Objects.requireNonNull(task.getResult().get("childhoodNumber")).toString());
                    teenNumber.setText(Objects.requireNonNull(task.getResult().get("teenNumber")).toString());
                    youthNumber.setText(Objects.requireNonNull(task.getResult().get("youthNumber")).toString());
                    adultNumber.setText(Objects.requireNonNull(task.getResult().get("adultNumber")).toString());
                    elderlyNumber.setText(Objects.requireNonNull(task.getResult().get("elderlyNumber")).toString());
                    afroNumber.setText(Objects.requireNonNull(task.getResult().get("afroNumber")).toString());
                    nativeNumber.setText(Objects.requireNonNull(task.getResult().get("nativeNumber")).toString());
                    lgtbiNumber.setText(Objects.requireNonNull(task.getResult().get("lgtbiNumber")).toString());
                    romNumber.setText(Objects.requireNonNull(task.getResult().get("romNumber")).toString());
                    victimNumber.setText(Objects.requireNonNull(task.getResult().get("victimNumber")).toString());
                    disabilityNumber.setText(Objects.requireNonNull(task.getResult().get("disabilityNumber")).toString());
                    desmobilizedNumber.setText(Objects.requireNonNull(task.getResult().get("demobilizedNumber")).toString());
                    mongrelNumber.setText(Objects.requireNonNull(task.getResult().get("mongrelNumber")).toString());
                    foreignNumber.setText(Objects.requireNonNull(task.getResult().get("foreignNumber")).toString());
                    peasantNumber.setText(Objects.requireNonNull(task.getResult().get("peasantNumber")).toString());
                    otherNumber.setText(Objects.requireNonNull(task.getResult().get("otherNumber")).toString());
                }catch (Exception e){
                    Toast.makeText(Form_RE.this, "Ocurrió un error al cargar los datos", Toast.LENGTH_LONG).show();
                }
            }
        });
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String datex, eventN, totalP, femaleN, maleN, noSpcN, infantN, childhoodN, teenN, youthN, adultN, elderlyN, afroN, nativeN, lgtbiN, romN, victimN, disabilityN, desmobilizedN, mongrelN, foreignN, peasantN, otherN;
                datex = date.getText().toString();
                eventN = eventName.getText().toString();
                totalP = totalPerson.getText().toString();
                femaleN = femaleNumber.getText().toString();
                maleN = maleNumber.getText().toString();
                noSpcN= noSpcNumber.getText().toString();
                infantN = infantNumber.getText().toString();
                childhoodN = chilhoodNumber.getText().toString();
                teenN = teenNumber.getText().toString();
                youthN = youthNumber.getText().toString();
                adultN = adultNumber.getText().toString();
                elderlyN = elderlyNumber.getText().toString();
                afroN = afroNumber.getText().toString();
                nativeN = nativeNumber.getText().toString();
                lgtbiN = lgtbiNumber.getText().toString();
                romN = afroNumber.getText().toString();
                victimN = victimNumber.getText().toString();
                disabilityN = disabilityNumber.getText().toString();
                desmobilizedN = desmobilizedNumber.getText().toString();
                mongrelN = mongrelNumber.getText().toString();
                foreignN = foreignNumber.getText().toString();
                peasantN = peasantNumber.getText().toString();
                otherN = otherNumber.getText().toString();
                formsUtilities.editInfoRE(Form_RE.this, idGarden, idCollection, datex, eventN, totalP, femaleN, maleN, noSpcN, infantN, childhoodN, teenN, youthN, adultN, elderlyN, afroN, nativeN, lgtbiN, romN, victimN, disabilityN, desmobilizedN, mongrelN, foreignN, peasantN, otherN);
                Toast.makeText(Form_RE.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
            }
        });
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