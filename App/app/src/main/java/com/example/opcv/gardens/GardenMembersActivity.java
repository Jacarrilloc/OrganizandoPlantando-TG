package com.example.opcv.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.adapter.CollaboratorListAdapter;
import com.example.opcv.adapter.GardenMembersAdapter;
import com.example.opcv.interfaces.OnCollaboratorIdsObtained;
import com.example.opcv.item_list.ItemCollaborator;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
        //DocumentReference gardenRef = database.collection("Gardens").document(idGarden);
        CollectionReference collaboratorsRef = database.collection("Gardens").document(idGarden).collection("Collaborators");

        getCollaboratorIds(collaboratorsRef, new OnCollaboratorIdsObtained() {
            @Override
            public void onCollaboratorIdsObtained(List<String> collaboratorIds) {
                    Query query = database.collection("UserInfo").whereIn("ID", collaboratorIds);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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
                        } else {
                            Log.d(TAG, "Error getting user info documents.", task.getException());
                        }
                    }
                });
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
}