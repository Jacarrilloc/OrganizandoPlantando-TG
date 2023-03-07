package com.example.opcv.gardens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.adapter.FormsListAdapter;
import com.example.opcv.formsScreen.Form_CIH;
import com.example.opcv.formsScreen.Form_CPS;
import com.example.opcv.formsScreen.Form_RAC;
import com.example.opcv.formsScreen.Form_RCC;
import com.example.opcv.formsScreen.Form_RHC;
import com.example.opcv.formsScreen.Form_RRH;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;


public class GardenForms extends AppCompatActivity {

    private ListView gardenFormsList;
    private String[] itemsList;

    private FloatingActionButton backButtom;
    private Animation animSlideUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_adapter);

        backButtom = findViewById(R.id.returnArrowButtonFormsToGarden);

        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        gardenFormsList = findViewById(R.id.gardenFormsList);
        itemsList = getResources().getStringArray(R.array.list_forms);
        FormsListAdapter adapter = new FormsListAdapter(this,R.layout.item_list_forms_adapter, Arrays.asList(itemsList));
        gardenFormsList.setDividerHeight(5);
        gardenFormsList.setAdapter(adapter);

        String idGardenFirebase = getIntent().getStringExtra("idGardenFirebaseDoc");

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        gardenFormsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:{
                        Object selectedItem = adapterView.getItemAtPosition(i);
                        String formsName = selectedItem.toString();
                        Intent newForm = new Intent(GardenForms.this, Form_RAC.class);
                        newForm.putExtra("Name",formsName);
                        newForm.putExtra("idGardenFirebase",idGardenFirebase);
                        startActivity(newForm);
                        break;
                    }
                    case 6:{
                        Object selectedItem = adapterView.getItemAtPosition(i);
                        String formsName = selectedItem.toString();
                        Intent newForm = new Intent(GardenForms.this, Form_CPS.class);
                        newForm.putExtra("Name",formsName);
                        newForm.putExtra("idGardenFirebase",idGardenFirebase);
                        startActivity(newForm);
                        break;
                    }
                    case 7:{
                        Object selectedItem = adapterView.getItemAtPosition(i);
                        String formsName = selectedItem.toString();
                        Intent newForm = new Intent(GardenForms.this, Form_RCC.class);
                        newForm.putExtra("Name",formsName);
                        newForm.putExtra("idGardenFirebase",idGardenFirebase);
                        startActivity(newForm);
                        break;
                    }
                    case 8:{
                        Object selectedItem = adapterView.getItemAtPosition(i);
                        String formsName = selectedItem.toString();
                        Intent newForm = new Intent(GardenForms.this, Form_RRH.class);
                        newForm.putExtra("Name",formsName);
                        newForm.putExtra("idGardenFirebase",idGardenFirebase);
                        startActivity(newForm);
                        break;
                    }
                    case 9:{
                        Object selectedItem = adapterView.getItemAtPosition(i);
                        String formsName = selectedItem.toString();
                        Intent newForm = new Intent(GardenForms.this, Form_CIH.class);
                        newForm.putExtra("Name",formsName);
                        newForm.putExtra("idGardenFirebase",idGardenFirebase);
                        startActivity(newForm);
                        break;
                    }
                    case 10:{
                        Object selectedItem = adapterView.getItemAtPosition(i);
                        String formsName = selectedItem.toString();
                        Intent newForm = new Intent(GardenForms.this, Form_RHC.class);
                        newForm.putExtra("Name",formsName);
                        newForm.putExtra("idGardenFirebase",idGardenFirebase);
                        startActivity(newForm);
                        break;
                    }
                }
                /*Object selectedItem = adapterView.getItemAtPosition(i);
                String formsName = selectedItem.toString();
                Intent newForm = new Intent(GardenForms.this,FormsActivity.class);
                newForm.putExtra("Name",formsName);
                startActivity(newForm);*/
            }
        });
    }
}