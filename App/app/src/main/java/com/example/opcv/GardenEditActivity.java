package com.example.opcv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.info.GardenInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class GardenEditActivity extends AppCompatActivity {
    private EditText gardenName, comunity, description;
    private Button acceptChanges, gardens, myGardens, profile, deleteGarden;
    private CheckBox publicGarden,privateGarden;
    private FirebaseAuth autentication;
    private FirebaseFirestore database, database2;
    private FirebaseUser userLog;

    private CollectionReference gardensRef;
    private String idUser, idGarden;
    private Boolean response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_edit);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idGarden = extras.getString("idGarden");
        }

       // System.out.println("El id creado es: " +idGarden);
        //Bundle extras = getIntent().getExtras();
        if(extras != null){

            response = extras.getBoolean("response");

        }
        if(response){
            System.out.println("response " +idGarden);
            deleteGarden(idGarden);
        }

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        gardensRef = database.collection("Gardens");


        gardenName = (EditText) findViewById(R.id.gardenName);
        comunity = (EditText) findViewById(R.id.gardenInfo);
        description = (EditText) findViewById(R.id.gardenDescription);
        publicGarden = (CheckBox) findViewById(R.id.checkbox_public_Create_Activity);
        privateGarden = (CheckBox) findViewById(R.id.checkbox_private_Create_Activity);

        gardens = (Button) findViewById(R.id.gardens);
        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenEditActivity.this, VegetablePatchAvailableActivity.class));
            }
        });

        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenEditActivity.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenEditActivity.this, EditUserActivity.class));
            }
        });

        acceptChanges = (Button) findViewById(R.id.acceptChanges);
        acceptChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editGardenInfo();
            }
        });

        deleteGarden = (Button) findViewById(R.id.deleteGarden);
        deleteGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(GardenEditActivity.this,deleteGardenConfirmationActivity.class);
                start.putExtra("idGarden",idGarden);

                startActivity(start);

                //deleteGarden(idGarden, response);

            }
        });
    }

    private void deleteGarden(String idGarden) {
        database2 = FirebaseFirestore.getInstance();
        database2.collection("Gardens")
                .document(idGarden)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(GardenEditActivity.this, "Se borro exitosamente tu huerta", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GardenEditActivity.this, "Error al borrar la huerta", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void editGardenInfo(){
        String name = gardenName.getText().toString();
        String info = description.getText().toString();
        Boolean gardenPublic = publicGarden.isChecked();
        Boolean gardenPrivate = privateGarden.isChecked();

        if(validateField(name, info, gardenPublic, gardenPrivate)){
            FirebaseUser user = autentication.getCurrentUser();
            CollectionReference collectionRef = database.collection("Gardens");

            idUser = user.getUid();
            searchGarden(idUser, name, info, gardenPublic, gardenPrivate);
            Toast.makeText(GardenEditActivity.this, "Se modificó exitosamente tu huerta", Toast.LENGTH_SHORT).show();

            //System.out.println("El id es: "+collectionRef.getPath().toString());
        }
    }

    private void searchGarden(String idUser, String name, String info, Boolean gardenPublic, Boolean gardenPrivate) {
                database.collection("Gardens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String userId;
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                userId = document.getData().get("ID_Owner").toString();
                                if(userId.equals(idUser) ){
                                    //String idCollection = document.getId().toString();
                                    final DocumentReference docRef = database.collection("Gardens").document(idGarden);
                                    database.runTransaction(new Transaction.Function<Void>() {
                                        @Nullable
                                        @Override
                                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                            DocumentSnapshot snapshot = transaction.get(docRef);
                                            if(gardenPublic){
                                                transaction.update(docRef, "GardenName", name, "InfoGarden", info, "GardenType", "Public");
                                            }
                                            if(gardenPrivate){
                                                transaction.update(docRef, "GardenName", name, "InfoGarden", info, "GardenType", "Private");
                                            }

                                            return null;
                                        }
                                    });

                                    /*database.collection("Gardens").document(idCollection)
                                            .update("GardenName", name);
*/
                                    //System.out.println("El id es: "+idCollection);
                                }

                            }
                        }
                        else{
                            Toast.makeText(GardenEditActivity.this, "No tienes permiso para actualizar la huerta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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