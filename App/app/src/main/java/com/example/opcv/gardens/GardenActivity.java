package com.example.opcv.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.fbComunication.CollaboratorUtilities;
import com.example.opcv.formsScreen.Form_CIH;
import com.example.opcv.formsScreen.Form_CPS;
import com.example.opcv.formsScreen.Form_RAC;
import com.example.opcv.info.GardenInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GardenActivity extends AppCompatActivity {

    private Button formsRegister, gardens, myGardens, profile;
    private ImageButton editGarden, seedTime, toolsButton, worm, collaboratorGardens, messages;

    private ImageView moreFormsButtom;
    private TextView nameGarden,descriptionGarden, gardenParticipants;
    private FloatingActionButton backButtom;
    private FirebaseFirestore database;
    private CollectionReference gardensRef;
    private int participants;

    private String gardenID, garden, infoGarden, idUSerColab, groupLink, id;
    private String owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huerta);

        nameGarden = findViewById(R.id.gardenNameText);
        descriptionGarden = findViewById(R.id.descriptionGarden);
        moreFormsButtom = findViewById(R.id.moreFormsButtom);
        backButtom = findViewById(R.id.returnArrowButtonToHome);
        gardenParticipants = (TextView) findViewById(R.id.gardenParticipants);
        editGarden = (ImageButton) findViewById(R.id.imageButton8);
        gardens = (Button) findViewById(R.id.gardens);
        profile = (Button) findViewById(R.id.profile);
        seedTime = (ImageButton) findViewById(R.id.seedTime);
        toolsButton = (ImageButton) findViewById(R.id.toolsButton);
        worm = (ImageButton) findViewById(R.id.imageButtonWorm);
        collaboratorGardens = (ImageButton) findViewById(R.id.editButton2);
        myGardens = (Button) findViewById(R.id.myGardens);
        messages = (ImageButton) findViewById(R.id.messageButton);
        formsRegister = (Button) findViewById(R.id.formsRegister);

        database = FirebaseFirestore.getInstance();
        gardensRef = database.collection("Gardens");



        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("ID");
            garden = extras.getString("gardenName");
            gardenID = extras.getString("idGarden");
            groupLink = extras.getString("GroupLink");
            owner = extras.getString("owner");
            System.out.println("El que es "+ owner);
            SearchInfoGardenSreen(id,garden);
        }

        if(!Objects.equals(owner, "true")){
            editGarden.setVisibility(View.INVISIBLE);
            editGarden.setClickable(false);
            collaboratorGardens.setVisibility(View.INVISIBLE);
            collaboratorGardens.setClickable(false);
        }
        else{
            if(groupLink != null){
                insertGroupLink(groupLink, gardenID);
            }
        }

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        moreFormsButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoForms = new Intent(GardenActivity.this, GardenForms.class);
                String idGardenFirebase = extras.getString("idGardenFirebaseDoc");
                infoForms.putExtra("idGardenFirebaseDoc",idGardenFirebase);
                infoForms.putExtra("Register/Forms","Forms");
                startActivity(infoForms);
            }
        });


        editGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent start = new Intent(GardenActivity.this, GardenEditActivity.class);
                start.putExtra("idGarden", gardenID);
                start.putExtra("gardenName", garden);
                start.putExtra("infoGarden", infoGarden);
                startActivity(start);
            }
        });


        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenActivity.this, MapsActivity.class));
            }
        });


        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenActivity.this, HomeActivity.class));
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenActivity.this, EditUserActivity.class));
            }
        });
        String idGardenFirebase = getIntent().getStringExtra("idGardenFirebaseDoc");
        String formsName = "Control de Procesos de Siembra";

        seedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newForm = new Intent(GardenActivity.this, Form_CPS.class);
                newForm.putExtra("Name",formsName);
                newForm.putExtra("idGardenFirebase",idGardenFirebase);
                startActivity(newForm);
                finish();
            }
        });
        String formsName2 = "Control de Inventarios de Herramientas";

        toolsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newForm = new Intent(GardenActivity.this, Form_CIH.class);
                newForm.putExtra("Name",formsName2);
                newForm.putExtra("idGardenFirebase",idGardenFirebase);
                startActivity(newForm);
                finish();
            }
        });


        worm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newForm = new Intent(GardenActivity.this, Form_RAC.class);
                newForm.putExtra("Name",formsName2);
                newForm.putExtra("idGardenFirebase",idGardenFirebase);
                startActivity(newForm);
                finish();
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(owner, "false")){
                    goToLink(gardenID);
                }
                else{
                    Intent newForm = new Intent(GardenActivity.this, WhatsappActivity.class);
                    newForm.putExtra("ID", id);
                    newForm.putExtra("idGarden", gardenID);
                    newForm.putExtra("gardenName", garden);
                    // newForm.putExtra("infoGarden", infoGarden);
                    newForm.putExtra("owner", owner);
                    startActivity(newForm);
                    finish();
                }

            }
        });

        formsRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoForms = new Intent(GardenActivity.this, GardenForms.class);
                String idGardenFirebase = extras.getString("idGardenFirebaseDoc");
                infoForms.putExtra("idGardenFirebaseDoc",idGardenFirebase);
                infoForms.putExtra("Register/Forms","Register");
                startActivity(infoForms);
            }
        });

        collaboratorGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollaboratorUtilities cU = new CollaboratorUtilities();
                Intent requests = new Intent(GardenActivity.this, GardenRequestsActivity.class);
                requests.putExtra("Name",formsName2);
                requests.putExtra("idGardenFirebase",idGardenFirebase);
                startActivity(requests);
                finish();
                //cU.acceptRequest("ZEhfjQHgINTIVTWtwxTMj2MWEbe2", idGardenFirebase, true);
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
                    //para conocer el numero de participantes de la huerta
                    ref.collection("Collaborators").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    participants = task.getResult().size();
                                    fillSreen(gardenInfo, participants);
                                }
                            });
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
        /*
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
        });*/
    }

    private void fillSreen(GardenInfo gardenInfo, int gardenParticipant){
        nameGarden.setText(gardenInfo.getName());
        if(gardenParticipant == 1){
            gardenParticipants.setText(gardenParticipant+ " Participante de la huerta");
        }else {
            gardenParticipants.setText(gardenParticipant+ " Participantes de la huerta");
        }
        descriptionGarden.setText(gardenInfo.getInfo());
    }
    private void insertGroupLink(String link, String idGarden){
        Map<String, Object> gardenLink = new HashMap<>();
        gardenLink.put("Garden_Chat_Link",link);
        DocumentReference documentRef = database.collection("Gardens").document(idGarden);

        documentRef.update(gardenLink);
        Toast.makeText(GardenActivity.this, "Se agregó el link exitosamente", Toast.LENGTH_SHORT).show();
    }
    private void goToLink(String idGardem){
        database.collection("Gardens").document(idGardem).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            String link = task.getResult().get("Garden_Chat_Link").toString();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(link));
                            startActivity(intent);
                        }
                    }
                });
    }
}