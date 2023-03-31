package com.example.opcv.formsScreen;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.adapter.FormsRegistersAdapter;
import com.example.opcv.adapter.MyCollaborationsListAdapter;
import com.example.opcv.conectionInfo.NetworkMonitorService;
import com.example.opcv.gardens.GardenForms;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.example.opcv.item_list.ItemRegistersList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
    private Button gardens, myGardens, profile;
    private NetworkMonitorService monitorService = new NetworkMonitorService();

    @Override
    protected void onStart() {
        super.onStart();
        fillFormsRegisters();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_registers);

        registerNameText = (TextView) findViewById(R.id.registerName);
        ListViewRegisters = (ListView) findViewById(R.id.ListViewRegisters);
        backButtom = findViewById(R.id.returnArrowButtonFormsToGarden);

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

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
        System.out.println("id garden "+idGarden+" name "+register_name);

        CollectionReference Ref = database.collection("Gardens").document(idGarden).collection("Forms");
        Query query = Ref.whereEqualTo("nameForm", register_name);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Se genero error: ", e);
                    return;
                }
                List<ItemRegistersList> formsRequests = new ArrayList<>();
                for(DocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.exists()) {
                        //System.out.println("Se genera"+documentSnapshot.getId());
                        ItemRegistersList newItem = new ItemRegistersList(idGarden, register_name, documentSnapshot.getId(), documentSnapshot.get("Date").toString());//xd
                        formsRequests.add(newItem);
                    }
                }
                fillListGardens(formsRequests);
            }
        });
    }
    private void fillListGardens( List<ItemRegistersList> gardenInfoDocument){
        FormsRegistersAdapter adapter = new FormsRegistersAdapter(this, gardenInfoDocument);
        ListViewRegisters.setAdapter(adapter);
        ListViewRegisters.setDividerHeight(5);
    }
}