package com.example.opcv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ImageButton editGarden;

    private ImageView moreFormsButtom;
    private TextView nameGarden,descriptionGarden;
    private FirebaseFirestore database;
    private CollectionReference gardensRef;

    private String gardenID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huerta);

        nameGarden = findViewById(R.id.gardenNameText);
        descriptionGarden = findViewById(R.id.descriptionGarden);
        moreFormsButtom = findViewById(R.id.moreFormsButtom);

        database = FirebaseFirestore.getInstance();
        gardensRef = database.collection("Gardens");

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String id = extras.getString("ID");
            String garden = extras.getString("gardenName");
            gardenID = extras.getString("idGarden");
            SearchInfoGardenSreen(id,garden);
        }

        moreFormsButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(huertaActivity.this,GardenForms.class));
            }
        });

        editGarden = (ImageButton) findViewById(R.id.imageButton8);
        editGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(huertaActivity.this,GardenEditActivity.class);
                start.putExtra("idGarden", gardenID);
                startActivity(start);
            }
        });
    }

    private void SearchInfoGardenSreen(String idUser,String name){
        Query query = gardensRef.whereEqualTo("ID_Owner", idUser).whereEqualTo("GardenName", name);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String infoDoc = document.getString("InfoGarden");
                        String typeDoc = document.getString("GardenType");
                        GardenInfo gardenInfo = new GardenInfo(idUser,name,infoDoc,typeDoc);
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