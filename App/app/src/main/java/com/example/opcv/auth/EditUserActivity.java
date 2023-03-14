package com.example.opcv.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.info.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

public class EditUserActivity extends AppCompatActivity {
    private Button signOff, delete;
    private Button gardensMap, profile, myGardens, acceptChanges;
    private TextView userNameTV, close, deleteP;
    private EditText userName, userLastName, userEmail, userPhone;
    private FirebaseAuth autentication;
    private FirebaseFirestore database, database2;
    private FirebaseUser userLog;
    private String nameUSer;
    private User userActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        signOff = (Button) findViewById(R.id.options);
        signOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, signOffActivity.class));
            }
        });

        delete = (Button) findViewById(R.id.options3);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, deleteAccountActivity.class));
            }
        });
        close = (TextView) findViewById(R.id.options2);
        deleteP = (TextView) findViewById(R.id.options4);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, signOffActivity.class));
            }
        });
        deleteP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, deleteAccountActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, EditUserActivity.class));
            }
        });
        myGardens = (Button) findViewById(R.id.myGardens);
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, HomeActivity.class));
            }
        });

        gardensMap = (Button) findViewById(R.id.globalMap);

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, MapsActivity.class));
            }
        });


        searchUserInfo();
        userNameTV = (TextView) findViewById(R.id.userName);
        userName =(EditText) findViewById(R.id.userName2);
        userLastName = (EditText) findViewById(R.id.lastNameInfo);
        userEmail = (EditText) findViewById(R.id.gardenName);
        userPhone = (EditText) findViewById(R.id.address);

        acceptChanges = (Button) findViewById(R.id.editUser);
        acceptChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, Lastname, Name, PhoneNumber;
                //email=userEmail.getText().toString();
                Lastname=userLastName.getText().toString();
                Name=userName.getText().toString();
                PhoneNumber=userPhone.getText().toString();
                editUserInfo(Name, Lastname, PhoneNumber);
                if(validateField(Name, Lastname)){
                    editUserInfo(Name, Lastname, PhoneNumber);
                    Intent start = new Intent(EditUserActivity.this,HomeActivity.class);
                    startActivity(start);
                }
            }
        });
    }
    private void searchUserInfo (){
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        String userID=autentication.getCurrentUser().getUid().toString();

        database.collection("UserInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            String idSearch;
                            for(QueryDocumentSnapshot document : task.getResult()){
                                idSearch = (String) document.getData().get("ID");
                                if(idSearch == null){
                                    idSearch = (String) document.getData().get("id");
                                }


                                if(idSearch.equals(userID)){
                                    String name, email, lastname, phoneNumber;
                                    name=document.getData().get("Name").toString();
                                    email=document.getData().get("Email").toString();
                                    lastname=document.getData().get("LastName").toString();
                                    phoneNumber=document.getData().get("PhoneNumber").toString();
                                    userActive =  new User(name, email, userID, lastname, phoneNumber, null);
                                    userNameTV.setText(userActive.getName());
                                    userName.setText(userActive.getName());
                                    userEmail.setText("Comabaquinta");
                                    userPhone.setText(userActive.getPhoneNumber());
                                    userLastName.setText(userActive.getLastName());

                                }
                            }
                        }
                    }
                });

        //System.out.println("EL id es "+userID);
    }
    private User returnUser (User userP){
        return userP;
    }

    private void editUserInfo(String name, String lastName, String phoneNumber){
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        String userID=autentication.getCurrentUser().getUid().toString();
        database.collection("UserInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            String idSearch;
                            for(QueryDocumentSnapshot document : task.getResult()){
                                idSearch = document.getData().get("ID").toString();
                                if(idSearch.equals(userID)){
                                    final DocumentReference docRef = database.collection("UserInfo").document(document.getId().toString());
                                    database.runTransaction(new Transaction.Function<Void>() {
                                        @Nullable
                                        @Override
                                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                            transaction.update(docRef, "LastName", lastName, "Name", name, "PhoneNumber", phoneNumber);
                                            return null;
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
    }
    private boolean validateField(String name,String lastName){

        if(name.isEmpty() || lastName.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el nombre o apellido del usuario", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

}