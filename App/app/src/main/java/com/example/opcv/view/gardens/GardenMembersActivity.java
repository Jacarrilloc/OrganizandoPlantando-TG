package com.example.opcv.view.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.view.adapter.GardenMembersAdapter;
import com.example.opcv.business.interfaces.OnCollaboratorIdsObtained;
import com.example.opcv.model.items.ItemCollaborator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GardenMembersActivity extends AppCompatActivity {
    private String idGarden, name;
    private OnCollaboratorIdsObtained callback;
    private FloatingActionButton backButton;
    private FirebaseAuth autentication;
    private ListView listMembers;
    private Animation animSlideUp;
    private FirebaseFirestore database;

    @Override
    protected void onStart() {
        super.onStart();
        fillGardenMembers();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_members);

        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        listMembers = findViewById(R.id.ListViewembers);
        backButton = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormsToGarden);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idGarden = extras.getString("idGarden");
        }
        fillGardenMembers();

    }

    private void fillGardenMembers(){
        CollectionReference collaboratorsRef = database.collection("Gardens").document(idGarden).collection("Collaborators");

        getCollaboratorIds(collaboratorsRef, new OnCollaboratorIdsObtained() {
            @Override
            public void onCollaboratorIdsObtained(List<String> collaboratorIds) {
                    try{
                        Query query = database.collection("UserInfo").whereIn("ID", collaboratorIds);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if(querySnapshot != null && !querySnapshot.isEmpty()){
                                        String idSearch;

                                        List<ItemCollaborator> membersNames = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            idSearch = (String) document.getData().get("ID");
                                            if (idSearch == null) {
                                                idSearch = (String) document.getData().get("id");
                                            }
                                            if(idSearch != null){
                                                name = (String) document.getData().get("Name");
                                                //Si se necesita mas informacion usar la clase User
                                                ItemCollaborator newItem = new ItemCollaborator(name, idSearch,idGarden);
                                                membersNames.add(newItem);
                                            }
                                            Map<String, Object> userInfo = document.getData(); // obtener los datos del usuario actual
                                            System.out.println(userInfo); // haz algo con los datos del usuario
                                        }
                                        fillListMembers(membersNames);
                                    }

                                } else {
                                    Log.d(TAG, "Error getting user info documents.", task.getException());
                                }
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(GardenMembersActivity.this, "Esta huerta no tiene colaboradores", Toast.LENGTH_LONG).show();
                    }

            }
        });

    }

    // Esta función auxiliar obtiene los identificadores de usuario de la colección "collaborators"
    private List<String> getCollaboratorIds(CollectionReference collaboratorsRef, OnCollaboratorIdsObtained callback) {
        List<String> collaboratorIds = new ArrayList<>();
        collaboratorsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collaboratorIds.add(document.getString("idCollaborator"));
                    }

                    callback.onCollaboratorIdsObtained(collaboratorIds);
                } else {
                    Log.d(TAG, "Error getting collaborator documents.", task.getException());
                }
            }
        });
        return collaboratorIds;
    }
    private void fillListMembers(List<ItemCollaborator> requestDocument){
        GardenMembersAdapter adapter = new GardenMembersAdapter(this, requestDocument);
        listMembers.setAdapter(adapter);
        listMembers.setDividerHeight(15);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}