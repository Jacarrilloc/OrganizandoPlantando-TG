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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//Formulario Registro Historico Caja
public class Form_RHC extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsCommunication formsUtilities;
    private Button addFormButtom, rewards, myGardens, profile, ludification;
    private EditText responsable, code, units, measurement, totalCost, comments, itemName;
    private TextView formName;
    private Spinner spinnerConcept, spinnerType;
    private String conceptSelectedItem=null, selectedType, watch, idGarden, idCollection;
    private FirebaseFirestore database;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rhc);

        database = FirebaseFirestore.getInstance();
        rewards = (Button) findViewById(R.id.rewards);
        myGardens = (Button) findViewById(R.id.myGardens);
        profile = (Button) findViewById(R.id.profile);
        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);
        formName = (TextView) findViewById(R.id.form_RHC_name);
        responsable = (EditText) findViewById(R.id.responsable);
        code = (EditText) findViewById(R.id.code);
        units = (EditText) findViewById(R.id.units);
        itemName=(EditText) findViewById(R.id.itemName);
        measurement = (EditText) findViewById(R.id.measurement);
        totalCost = (EditText) findViewById(R.id.cost);
        comments = (EditText) findViewById(R.id.comments);
        spinnerConcept = (Spinner) findViewById(R.id.spinner1);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        addFormButtom = findViewById(R.id.create_forms1_buttom);
        ludification = (Button) findViewById(R.id.ludification);

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RHC.this, RewardHomeActivity.class));
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RHC.this, HomeActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RHC.this, EditUserActivity.class));
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(Form_RHC.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(Form_RHC.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        watch = getIntent().getStringExtra("watch");

        if(watch.equals("true")){
            idGarden = getIntent().getStringExtra("idGardenFirebase");
            idCollection = getIntent().getStringExtra("idCollecion");
            addFormButtom.setVisibility(View.INVISIBLE);
            addFormButtom.setClickable(false);
            spinnerType.setEnabled(false);
            spinnerConcept.setEnabled(false);
            responsable.setEnabled(false);
            code.setEnabled(false);
            itemName.setEnabled(false);
            units.setEnabled(false);
            measurement.setEnabled(false);
            totalCost.setEnabled(false);
            comments.setEnabled(false);
            responsable.setFocusable(false);
            responsable.setClickable(false);
            code.setFocusable(false);
            code.setClickable(false);
            units.setFocusable(false);
            units.setClickable(false);
            itemName.setFocusable(false);
            itemName.setClickable(false);
            measurement.setFocusable(false);
            measurement.setClickable(false);
            totalCost.setFocusable(false);
            totalCost.setClickable(false);
            comments.setFocusable(false);
            comments.setClickable(false);
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

                    if (ContextCompat.checkSelfPermission(Form_RHC.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Form_RHC.this,
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
        phaseElements.add("Ingreso");
        phaseElements.add("Egreso");
        ArrayAdapter adap = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, phaseElements);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConcept.setAdapter(adap);
        spinnerConcept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                conceptSelectedItem = spinnerConcept.getAdapter().getItem(i).toString();
                TextView text = (TextView) view;
                text.setText(conceptSelectedItem);
                text.setTextColor(Color.BLACK); // set the text color
                if(conceptSelectedItem != null){
                    if(conceptSelectedItem.equals("Ingreso")){
                        ArrayList<String> choiceElementsIncome = new ArrayList<>();
                        choiceElementsIncome.add("Seleccione un elemento");
                        choiceElementsIncome.add("Venta");
                        choiceElementsIncome.add("Donativo");
                        choiceElementsIncome.add("Mano de obra");
                        choiceElementsIncome.add("Aporte comunitario");
                        choiceElementsIncome.add("Otro");
                        ArrayAdapter adap2 = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, choiceElementsIncome);
                        adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerType.setAdapter(adap2);
                    }
                    else if(conceptSelectedItem.equals("Egreso")){
                        ArrayList<String> choiceElementsExpenses = new ArrayList<>();
                        choiceElementsExpenses.add("Seleccione un elemento");
                        choiceElementsExpenses.add("Compra");
                        choiceElementsExpenses.add("Obligación fiscal");
                        choiceElementsExpenses.add("Cobro");
                        choiceElementsExpenses.add("Devolución");
                        choiceElementsExpenses.add("Otro");
                        ArrayAdapter adap2 = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, choiceElementsExpenses);
                        adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerType.setAdapter(adap2);
                    }
                }
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedType = spinnerType.getAdapter().getItem(i).toString();
                        TextView text = (TextView) view;

                        if(Objects.nonNull(text)){
                            text.setText(selectedType);
                            text.setTextColor(Color.BLACK); // set the text color
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(Form_RHC.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Form_RHC.this, "Debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
            }
        });

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
                responsable.setText(task.getResult().get("responsable").toString());
                code.setText(task.getResult().get("code").toString());
                itemName.setText(task.getResult().get("itemName").toString());
                units.setText(task.getResult().get("units").toString());
                measurement.setText(task.getResult().get("measurement").toString());
                totalCost.setText(task.getResult().get("totalCost").toString());
                comments.setText(task.getResult().get("comments").toString());
                if(task.isSuccessful()){
                    if(status.equals("edit")){
                        String choice1 = task.getResult().get("incomeExpense").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        if(choice1.equals("Ingreso")){
                            elementShow.add("Ingreso");
                            elementShow.add("Egreso");
                            ArrayAdapter adap = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, elementShow);
                            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerConcept.setAdapter(adap);

                            String choice2 = task.getResult().get("type").toString();
                            ArrayList<String> elementShow2 = new ArrayList<>();
                                if(choice2.equals("Venta")){
                                    elementShow2.add("Venta");
                                    elementShow2.add("Donativo");
                                    elementShow2.add("Mano de obra");
                                    elementShow2.add("Aporte comunitario");
                                    elementShow2.add("Otro");

                                }
                                else if(choice2.equals("Donativo")){
                                    elementShow2.add("Donativo");
                                    elementShow2.add("Venta");
                                    elementShow2.add("Mano de obra");
                                    elementShow2.add("Aporte comunitario");
                                    elementShow2.add("Otro");
                                }
                                else if(choice2.equals("Mano de obra")){
                                    elementShow2.add("Mano de obra");
                                    elementShow2.add("Venta");
                                    elementShow2.add("Donativo");
                                    elementShow2.add("Aporte comunitario");
                                    elementShow2.add("Otro");
                                }else if(choice2.equals("Aporte comunitario")){
                                    elementShow2.add("Aporte comunitario");
                                    elementShow2.add("Venta");
                                    elementShow2.add("Donativo");
                                    elementShow2.add("Mano de obra");
                                    elementShow2.add("Otro");
                                }
                                else if(choice2.equals("Otro")){
                                    elementShow2.add("Otro");
                                    elementShow2.add("Aporte comunitario");
                                    elementShow2.add("Venta");
                                    elementShow2.add("Donativo");
                                    elementShow2.add("Mano de obra");
                                }
                                ArrayAdapter adap2 = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, elementShow2);
                                adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerType.setAdapter(adap2);


                        } else if (choice1.equals("Egreso")) {
                            elementShow.add("Egreso");
                            elementShow.add("Ingreso");

                            ArrayAdapter adap = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, elementShow);
                            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerConcept.setAdapter(adap);

                            String choice3 = task.getResult().get("type").toString();
                            ArrayList<String> elementShow3 = new ArrayList<>();
                            if(choice3.equals("Compra")){
                                elementShow3.add("Compra");
                                elementShow3.add("Obligación fiscal");
                                elementShow3.add("Cobro");
                                elementShow3.add("Devolución");
                                elementShow3.add("Otro");
                            }
                            else if(choice3.equals("Obligación fiscal")){
                                elementShow3.add("Obligación fiscal");
                                elementShow3.add("Compra");
                                elementShow3.add("Cobro");
                                elementShow3.add("Devolución");
                                elementShow3.add("Otro");
                            }
                            else if(choice3.equals("Cobro")){
                                elementShow3.add("Cobro");
                                elementShow3.add("Compra");
                                elementShow3.add("Obligación fiscal");
                                elementShow3.add("Devolución");
                                elementShow3.add("Otro");
                            }
                            else if(choice3.equals("Devolución")){
                                elementShow3.add("Devolución");
                                elementShow3.add("Compra");
                                elementShow3.add("Obligación fiscal");
                                elementShow3.add("Cobro");
                                elementShow3.add("Otro");
                            }
                            else if(choice3.equals("Otro")){
                                elementShow3.add("Otro");
                                elementShow3.add("Compra");
                                elementShow3.add("Obligación fiscal");
                                elementShow3.add("Cobro");
                                elementShow3.add("Devolución");
                            }
                            ArrayAdapter adap3 = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, elementShow3);
                            adap3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerType.setAdapter(adap3);

                        }

                    }
                    else if(status.equals("true")){

                        String choice1 = task.getResult().get("incomeExpense").toString();
                        ArrayList<String> elementShow = new ArrayList<>();
                        elementShow.add(choice1);
                        ArrayAdapter adap2 = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, elementShow);
                        spinnerConcept.setAdapter(adap2);
                        String choice2 = task.getResult().get("type").toString();
                        ArrayList<String> elementShow2 = new ArrayList<>();
                        elementShow2.add(choice2);
                        ArrayAdapter adap = new ArrayAdapter(Form_RHC.this, android.R.layout.simple_spinner_item, elementShow2);
                        spinnerType.setAdapter(adap);
                    }
                }
            }
        });

        addFormButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personResponsable, codeC, itemNameC, unitsC, measurementC, totalCostC, commentsC, incomeExpense, type;
                personResponsable = responsable.getText().toString();
                codeC = code.getText().toString();
                itemNameC = itemName.getText().toString();
                unitsC = units.getText().toString();
                measurementC = measurement.getText().toString();
                totalCostC = totalCost.getText().toString();
                commentsC = comments.getText().toString();
                incomeExpense = conceptSelectedItem;
                type =selectedType;
                if(validateField(personResponsable, codeC, itemNameC, unitsC, measurementC, totalCostC, commentsC, incomeExpense, type)){
                    formsUtilities.editInfoRHC(Form_RHC.this, idGarden, idCollection, personResponsable, codeC, itemNameC, unitsC, measurementC, totalCostC, commentsC, incomeExpense, type);
                    Toast.makeText(Form_RHC.this, "Se actualizó correctamente el formulario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean validateField(String personResponsable,String codeC, String itemNameC, String unitsC, String measurementC, String totalCostC, String commentsC, String incomeExpense, String type){

        if(personResponsable.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar la persona responsable", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(incomeExpense.equals("Seleccione un elemento")){
            Toast.makeText(this, "Es necesario seleccionar si es ingreso o egreso", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(type.equals("Seleccione un elemento")){
            Toast.makeText(this, "Es necesario seleccionar un tipo", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(codeC.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el codigo", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(itemNameC.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el nombre del item", Toast.LENGTH_SHORT).show();
            return false;
        }else if(unitsC.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar las unidades", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(measurementC.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar las medidas del ingreso/egreso", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(totalCostC.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el costo total", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(commentsC.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar comentarios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewForm(){
        String personResponsable, codeC, itemNameC, unitsC, measurementC, totalCostC, commentsC, idGardenFb, nameForm;
        personResponsable = responsable.getText().toString();
        codeC = code.getText().toString();
        itemNameC = itemName.getText().toString();
        unitsC = units.getText().toString();
        measurementC = measurement.getText().toString();
        totalCostC = totalCost.getText().toString();
        commentsC = comments.getText().toString();
        nameForm = formName.getText().toString();

        idGardenFb = getIntent().getStringExtra("idGardenFirebase");

        Map<String,Object> infoForm = new HashMap<>();
        infoForm.put("idForm",11);
        infoForm.put("nameForm",nameForm);
        infoForm.put("responsable",personResponsable);
        infoForm.put("incomeExpense",conceptSelectedItem);
        infoForm.put("type",selectedType);
        infoForm.put("code",codeC);
        infoForm.put("itemName",itemNameC);
        infoForm.put("measurement",measurementC);
        infoForm.put("totalCost",totalCostC);
        infoForm.put("comments",commentsC);
        infoForm.put("units",unitsC);
        if(validateField(personResponsable, codeC, itemNameC, unitsC, measurementC, totalCostC, commentsC, conceptSelectedItem, selectedType)){

            Forms newForm = new com.example.opcv.business.forms.Forms(Form_RHC.this);
            newForm.createFormInfo(infoForm, idGardenFb);

            Notifications notifications = new Notifications();
            notifications.notification("Formulario creado", "Felicidades! El formulario fue registrada satisfactoriamente", Form_RHC.this);
            //newForm.insertInto_RHC(infoForm);
            Toast.makeText(Form_RHC.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Form_RHC.this, HomeActivity.class));
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