package com.example.opcv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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

import com.example.opcv.adapter.GardenListAdapter;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.business.gardenController.GardenLogic;
import com.example.opcv.business.gardenController.GardenViewModel;
import com.example.opcv.conectionInfo.NetworkMonitorService;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.gardens.CollaboratorGardensActivity;
import com.example.opcv.gardens.CreateGardenActivity;
import com.example.opcv.gardens.GardenActivity;
import com.example.opcv.gardens.GardensAvailableActivity;
import com.example.opcv.info.User;
import com.example.opcv.item_list.ItemGardenHomeList;
import com.example.opcv.localDatabase.DatabaseHelper;
import com.example.opcv.ludificationScreens.DictionaryHome;
<<<<<<< HEAD
import com.example.opcv.persistance.gardenPersistance.GardenPersistance;
import com.example.opcv.repository.GardenRepository;
import com.example.opcv.repository.local_db.Garden;
=======
>>>>>>> parent of 0679807 (Manejo de imagenes listo)
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
    private Button otherGardensButton, profile, myGardens, collaboration, ludification;
    private ImageButton generateReport;
    private ListView listAviableGardensInfo;
    private FloatingActionButton nextArrow, addButton;
    private Animation animSlideUp;

    private  Button gardensMap;
    private String userId;
    private GardenRepository gardenRepository;
    private AuthUtilities authUtilities;


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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillListGardens();
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

        gardenRepository = new GardenRepository(getApplication());
        authUtilities = new AuthUtilities();
        fillListGardens();

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
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        if (userId == null){
            AuthUtilities auth = new AuthUtilities();
            userId = auth.getCurrentUserUid();
        }


        listAviableGardensInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Object selectedItem = adapterView.getItemAtPosition(i);
                String dbID = ((ItemGardenHomeList) selectedItem).getIdGarden();
                Intent start = new Intent(HomeActivity.this, GardenActivity.class);
                start.putExtra("ID_garden",dbID);
                startActivity(start);

                /*
                Intent start = new Intent(HomeActivity.this, GardenActivity.class);
                startActivity(start);

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
                finish();*/
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
                Intent edit = new Intent(HomeActivity.this, DictionaryHome.class);
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


    /*
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
    */

    private void fillListGardens() {
        GardenLogic gardenLogic = new GardenLogic(getApplication());
        gardenLogic.getGardensUser().observe(this, new Observer<List<Garden>>() {
            @Override
            public void onChanged(List<Garden> gardens) {
                // Crea una lista de objetos ItemGardenHomeList a partir de la lista de jardines devuelta
                List<ItemGardenHomeList> gardenInfoDocument = new ArrayList<>();

                for (Garden garden : gardens) {
                    ItemGardenHomeList item = new ItemGardenHomeList(garden.getGardenName(),garden.getId());
                    gardenInfoDocument.add(item);
                }

                // Crea un adaptador para la ListView y actualiza su contenido
                GardenListAdapter adapter = new GardenListAdapter(HomeActivity.this, gardenInfoDocument);
                listAviableGardensInfo.setAdapter(adapter);
                listAviableGardensInfo.setDividerHeight(5);
            }
        });
    }
}
