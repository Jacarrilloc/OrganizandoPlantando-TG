package com.example.opcv.auth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.NewToAppActivity;
import com.example.opcv.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DeleteAccountActivity extends AppCompatActivity {

    private Button delete, returnButton, gardensMap, profile, myGardens;

    private FirebaseAuth autentication;
    private FirebaseFirestore database;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeleteAccountActivity.this, EditUserActivity.class));
            }
        });
        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeleteAccountActivity.this, HomeActivity.class));
            }
        });

        gardensMap = (Button) findViewById(R.id.gardens);

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeleteAccountActivity.this, MapsActivity.class));
            }
        });

        returnButton = (Button) findViewById(R.id.returnButton2);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeleteAccountActivity.this, EditUserActivity.class));
            }
        });

        delete = (Button) findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = autentication.getCurrentUser();
                idUser = user.getUid();
                deleteUser(idUser, user);
                user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DeleteAccountActivity.this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                        autentication.signOut();
                        Intent start = new Intent(DeleteAccountActivity.this, NewToAppActivity.class);
                        startActivity(start);
                    }
                });
            }
        });
    }

    private void deleteUser(String id, FirebaseUser auth){


        database.collection("UserInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String userId;
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        userId = (String) document.getData().get("ID");
                        if (userId == null) {
                            userId = (String) document.getData().get("id");
                        }
                        if(userId.equals(id)){
                            String idCollection = document.getId().toString();
                            database.collection("UserInfo")
                                    .document(idCollection)
                                    .delete();

                        }
                    }
                }
            }
        });
        auth.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "User account deleted.");
                }
            }
        });
        //Toast.makeText(this, idUser, Toast.LENGTH_SHORT).show();
    }


}