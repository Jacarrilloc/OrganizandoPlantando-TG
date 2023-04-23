package com.example.opcv.gardens;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.adapter.GardenListAdapter;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.item_list.ItemGardenHomeList;
import com.example.opcv.ludificationScreens.DictionaryHome;
import com.example.opcv.persistance.gardenPersistance.GardenPersistance;
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

public class GardensAvailableActivity extends AppCompatActivity {

    private Button gardensMap, profile, myGardens, ludification;
    private FirebaseAuth autentication;
    private ListView listGardens;
    private FirebaseFirestore database;
    String userID;
    private Animation animSlideUp;

    @Override
    protected void onStart() {
        super.onStart();
        fillGardenAvaliable();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_patch_available);
        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        listGardens = findViewById(R.id.gardenList);

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardensAvailableActivity.this, EditUserActivity.class));
            }
        });
        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardensAvailableActivity.this, HomeActivity.class));
            }
        });

        gardensMap = (Button) findViewById(R.id.gardens);
        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardensAvailableActivity.this, MapsActivity.class));
            }
        });


        fillGardenAvaliable();

        listGardens.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object selectedItem = adapterView.getItemAtPosition(i);
                String itemName = ((ItemGardenHomeList) selectedItem).getName();
                userID = autentication.getCurrentUser().getUid();
                String idGarden = ((ItemGardenHomeList) selectedItem).getIdGarden();
                String idGardenFirebaseDoc = getIntent().getStringExtra("idGarden");
                Intent start = new Intent(GardensAvailableActivity.this, OtherGardensActivity.class);
                start.putExtra("ID",userID);
                start.putExtra("gardenName",itemName);
                start.putExtra("idGarden", idGarden);
                start.putExtra("idGardenFirebaseDoc",idGarden);
                startActivity(start);
                finish();
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(GardensAvailableActivity.this, DictionaryHome.class);
                startActivity(edit);
            }
        });
    }
    private void fillGardenAvaliable() {
        CollectionReference Ref = database.collection("Gardens");
        String currentUserId = autentication.getCurrentUser().getUid();
        Query query = Ref.whereEqualTo("GardenType", "Public").whereNotEqualTo("ID_Owner", currentUserId);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {

            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Se generó error: ", e);
                    return;
                }

                List<ItemGardenHomeList> gardenNames = new ArrayList<>();
                for (QueryDocumentSnapshot document : value) {
                    String name = document.getString("GardenName");
                    String gardenId = document.getId();
                    // Check if user is not a collaborator
                    document.getReference().collection("Collaborators")
                            .whereEqualTo("idCollaborator", currentUserId)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) { // User is not a collaborator
                                        GardenPersistance persistance = new GardenPersistance();
                                        persistance.getGardenPicture(gardenId, new GardenPersistance.GetUri() {
                                            @Override
                                            public void onSuccess(String uri) {
                                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, uri);
                                                gardenNames.add(newItem);
                                                fillListGardens(gardenNames);
                                            }
                                        });
                                    }
                                } else {
                                    Log.d(TAG, "Error al verificar colaboradores: ", task.getException());
                                }
                            });
                }
            }
        });

    }

    private void fillListGardens( List<ItemGardenHomeList> gardenInfoDocument){
        try{
            Thread.sleep(65);
            GardenListAdapter  adapter = new GardenListAdapter(this, gardenInfoDocument);
            listGardens.setAdapter(adapter);
            listGardens.setDividerHeight(15);
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