package com.example.opcv;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.adapter.GardenAvailableListAdapter;
import com.example.opcv.adapter.GardenListAdapter;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.gardens.huertaActivity;
import com.example.opcv.gardens.otherGardensActivity;
import com.example.opcv.item_list.ItemGardenHomeList;
import com.example.opcv.item_list.ItemShowGardenAvailable;
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

public class VegetablePatchAvailableActivity extends AppCompatActivity {

    private Button gardensMap, profile, myGardens;
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
                startActivity(new Intent(VegetablePatchAvailableActivity.this, EditUserActivity.class));
            }
        });
        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VegetablePatchAvailableActivity.this, HomeActivity.class));
            }
        });

        gardensMap = (Button) findViewById(R.id.gardens);
        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VegetablePatchAvailableActivity.this, MapsActivity.class));
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
                Intent start = new Intent(VegetablePatchAvailableActivity.this, otherGardensActivity.class);
                start.putExtra("ID",userID);
                start.putExtra("gardenName",itemName);
                start.putExtra("idGarden", idGarden);
                start.putExtra("idGardenFirebaseDoc",idGarden);
                startActivity(start);
                finish();
            }
        });
    }
    private void fillGardenAvaliable(){
        CollectionReference Ref = database.collection("Gardens");


        Query query = Ref.whereEqualTo("GardenType", "Public").whereNotEqualTo("ID_Owner", autentication.getCurrentUser().getUid());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {

            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Se generó error: ", e);
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
                        fillListGardens(gardenNames);
                    } else {
                        Toast.makeText(VegetablePatchAvailableActivity.this, "Error al obtener los documentos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void fillListGardens( List<ItemGardenHomeList> gardenInfoDocument){
        GardenListAdapter  adapter = new GardenListAdapter(this, gardenInfoDocument);
        listGardens.setAdapter(adapter);
        listGardens.setDividerHeight(15);
    }
}