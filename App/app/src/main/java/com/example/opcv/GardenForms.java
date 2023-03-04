package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.adapter.FormsListAdapter;
import com.example.opcv.formsScreen.First_form;
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
                        Intent newForm = new Intent(GardenForms.this, First_form.class);
                        newForm.putExtra("Name",formsName);
                        newForm.putExtra("idGardenFirebase",idGardenFirebase);
                        startActivity(newForm);
                        finish();
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