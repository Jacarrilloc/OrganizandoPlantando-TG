package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateGardenActivity extends AppCompatActivity {

    private EditText nameGarden,infoGarden;
    private CheckBox publicGarden,privateGarden;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_garden);

        nameGarden = findViewById(R.id.gardenName);
        infoGarden = findViewById(R.id.gardenInfo);
        publicGarden = findViewById(R.id.checkbox_public_Create_Activity);
        privateGarden = findViewById(R.id.checkbox_private_Create_Activity);

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        create = findViewById(R.id.add_garden_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGarden();
            }
        });
    }

    private void createGarden(){
        String name = nameGarden.getText().toString();
        String info = infoGarden.getText().toString();
        Boolean gardenPublic = publicGarden.isChecked();
        Boolean gardenPrivate = privateGarden.isChecked();

        if(validateField(name,info,gardenPublic,gardenPrivate)){
            FirebaseUser user = autentication.getCurrentUser();
            CollectionReference collectionRef = database.collection("Gardens");

            Map<String, Object> gardenInfo = new HashMap<>();
            gardenInfo.put("ID_Owner",user.getUid());
            gardenInfo.put("GardenName",name);
            gardenInfo.put("InfoGarden",info);
            if(gardenPublic){
                gardenInfo.put("GardenType", "Public");
            }
            if(gardenPrivate){
                gardenInfo.put("GardenType", "Private");
            }
            collectionRef.add(gardenInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(CreateGardenActivity.this, "Se Creó exitosamente la Huerta", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateGardenActivity.this, HomeActivity.class));
                }
            });
        }
    }
    private boolean validateField(String name,String info,Boolean gardenPublic,Boolean gardenPrivate){

        if(name.isEmpty() || info.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el nombre e información de la Huerta", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((gardenPublic == false) && (gardenPrivate == false)){
            Toast.makeText(this, "Debes indicar si la huerta es publica o privada", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((gardenPublic == true) && (gardenPrivate == true)){
            Toast.makeText(this, "Debes Seleccionar una sola Opción", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}