package com.example.opcv.gardens;

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

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.fbComunication.CollaboratorUtilities;
import com.example.opcv.info.GardenInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OtherGardensActivity extends AppCompatActivity {
    private Button otherGardensButton, profile, myGardens, join, visit;
    private TextView nameGarden,descriptionGarden;
    private FirebaseFirestore database;
    private CollectionReference gardensRef;
    private String gardenID, garden, infoGarden, id;
    private FloatingActionButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_gardens);

        join= (Button) findViewById(R.id.requesteJoin);
        nameGarden = (TextView) findViewById(R.id.gardenNameText);
        descriptionGarden = (TextView) findViewById(R.id.descriptionGarden);
        returnButton = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        otherGardensButton = (Button) findViewById(R.id.gardens);
        otherGardensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OtherGardensActivity.this, MapsActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OtherGardensActivity.this, EditUserActivity.class));
            }
        });
        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OtherGardensActivity.this, HomeActivity.class));
            }
        });



        database = FirebaseFirestore.getInstance();
        gardensRef = database.collection("Gardens");

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("ID");
            garden = extras.getString("gardenName");
            gardenID = extras.getString("idGarden");
            SearchInfoGardenSreen(id,garden);
        }

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollaboratorUtilities cU = new CollaboratorUtilities();
                cU.addRequests(OtherGardensActivity.this, id, gardenID);
                Toast.makeText(OtherGardensActivity.this, "Se envio la solicitud al dueño de la huerta", Toast.LENGTH_SHORT).show();
                join.setVisibility(View.INVISIBLE);
                join.setClickable(false);
            }
        });
    }
    private void SearchInfoGardenSreen(String idUser,String name){
        DocumentReference ref = gardensRef.document(gardenID);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String infoDoc=null;
                if (documentSnapshot.exists()) {

                    String typeDoc = documentSnapshot.getString("GardenType");
                    infoDoc = documentSnapshot.getString("InfoGarden");
                    GardenInfo gardenInfo = new GardenInfo(idUser,name,infoDoc,typeDoc);

                    fillSreen(gardenInfo);
                }
                /*else {
                    System.out.println("Se genero error al mostrar la información");
                }*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Se genero error: ", e);
            }
        });

    }

    private void fillSreen(GardenInfo gardenInfo){
        nameGarden.setText(gardenInfo.getName());
        descriptionGarden.setText(gardenInfo.getInfo());
    }

}