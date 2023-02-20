package com.example.opcv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.info.GardenInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class huertaActivity extends AppCompatActivity {

    private Button returnArrowButton;
    private TextView nameGarden,descriptionGarden;
    private FirebaseFirestore database;
    private CollectionReference gardensRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huerta);

        nameGarden = findViewById(R.id.gardenNameText);
        descriptionGarden = findViewById(R.id.descriptionGarden);

        database = FirebaseFirestore.getInstance();
        gardensRef = database.collection("Gardens");

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String id = extras.getString("ID");
            String garden = extras.getString("gardenName");
            SearchInfoGardenSreen(id,garden);
        }
    }

    private void SearchInfoGardenSreen(String idUser,String name){
        Toast.makeText(this, "Entró", Toast.LENGTH_SHORT).show();
        Query query = gardensRef.whereEqualTo("ID_Owner", idUser).whereEqualTo("GardenName", name);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String infoDoc = document.getString("InfoGarden");
                        String typeDoc = document.getString("GardenType");
                        GardenInfo gardenInfo = new GardenInfo(idUser,name,infoDoc,typeDoc);
                        Toast.makeText(huertaActivity.this, "Info:" + gardenInfo.getName() + " " + gardenInfo.getInfo(), Toast.LENGTH_SHORT).show();
                        fillSreen(gardenInfo);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void fillSreen(GardenInfo gardenInfo){
        nameGarden.setText(gardenInfo.getName());
        descriptionGarden.setText(gardenInfo.getInfo());
    }
}