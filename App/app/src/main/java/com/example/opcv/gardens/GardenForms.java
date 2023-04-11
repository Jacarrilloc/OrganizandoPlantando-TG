package com.example.opcv.gardens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.opcv.R;
import com.example.opcv.adapter.FormsListAdapter;
import com.example.opcv.formsScreen.Form_IMP;
import com.example.opcv.formsScreen.Form_RAC;
import com.example.opcv.formsScreen.Form_CIH;
import com.example.opcv.formsScreen.Form_CPS;
import com.example.opcv.formsScreen.Form_RCC;
import com.example.opcv.formsScreen.Form_RHC;
import com.example.opcv.formsScreen.Form_RRH;
import com.example.opcv.formsScreen.Form_RSMP;
import com.example.opcv.formsScreen.Form_SCMPH;
import com.example.opcv.formsScreen.Form_RE;
import com.example.opcv.formsScreen.FormsRegistersActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Objects;


public class GardenForms extends AppCompatActivity {

    private ListView gardenFormsList;
    private TextView text;
    private String[] itemsList;

    private FloatingActionButton backButtom;
    private Animation animSlideUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_adapter);

        backButtom = findViewById(R.id.returnArrowButtonFormsToGarden);
        text = (TextView) findViewById(R.id.formsOrRegister);

        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        gardenFormsList = findViewById(R.id.gardenFormsList);
        itemsList = getResources().getStringArray(R.array.list_forms);
        FormsListAdapter adapter = new FormsListAdapter(this,R.layout.item_list_forms_adapter, Arrays.asList(itemsList));
        gardenFormsList.setDividerHeight(10);
        gardenFormsList.setAdapter(adapter);

        String register_forms = getIntent().getStringExtra("Register/Forms");
        String idGardenFirebase = getIntent().getStringExtra("idGardenFirebaseDoc");

        if(Objects.equals(register_forms, "Register")){
            text.setText("Registros de formularios");
        }

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        gardenFormsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Objects.equals(register_forms, "Forms")){
                    switch (i){
                        case 0:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_RAC.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                        case 1:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_RE.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                        case 2:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_IMP.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                        case 3:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_SCMPH.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                        case 4:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_RSMP.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                        case 5:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_CPS.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                        case 6:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_RCC.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                        case 7:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_RRH.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                        case 8:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_CIH.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                        case 9:{
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, Form_RHC.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            newForm.putExtra("watch","create");
                            startActivity(newForm);
                            break;
                        }
                    }
                }
                else if(Objects.equals(register_forms, "Register")){
                    switch (i){
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10: {
                            Object selectedItem = adapterView.getItemAtPosition(i);
                            String formsName = selectedItem.toString();
                            Intent newForm = new Intent(GardenForms.this, FormsRegistersActivity.class);
                            newForm.putExtra("Name",formsName);
                            newForm.putExtra("idGardenFirebase",idGardenFirebase);
                            //newForm.putExtra("watch","true");
                            startActivity(newForm);
                            break;
                        }
                    }
                }
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