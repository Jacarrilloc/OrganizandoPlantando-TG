package com.example.opcv.view.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.view.gardens.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteAccountActivity extends AppCompatActivity {

    private Button delete, returnButton, gardensMap, profile, myGardens, ludification;

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
        myGardens = (Button) findViewById(R.id.myGardens);
        gardensMap = (Button) findViewById(R.id.gardens);
        ludification = (Button) findViewById(R.id.ludification);
        returnButton = (Button) findViewById(R.id.returnButton2);
        delete = (Button) findViewById(R.id.deleteButton);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeleteAccountActivity.this, EditUserActivity.class));
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeleteAccountActivity.this, HomeActivity.class));
            }
        });


        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeleteAccountActivity.this, MapsActivity.class));
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(DeleteAccountActivity.this, DictionaryHomeActivity.class);
                startActivity(edit);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeleteAccountActivity.this, EditUserActivity.class));
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = autentication.getCurrentUser();
                idUser = user.getUid();
                deletePhoto(idUser);
                deleteUser(idUser, user);
                user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DeleteAccountActivity.this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                        autentication.signOut();
                        Intent intent = new Intent(DeleteAccountActivity.this, NewToAppActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void deleteUser(String id, FirebaseUser auth){

        database.collection("UserInfo").document(id).delete();
/*
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
        });*/
        //Toast.makeText(this, idUser, Toast.LENGTH_SHORT).show();
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

    private void deletePhoto(String idUser){

    }

}