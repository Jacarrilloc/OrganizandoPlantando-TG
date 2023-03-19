package com.example.opcv.formsScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.opcv.R;
import com.example.opcv.adapter.FormsRegistersAdapter;
import com.example.opcv.adapter.MyCollaborationsListAdapter;
import com.example.opcv.conectionInfo.NetworkMonitorService;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.example.opcv.item_list.ItemRegistersList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FormsRegistersActivity extends AppCompatActivity {
    private TextView registerNameText;
    private ListView ListViewRegisters;
    private FloatingActionButton backButtom;
    private String register_name, idGarden;
    private FirebaseFirestore database;
    private FirebaseAuth autentication;
    private NetworkMonitorService monitorService = new NetworkMonitorService();

    @Override
    protected void onStart() {
        super.onStart();
        fillFormsRegisters();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(monitorService, filter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_registers);

        registerNameText = (TextView) findViewById(R.id.registerName);
        ListViewRegisters = (ListView) findViewById(R.id.ListViewRegisters);
        backButtom = findViewById(R.id.returnArrowButtonFormsToGarden);

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        register_name = getIntent().getStringExtra("Name");
        idGarden = getIntent().getStringExtra("idGardenFirebase");

        registerNameText.setText(register_name);
    }
    private void fillFormsRegisters(){
        List<ItemRegistersList> formsRequests = new ArrayList<>();
        String fecha= "18/03/2023";
        String texto = "Fecha: "+fecha;
        ItemRegistersList newItem = new ItemRegistersList(texto, register_name, null);//xd
        formsRequests.add(newItem);
        fillListGardens(formsRequests);
    }
    private void fillListGardens( List<ItemRegistersList> gardenInfoDocument){
        FormsRegistersAdapter adapter = new FormsRegistersAdapter(this, gardenInfoDocument);
        ListViewRegisters.setAdapter(adapter);
        ListViewRegisters.setDividerHeight(5);
    }
    private void searchForms(String idGarden, String formName){
        int form;
        database = FirebaseFirestore.getInstance();
        autentication = FirebaseAuth.getInstance();
        if(Objects.equals(formName, "Registro y Actualización de Compostaje")){
            form = 0;
        }
        else if(Objects.equals(formName, "Solicitud de visita a la huerta")){
            form = 1;
        }
        else if(Objects.equals(formName, "Control de Inventario de Materia Prima")){
            form = 2;
        }
        else if(Objects.equals(formName, "Solicitud de compra de materia prima y herramientas")){
            form = 3;
        }
        else if(Objects.equals(formName, "Recepción y Requisición de Materia Prima")){
            form = 4;
        }
        else if(Objects.equals(formName, "SRevisión de Asistencia a la Huerta")){
            form = 5;
        }
        else if(Objects.equals(formName, "Control de Procesos de Siembra")){
            form = 6;
        }
        else if(Objects.equals(formName, "Registro y Control de Compostaje")){
            form = 7;
        }
        else if(Objects.equals(formName, "Recepción y Requisición de Herramientas")){
            form = 8;
        }
        else if(Objects.equals(formName, "Control de Inventarios de Herramientas")){
            form = 9;
        }
        else if(Objects.equals(formName, "Registro Histórico Caja")){
            form = 10;
        }

    }
}