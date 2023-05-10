package com.example.opcv.view.forms;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//Formulario Registro de eventos
public class Form_RE extends AppCompatActivity {

    private EditText date, eventName, totalPerson, femaleNumber, maleNumber, noSpcNumber, infantNumber, chilhoodNumber, teenNumber, youthNumber, adultNumber, elderlyNumber, afroNumber,nativeNumber, lgtbiNumber, romNumber, victimNumber, disabilityNumber, desmobilizedNumber, mongrelNumber, foreignNumber, peasantNumber, otherNumber;
    private FirebaseFirestore database;
    private Button rewards, myGardens, profile, addFormButtom, ludification;
    private String watch, idGarden, idCollection;
    private FormsCommunication formsUtilities;
    private TextView formName;
    private FloatingActionButton backButtom;
    private static final int REQUEST_STORAGE_PERMISSION = 1;


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
        rewards = (Button) findViewById(R.id.rewards);
        addFormButtom = findViewById(R.id.create_forms1_buttom);
        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        ludification = (Button) findViewById(R.id.ludification);

        // Nombre del formulario
        formName = (TextView) findViewById(R.id.form_RE_name);

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RE.this, RewardHomeActivity.class));
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

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(Form_RE.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(Form_RE.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
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

        if(watch.equals("true")){
            addFormButtom.setVisibility(View.GONE);
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            Map<String, Object> infoForm = (Map<String, Object>) getIntent().getSerializableExtra("idCollecion");
            showMapInfo(infoForm,idGarden);
        } else if (watch.equals("edit")) {
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            Map<String, Object> infoForm = (Map<String, Object>) getIntent().getSerializableExtra("idCollecion");
            showMapInfo(infoForm,idGarden);
            addFormButtom.setText("Aceptar cambios");
        }
        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watch = getIntent().getStringExtra("watch");
                    if (femaleNumber.getText().toString().equals("")) {
                        femaleNumber.setText("0");
                    }
                    if (maleNumber.getText().toString().equals("")) {
                        maleNumber.setText("0");
                    }
                    if (noSpcNumber.getText().toString().equals("")) {
                        noSpcNumber.setText("0");
                    }
                    if (infantNumber.getText().toString().equals("")) {
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

                    if(watch.equals("create")){
                        if (ContextCompat.checkSelfPermission(Form_RE.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(Form_RE.this,
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


        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void createNewForm(){
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

        Forms newForm = new com.example.opcv.business.forms.Forms(Form_RE.this);
        newForm.createFormInfo(infoForm, idGardenFb);

        Notifications notifications = new Notifications();
        notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_RE.this);

        Toast.makeText(Form_RE.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Form_RE.this, HomeActivity.class));
        finish();
    }

    private void updateForm(Map<String, Object> oldInfo) throws JSONException, IOException{
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

        String idGardenFb;
        idGardenFb = getIntent().getStringExtra("idGardenFirebase");
        Forms updateInfo = new Forms(this);
        updateInfo.updateInfoForm(oldInfo,infoForm,idGardenFb);
        Notifications notifications = new Notifications();
        notifications.notification("Formulario Editado", "Felicidades! Actualizaste la Información de tu Formulario", Form_RE.this);
        onBackPressed();
    }

    private void showMapInfo(Map<String, Object> info,String status){
        if(status.equals("true")){
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
        }

        date.setText((CharSequence) info.get("date"));
        eventName.setText((CharSequence) info.get("eventName"));
        totalPerson.setText((CharSequence) info.get("totalPerson"));
        femaleNumber.setText((CharSequence) info.get("womenNumber"));
        maleNumber.setText((CharSequence) info.get("menNumber"));
        noSpcNumber.setText((CharSequence) info.get("noSpcNumber"));
        infantNumber.setText((CharSequence) info.get("infantNumber"));
        chilhoodNumber.setText((CharSequence) info.get("childhoodNumber"));
        teenNumber.setText((CharSequence) info.get("teenNumber"));
        youthNumber.setText((CharSequence) info.get("youthNumber"));
        adultNumber.setText((CharSequence) info.get("adultNumber"));
        elderlyNumber.setText((CharSequence) info.get("elderlyNumber"));
        afroNumber.setText((CharSequence) info.get("afroNumber"));
        nativeNumber.setText((CharSequence) info.get("nativeNumber"));
        lgtbiNumber.setText((CharSequence) info.get("lgtbiNumber"));
        romNumber.setText((CharSequence) info.get("romNumber"));
        victimNumber.setText((CharSequence) info.get("victimNumber"));
        disabilityNumber.setText((CharSequence) info.get("disabilityNumber"));
        desmobilizedNumber.setText((CharSequence) info.get("demobilizedNumber"));
        mongrelNumber.setText((CharSequence) info.get("mongrelNumber"));
        foreignNumber.setText((CharSequence) info.get("foreignNumber"));
        peasantNumber.setText((CharSequence) info.get("peasantNumber"));
        otherNumber.setText((CharSequence) info.get("otherNumber"));
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

}