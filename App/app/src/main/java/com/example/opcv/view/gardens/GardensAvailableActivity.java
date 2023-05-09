package com.example.opcv.view.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.model.persistance.garden.GardenPersistance;
import com.example.opcv.view.auth.SignOffActivity;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.adapter.GardenListAdapter;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.model.items.ItemGardenHomeList;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GardensAvailableActivity extends AppCompatActivity {

    private Button rewards, profile, myGardens, ludification;
    private ImageView mapIcon;
    private FirebaseAuth autentication;
    private ListView listGardens;
    private FirebaseFirestore database;
    String userID;
    private Animation animSlideUp;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_patch_available);
        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        listGardens = findViewById(R.id.gardenList);
        rewards = (Button) findViewById(R.id.rewards);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        ludification = (Button) findViewById(R.id.ludification);
        mapIcon = (ImageView) findViewById(R.id.mapIcon);
        AuthCommunication authCommunication = new AuthCommunication();
        FirebaseUser user = authCommunication.guestUser();

        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardensAvailableActivity.this, MapsActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCommunication authCommunication = new AuthCommunication();
                FirebaseUser user = authCommunication.guestUser();
                if(user != null && !user.isAnonymous()){
                    startActivity(new Intent(GardensAvailableActivity.this, EditUserActivity.class));
                }
                else{
                    startActivity(new Intent(GardensAvailableActivity.this, SignOffActivity.class));
                }

            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardensAvailableActivity.this, HomeActivity.class));
            }
        });


        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCommunication authCommunication = new AuthCommunication();
                FirebaseUser user = authCommunication.guestUser();
                if(user != null && !user.isAnonymous()){
                    startActivity(new Intent(GardensAvailableActivity.this, RewardHomeActivity.class));
                }
                else{
                    Toast.makeText(GardensAvailableActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        fillGardenAvaliable();

        listGardens.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object selectedItem = adapterView.getItemAtPosition(i);
                String itemName = ((ItemGardenHomeList) selectedItem).getName();
                if(user != null && !user.isAnonymous()){
                    userID = autentication.getCurrentUser().getUid();
                }
                else{
                    userID = "a";
                }
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

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(GardensAvailableActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(GardensAvailableActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void fillGardenAvaliable() {
        CollectionReference Ref = database.collection("Gardens");
        FirebaseUser user = autentication.getCurrentUser();
        String currentUserId;
        if(user != null && !user.isAnonymous()){
            currentUserId = autentication.getCurrentUser().getUid();
        }
        else{
            currentUserId = "a";
        }


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
                                        persistance.getGardenPicture(gardenId, GardensAvailableActivity.this, new GardenPersistance.GetUri() {
                                            @Override
                                            public void onSuccess(String uri) {
                                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, uri);
                                                gardenNames.add(newItem);
                                                fillListGardens(gardenNames);
                                            }

                                            @Override
                                            public void onFailure(String imageString) {
                                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, imageString);
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
        GardenListAdapter  adapter = new GardenListAdapter(this, gardenInfoDocument);
        listGardens.setAdapter(adapter);
        listGardens.setDividerHeight(15);

    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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