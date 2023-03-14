package com.example.opcv.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.VegetablePatchAvailableActivity;
import com.example.opcv.adapter.CollaboratorListAdapter;
import com.example.opcv.adapter.GardenListAdapter;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.example.opcv.item_list.ItemGardenHomeList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.concurrent.CountDownLatch;

public class GardenRequestsActivity extends AppCompatActivity {
    private Button accept, deny;
    private FloatingActionButton backButton;
    private FirebaseAuth autentication;
    private ListView listGardens;
    private FirebaseFirestore database;
    private Animation animSlideUp;
    private String gardenId, garden, name;

    @Override
    protected void onStart() {
        super.onStart();
        fillGardenAvaliable();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_requests);

        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        listGardens = findViewById(R.id.listViewCollabs);
        backButton = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormsToGarden);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String id = extras.getString("ID");
            garden = extras.getString("Name");
            gardenId = extras.getString("idGardenFirebase");

        }
        fillGardenAvaliable();
    }
    private void fillGardenAvaliable(){
        CollectionReference Ref = database.collection("Gardens").document(gardenId).collection("Requests");
        Query query = Ref.whereEqualTo("requestStatus", "SV");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {

            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Se generó error: ", e);
                    return;
                }
                for(DocumentSnapshot documentSnapshot : value){
                    if(documentSnapshot.exists()){
                        List<ItemCollaboratorsRequest> gardenNames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            String idUser = document.getString("idUserRequest");
                            database.collection("UserInfo")
                                    .get()

                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if(!queryDocumentSnapshots.isEmpty()){
                                                String idSearch;
                                                for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                                    idSearch = (String) document.getData().get("ID");
                                                    if (idSearch == null) {
                                                        idSearch = (String) document.getData().get("id");
                                                    }
                                                    if(idSearch.equals(idUser)){
                                                        name = (String) document.getData().get("Name");
                                                        //Si se necesita mas informacion usar la clase User
                                                        ItemCollaboratorsRequest newItem = new ItemCollaboratorsRequest(name, idUser, gardenId);
                                                        gardenNames.add(newItem);
                                                        fillListRequests(gardenNames);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });
                                    /*.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                String idSearch;
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    idSearch = (String) document.getData().get("ID");
                                                    if (idSearch == null) {
                                                        idSearch = (String) document.getData().get("id");
                                                    }
                                                    //System.out.println("El id es: "+idSearch+"y el otro es: "+idUser);
                                                    if(idSearch.equals(idUser)){
                                                        name = (String) document.getData().get("Name");
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });*/

                        }

                    } else {
                        Toast.makeText(GardenRequestsActivity.this, "Error al obtener los documentos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void fillListRequests(List<ItemCollaboratorsRequest> requestDocument){
        CollaboratorListAdapter adapter = new CollaboratorListAdapter(this, requestDocument);
        listGardens.setAdapter(adapter);
        listGardens.setDividerHeight(15);
    }


}