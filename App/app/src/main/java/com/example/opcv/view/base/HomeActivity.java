package com.example.opcv.view.base;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.view.adapter.GardenListAdapter;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.view.gardens.CollaboratorGardensActivity;
import com.example.opcv.view.gardens.CreateGardenActivity;
import com.example.opcv.view.gardens.GardenActivity;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.example.opcv.model.entity.User;
import com.example.opcv.model.items.ItemGardenHomeList;
import com.example.opcv.view.gardens.GenerateReportsActivity;
import com.example.opcv.view.gardens.MapsActivity;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.business.persistance.garden.GardenPersistance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Button otherGardensButton, profile, myGardens, collaboration, ludification;
    private ImageButton generateReport;
    private ListView listAviableGardensInfo;
    private FloatingActionButton nextArrow, addButton;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;
    private Animation animSlideUp;

    private  Button gardensMap;

    private String idHuerta;
    private User userInfo;
    private String userId;

    private Intent serviceIntent;

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
    @Override
    protected void onStart() {
        super.onStart();
        fillGardenUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillGardenUser();
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

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        database.setFirestoreSettings(settings);

        //Declaracion metodos de navegacion
        listAviableGardensInfo = findViewById(R.id.listAviableGardens);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        collaboration = (Button) findViewById(R.id.collaboratorGardens);
        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        otherGardensButton = (Button) findViewById(R.id.otherGardensButton);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        gardensMap = (Button) findViewById(R.id.gardens);
        generateReport = (ImageButton) findViewById(R.id.generalReport);
        ludification = (Button) findViewById(R.id.ludification);

        userId = getIntent().getStringExtra("userID");

        if (userId == null){
            AuthCommunication auth = new AuthCommunication();
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
                Intent start = new Intent(HomeActivity.this, GardenActivity.class);
                start.putExtra("ID",userID);
                start.putExtra("gardenName",itemName);
                start.putExtra("idGarden", idGarden);
                start.putExtra("idGardenFirebaseDoc",idGarden);
                start.putExtra("owner", "true");
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


        otherGardensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, GardensAvailableActivity.class));
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(HomeActivity.this, DictionaryHomeActivity.class);
                startActivity(edit);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId != null) {
                    Intent edit = new Intent(HomeActivity.this, EditUserActivity.class);
                    edit.putExtra("userInfo", userId);
                    startActivity(edit);
                }
                else{
                    AuthCommunication auth = new AuthCommunication();
                    userId = auth.getCurrentUserUid();
                    Intent edit = new Intent(HomeActivity.this, EditUserActivity.class);
                    edit.putExtra("userInfo", userId);
                    startActivity(edit);
                }
            }
        });

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
            }
        });

        collaboration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent edit = new Intent(HomeActivity.this, CollaboratorGardensActivity.class);
                edit.putExtra("userID", userId);
                startActivity(edit);
            }
        });

        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent requests = new Intent(HomeActivity.this, GenerateReportsActivity.class);
                requests.putExtra("idGardenFirebaseDoc","null");
                requests.putExtra("idUser","null");
                requests.putExtra("garden","false");//con esto se define si, al ejecutar GenerateReportsActivity es solo para la huerta o para todos
                requests.putExtra("ownerName","null");
                startActivity(requests);
                finish();
            }
        });

    }

    private void fillGardenUser(){
        //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        CollectionReference Ref = database.collection("Gardens");

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
                            GardenPersistance persistance = new GardenPersistance();
                            if(isOnline()) {
                                persistance.getGardenPicture(gardenId, HomeActivity.this, new GardenPersistance.GetUri() {
                                    @Override
                                    public void onSuccess(String uri) {
                                        ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, uri);
                                        gardenNames.add(newItem);
                                        fillListGardens(gardenNames);

                                    }
                                });
                            }else{
                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, null);
                                gardenNames.add(newItem);
                                fillListGardens(gardenNames);
                            }
                        }
                        //fillListGardens(gardenNames);
                    } else {
                        Toast.makeText(HomeActivity.this, "Error al obtener los documentos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void fillListGardens( List<ItemGardenHomeList> gardenInfoDocument){
        try {
            if(isOnline()) {
                Thread.sleep(65);
            }
            GardenListAdapter adapter = new GardenListAdapter(this, gardenInfoDocument);
            listAviableGardensInfo.setAdapter(adapter);
            listAviableGardensInfo.setDividerHeight(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}