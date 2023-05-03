package com.example.opcv.view.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.opcv.business.persistance.garden.GardenPersistance;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.adapter.MyCollaborationsListAdapter;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.model.entity.GardenInfo;
import com.example.opcv.model.items.ItemCollaboratorsRequest;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.business.persistance.firebase.GardenCommunication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CollaboratorGardensActivity extends AppCompatActivity {
    private Button gardensMap, profile, myGardens, ludification;
    private String userId;
    private ListView listGardens;
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
        setContentView(R.layout.activity_collaborator_gardens);

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        gardensMap = (Button) findViewById(R.id.gardens);
        listGardens = findViewById(R.id.collaborationGardenList);
        Bundle extras = getIntent().getExtras();
        userId = getIntent().getStringExtra("userID");
        if (userId == null){
            AuthCommunication auth = new AuthCommunication();
            userId = auth.getCurrentUserUid();
        }
        if(extras != null){
            String id = extras.getString("ID");
            //garden = extras.getString("Name");
            //gardenId = extras.getString("idGardenFirebase");

        }
        //System.out.println("el id xd "+userId);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId != null) {
                    Intent edit = new Intent(CollaboratorGardensActivity.this, EditUserActivity.class);
                    edit.putExtra("userInfo", userId);
                    startActivity(edit);
                }
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CollaboratorGardensActivity.this, HomeActivity.class));
            }
        });



        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CollaboratorGardensActivity.this, MapsActivity.class));
            }
        });
        fillGardenUser();
        listGardens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object selectedItem = adapterView.getItemAtPosition(i);
                String itemName = ((ItemCollaboratorsRequest) selectedItem).getName();
                String userID = userId;
                String idGarden = ((ItemCollaboratorsRequest) selectedItem).getIdGarden();
                String idGardenFirebaseDoc = getIntent().getStringExtra("idGarden");

                Intent start = new Intent(CollaboratorGardensActivity.this, GardenActivity.class);
                start.putExtra("ID",userID);
                start.putExtra("gardenName",itemName);
                start.putExtra("idGarden", idGarden);
                start.putExtra("idGardenFirebaseDoc",idGarden);
                start.putExtra("owner", "false");
                startActivity(start);
                finish();
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(CollaboratorGardensActivity.this, DictionaryHomeActivity.class);
                startActivity(edit);
            }
        });
    }
    private void fillGardenUser(){
        CollectionReference Ref = database.collection("UserInfo");

        Query query = Ref.whereEqualTo("ID", userId);
        if(query.equals(null)){
            query = Ref.whereEqualTo("id", userId);
        }
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Se genero error: ", e);
                    return;
                }
                for(DocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.exists()) {
                        for (QueryDocumentSnapshot document : value) {

                            database.collection("UserInfo").document(document.getId()).collection("GardensCollaboration").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                List<ItemCollaboratorsRequest> gardenNames = new ArrayList<>();
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String idUser = document.getId().toString();

                                                    database.collection("Gardens").document(document.getData().get("idGardenCollab").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            String idOwner, name, info, gardenType, idGarden;

                                                            if(task.isSuccessful()){

                                                                for(QueryDocumentSnapshot documen:value){
                                                                    GardenInfo idSearch;
                                                                    String nameUser = task.getResult().get("GardenName").toString();
                                                                    idOwner = task.getResult().get("ID_Owner").toString();
                                                                    info = task.getResult().get("InfoGarden").toString();
                                                                    gardenType = task.getResult().get("GardenType").toString();
                                                                    //idSearch = new GardenInfo(idOwner, name, info, gardenType);
                                                                    String idGarde = document.getData().get("idGardenCollab").toString();
                                                                    GardenPersistance persistance = new GardenPersistance();
                                                                    persistance.getGardenPicture(idGarde, CollaboratorGardensActivity.this, new GardenPersistance.GetUri() {
                                                                        @Override
                                                                        public void onSuccess(String uri) {
                                                                            ItemCollaboratorsRequest newItem = new ItemCollaboratorsRequest(nameUser, userId, idGarde, uri);
                                                                            //System.out.println("EL id es "+newItem.getName());
                                                                            gardenNames.add(newItem);
                                                                            fillListGardens(gardenNames);
                                                                        }

                                                                        @Override
                                                                        public void onFailure(String imageString) {
                                                                            ItemCollaboratorsRequest newItem = new ItemCollaboratorsRequest(nameUser, userId, idGarde, imageString);
                                                                            //System.out.println("EL id es "+newItem.getName());
                                                                            gardenNames.add(newItem);
                                                                            fillListGardens(gardenNames);
                                                                        }
                                                                    });

                                                                }

                                                            }

                                                        }
                                                    });


                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });

    }
    private void fillListGardens( List<ItemCollaboratorsRequest> gardenInfoDocument){
        try{
            Thread.sleep(65);
            MyCollaborationsListAdapter adapter = new MyCollaborationsListAdapter(this, gardenInfoDocument);
            listGardens.setAdapter(adapter);
            listGardens.setDividerHeight(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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