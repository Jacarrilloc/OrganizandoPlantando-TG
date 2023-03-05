package com.example.opcv.formsScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.EditUserActivity;
import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.VegetablePatchAvailableActivity;
import com.example.opcv.fbComunication.FormsUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

//FOrmulario Registro y control de Compostaje
public class Form_RCC extends AppCompatActivity {
    private FloatingActionButton backButtom;
    private FormsUtilities formsUtilities;
    private Button addFormButtom, gardens, myGardens, profile;
    private EditText recipientArea, description, residueQuant, fertilizer, leached;
    private TextView formName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rcc);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RCC.this, VegetablePatchAvailableActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RCC.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_RCC.this, EditUserActivity.class));
            }
        });

        backButtom = findViewById(R.id.returnArrowButtonFormOnetoFormListElement);

        formName = (TextView) findViewById(R.id.form_RCC_name);

        recipientArea = (EditText) findViewById(R.id.areaRecipient);
        description = (EditText) findViewById(R.id.areaDescription);
        residueQuant = (EditText) findViewById(R.id.residueQuantity);
        fertilizer = (EditText) findViewById(R.id.fertilizerQuantity);
        leached = (EditText) findViewById(R.id.amount_leached_info);

        addFormButtom = (Button) findViewById(R.id.addForm);
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
                infoForm.put("nameForm",nameForm);
                infoForm.put("areaRecipient",areaRecipient);
                infoForm.put("areaDescription",descriptionProc);
                infoForm.put("residueQuantity",quantityResidue);
                infoForm.put("fertilizerQuantity",fertilizerQuantity);
                infoForm.put("leachedQuantity",quantityLeached);
                formsUtilities.createForm(Form_RCC.this,infoForm,idGardenFb);
                Toast.makeText(Form_RCC.this, "Se ha creado el Formulario con Exito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Form_RCC.this, HomeActivity.class));
                finish();

            }
        });

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}