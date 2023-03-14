package com.example.opcv;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.adapter.GardenListAdapter;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.conectionInfo.NetworkMonitorService;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.gardens.CreateGardenActivity;
import com.example.opcv.gardens.huertaActivity;
import com.example.opcv.info.User;
import com.example.opcv.item_list.ItemGardenHomeList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class HomeActivity extends AppCompatActivity {
    private Button otherGardensButton, profile, myGardens;
    private ListView listAviableGardensInfo;
    private FloatingActionButton nextArrow, addButton;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;
    private Animation animSlideUp;

    private  Button gardensMap;

    private String idHuerta;
    private User userInfo;
    private String userId;

    private NetworkMonitorService monitorService = new NetworkMonitorService();

    @Override
    protected void onStart() {
        super.onStart();
        fillGardenUser();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(monitorService, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(monitorService);
    }

    @Override
    protected void onResume() {
        fillGardenUser();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("¿Estás seguro de que quieres salir de Ceres?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finishAffinity();
                        HomeActivity.super.onBackPressed();
                    }
                }).create().show();
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
        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(monitorService, filter);

        userId = getIntent().getStringExtra("userID");

        if (userId == null){
            AuthUtilities auth = new AuthUtilities();
            userId = auth.getCurrentUserUid();
        }

        fillGardenUser();

        listAviableGardensInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Object selectedItem = adapterView.getItemAtPosition(i);
                String itemName = ((ItemGardenHomeList) selectedItem).getName();
                String userID = userId;
                String idGarden = ((ItemGardenHomeList) selectedItem).getIdGarden();
                String idGardenFirebaseDoc = getIntent().getStringExtra("idGarden");
                Intent start = new Intent(HomeActivity.this, huertaActivity.class);
                start.putExtra("ID",userID);
                start.putExtra("gardenName",itemName);
                start.putExtra("idGarden", idGarden);
                start.putExtra("idGardenFirebaseDoc",idGarden);
                startActivity(start);
                finish();
            }
        });


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
                if(userId != null) {
                    Intent edit = new Intent(HomeActivity.this, EditUserActivity.class);
                    edit.putExtra("userInfo", userId);
                    startActivity(edit);
                }
            }
        });
        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
            }
        });

        gardensMap = (Button) findViewById(R.id.globalMap);

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
            }
        });

    }

    private void fillGardenUser(){
        //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        CollectionReference Ref = database.collection("Gardens");
        /*try {
            TimeUnit.SECONDS.sleep(2);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

*/
        Query query = Ref.whereEqualTo("ID_Owner", userId);
        query.whereEqualTo("ID_Owner", userId).addSnapshotListener(new EventListener<QuerySnapshot>() {

            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Se genero error: ", e);
                    return;
                }
                for(DocumentSnapshot documentSnapshot : value){
                    if(documentSnapshot.exists()){
                        List<ItemGardenHomeList> gardenNames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            String name = document.getString("GardenName");
                            String gardenId = document.getId();

                            ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId);
                            gardenNames.add(newItem);
                        }
                    /*String newString;
                    Bundle extras = getIntent().getExtras();
                    if(extras==null){
                        newString = null;
                    }
                    else {
                        newString = extras.getString("idGarden");
                    }
                    idHuerta = newString;*/

                        fillListGardens(gardenNames);
                    } else {
                        Toast.makeText(HomeActivity.this, "Error al obtener los documentos", Toast.LENGTH_SHORT).show();
                    }
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
