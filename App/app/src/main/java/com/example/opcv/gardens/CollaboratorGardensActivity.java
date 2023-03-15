package com.example.opcv.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.adapter.GardenListAdapter;
import com.example.opcv.adapter.MyCollaborationsListAdapter;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.conectionInfo.NetworkMonitorService;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.fbComunication.CollaboratorRequestUtilities;
import com.example.opcv.info.GardenInfo;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.example.opcv.item_list.ItemGardenHomeList;
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
    private Button gardensMap, profile, myGardens;
    private String userId;
    private ListView listGardens;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;
    private NetworkMonitorService monitorService = new NetworkMonitorService();

    @Override
    protected void onStart() {
        super.onStart();
        fillGardenUser();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(monitorService, filter);
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
            AuthUtilities auth = new AuthUtilities();
            userId = auth.getCurrentUserUid();
        }
        if(extras != null){
            String id = extras.getString("ID");
            //garden = extras.getString("Name");
            //gardenId = extras.getString("idGardenFirebase");

        }
        System.out.println("el id xd "+userId);

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

                Intent start = new Intent(CollaboratorGardensActivity.this, huertaActivity.class);
                start.putExtra("ID",userID);
                start.putExtra("gardenName",itemName);
                start.putExtra("idGarden", idGarden);
                start.putExtra("idGardenFirebaseDoc",idGarden);
                start.putExtra("owner", false);
                startActivity(start);
                finish();
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
                                                                    name = task.getResult().get("GardenName").toString();
                                                                    idOwner = task.getResult().get("ID_Owner").toString();
                                                                    info = task.getResult().get("InfoGarden").toString();
                                                                    gardenType = task.getResult().get("GardenType").toString();
                                                                    idSearch = new GardenInfo(idOwner, name, info, gardenType);
                                                                    idGarden = document.getData().get("idGardenCollab").toString();

                                                                    ItemCollaboratorsRequest newItem = new ItemCollaboratorsRequest(name, userId, idGarden);
                                                                    System.out.println("EL id es "+newItem.getName());
                                                                    gardenNames.add(newItem);
                                                                }
                                                                fillListGardens(gardenNames);
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
        MyCollaborationsListAdapter adapter = new MyCollaborationsListAdapter(this, gardenInfoDocument);
        listGardens.setAdapter(adapter);
        listGardens.setDividerHeight(5);
    }


}