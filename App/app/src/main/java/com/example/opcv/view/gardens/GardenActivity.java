package com.example.opcv.view.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opcv.business.persistance.garden.GardenPersistance;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.business.persistance.firebase.CollaboratorCommunication;
import com.example.opcv.view.forms.Form_CIH;
import com.example.opcv.view.forms.Form_CPS;
import com.example.opcv.view.forms.Form_RAC;
import com.example.opcv.model.entity.GardenInfo;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GardenActivity extends AppCompatActivity {

    private Button formsRegister, rewards, myGardens, profile, ludification;
    private ImageButton editGarden, seedTime, toolsButton, worm, collaboratorGardens, messages, generateReport;

    private ImageView moreFormsButtom,gardenImage;
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
        rewards = (Button) findViewById(R.id.rewards);
        profile = (Button) findViewById(R.id.profile);
        seedTime = (ImageButton) findViewById(R.id.seedTime);
        toolsButton = (ImageButton) findViewById(R.id.toolsButton);
        worm = (ImageButton) findViewById(R.id.imageButtonWorm);
        collaboratorGardens = (ImageButton) findViewById(R.id.editButton2);
        myGardens = (Button) findViewById(R.id.myGardens);
        messages = (ImageButton) findViewById(R.id.messageButton);
        formsRegister = (Button) findViewById(R.id.formsRegister);
        generateReport = (ImageButton) findViewById(R.id.imageButton9);
        database = FirebaseFirestore.getInstance();
        gardensRef = database.collection("Gardens");
        gardenImage = findViewById(R.id.gardenProfilePicture);
        GardenPersistance gardenPersistance = new GardenPersistance();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("ID");
            garden = extras.getString("gardenName");
            gardenID = extras.getString("idGarden");
            groupLink = extras.getString("GroupLink");
            owner = extras.getString("owner");
            //System.out.println("El que es "+ owner);
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

        gardenPersistance.getGardenPicture(gardenID, this, new GardenPersistance.GetUri() {
            @Override
            public void onSuccess(String uri) {
                Glide.with(GardenActivity.this).load(uri).into(gardenImage);
            }

            @Override
            public void onFailure(String imageString) {
                Glide.with(GardenActivity.this).load(imageString).into(gardenImage);
            }
        });

        //getImageGarden(extras.getString("idGarden"));
        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        moreFormsButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoForms = new Intent(GardenActivity.this, GardenFormsActivity.class);
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


        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenActivity.this, RewardHomeActivity.class));
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
                newForm.putExtra("watch","create");
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
                newForm.putExtra("watch","create");
                startActivity(newForm);
                finish();
            }
        });


        String formname3 = "Registro y Actualización de Compostaje";
        worm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newForm = new Intent(GardenActivity.this, Form_RAC.class);
                newForm.putExtra("Name",formname3);
                newForm.putExtra("idGardenFirebase",idGardenFirebase);
                newForm.putExtra("watch","create");
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
                Intent infoForms = new Intent(GardenActivity.this, GardenFormsActivity.class);
                String idGardenFirebase = extras.getString("idGardenFirebaseDoc");
                infoForms.putExtra("idGardenFirebaseDoc",idGardenFirebase);
                infoForms.putExtra("Register/Forms","Register");
                startActivity(infoForms);
            }
        });

        collaboratorGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollaboratorCommunication cU = new CollaboratorCommunication();
                Intent requests = new Intent(GardenActivity.this, GardenRequestsActivity.class);
                requests.putExtra("Name",formsName2);
                requests.putExtra("idGardenFirebase",idGardenFirebase);
                startActivity(requests);
                finish();
            }
        });

        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idGardenFirebase = extras.getString("idGardenFirebaseDoc");
                Intent requests = new Intent(GardenActivity.this, GenerateReportsActivity.class);
                requests.putExtra("idGardenFirebaseDoc",idGardenFirebase);
                requests.putExtra("idUser",id);
                requests.putExtra("garden","true");// con esto se define si, al ejecutar GenerateReportsActivity es solo para la huerta o para todos
                CollectionReference collectionRef2 = database.collection("UserInfo");
                Query query = collectionRef2.whereEqualTo("ID", id);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String name = document.getData().get("Name").toString();
                                String lastName =document.getData().get("LastName").toString();
                                requests.putExtra("ownerName",name+" "+lastName);
                                startActivity(requests);
                                finish();
                            }
                        }
                    }
                });
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(GardenActivity.this, DictionaryHomeActivity.class);
                startActivity(edit);
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent toHome = new Intent(GardenActivity.this,HomeActivity.class);
        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toHome);
    }

    private void SearchInfoGardenSreen(String idUser, String name){
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
                            String link = (String) task.getResult().get("Garden_Chat_Link");
                            if(link != null){
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(link));
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(GardenActivity.this, "Esta huerta no tiene chat grupal", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    private void getImageGarden(String idGarden){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String imageName = idGarden + ".jpg";
        StorageReference imageRef = storageRef.child("gardenMainPhoto/" + imageName);
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                gardenImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                gardenImage.setImageResource(R.drawable.im_logo_ceres_green);
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);
        super.attachBaseContext(newBase);
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration config = new Configuration(newConfig);
        adjustFontScale(getApplicationContext(), config);
    }
    public static void adjustFontScale(Context context, Configuration configuration) {
        if (configuration.fontScale != 1) {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }
}