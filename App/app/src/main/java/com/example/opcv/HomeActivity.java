package com.example.opcv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.info.GardenListAdapter;
import com.example.opcv.info.ItemGardenHomeList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Button otherGardensButton, profile;
    private ListView listAviableGardensInfo;
    private FloatingActionButton nextArrow, addButton;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;

    @Override
    protected void onStart() {
        super.onStart();
        fillGardenUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        //Declaracion metodos de navegacion
        listAviableGardensInfo = findViewById(R.id.listAviableGardens);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        fillGardenUser();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CreateGardenActivity.class));
            }
        });

        otherGardensButton = (Button) findViewById(R.id.otherGardensButton);
        otherGardensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, VegetablePatchAvailableActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, EditUserActivity.class));
            }
        });

    }

    private void fillGardenUser(){
        CollectionReference Ref = database.collection("Gardens");
        String userID = autentication.getCurrentUser().getUid();
        Query query = Ref.whereEqualTo("ID_Owner", userID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ItemGardenHomeList> gardenNames = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getString("GardenName");
                        ItemGardenHomeList newItem = new ItemGardenHomeList(name);
                        gardenNames.add(newItem);
                    }
                    fillListGardens(gardenNames);
                } else {
                    Toast.makeText(HomeActivity.this, "Error al obtener los documentos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fillListGardens( List<ItemGardenHomeList> gardenInfoDocument){
        GardenListAdapter adapter = new GardenListAdapter(this, gardenInfoDocument);
        listAviableGardensInfo.setAdapter(adapter);
        listAviableGardensInfo.setDividerHeight(5);
    }
}