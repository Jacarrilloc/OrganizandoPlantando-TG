package com.example.opcv.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.gardens.CreateGardenActivity;
import com.example.opcv.gardens.GardenEditActivity;
import com.example.opcv.info.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class EditUserActivity extends AppCompatActivity {
    private Button signOff, delete;
    private Button gardensMap, profile, myGardens, acceptChanges, changePhoto;
    private TextView userNameTV, close, deleteP;
    private EditText userName, userLastName, userEmail, userPhone;
    private ImageView profilePhoto;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;
    private User userActive;
    private String userID_Recived;
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private Boolean IsChangedPhoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        userID_Recived = getIntent().getStringExtra("userInfo");

        if (userID_Recived == null){
            AuthUtilities info = new AuthUtilities();
            userID_Recived = info.getCurrentUserUid();
        }
        changePhoto = findViewById(R.id.ChangeImageEditUser);
        profilePhoto = findViewById(R.id.userImageEditUserActivity);
        userNameTV = (TextView) findViewById(R.id.userName);
        userName =(EditText) findViewById(R.id.userName2);
        userLastName = (EditText) findViewById(R.id.lastNameInfo);
        userEmail = (EditText) findViewById(R.id.gardenName);
        userPhone = (EditText) findViewById(R.id.address);

        signOff = (Button) findViewById(R.id.options);
        delete = (Button) findViewById(R.id.options3);
        close = (TextView) findViewById(R.id.options2);
        deleteP = (TextView) findViewById(R.id.options4);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        gardensMap = (Button) findViewById(R.id.gardens);
        acceptChanges = (Button) findViewById(R.id.editUser);

        searchUserInfo();

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditUserActivity.this)
                        .setMessage("¿Deseas Tomar una foto o elegir desde la galeria?")
                        .setNegativeButton("Tomar Foto", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                takePhoto();
                            }
                        })
                        .setPositiveButton("Seleccionar desde la Galeria", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                selectInGalery();
                            }
                        })
                        .show();
            }
        });

        signOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, SignOffActivity.class));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, DeleteAccountActivity.class));
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, SignOffActivity.class));
            }
        });
        deleteP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, DeleteAccountActivity.class));
            }
        });


        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, HomeActivity.class));
            }
        });

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, MapsActivity.class));
            }
        });

        acceptChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, Lastname, Name, PhoneNumber;
                //email=userEmail.getText().toString();
                Lastname = userLastName.getText().toString();
                Name=userName.getText().toString();
                PhoneNumber=userPhone.getText().toString();
                editUserInfo(Name, Lastname, PhoneNumber);
                if(validateField(Name, Lastname)){
                    editUserInfo(Name, Lastname, PhoneNumber);
                    //Intent start = new Intent(EditUserActivity.this,HomeActivity.class);
                    //startActivity(start);
                    Toast.makeText(EditUserActivity.this, "Se guardarón los cambios con exito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchUserInfo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("UserInfo");
        Query query = collectionRef.whereEqualTo("ID", userID_Recived);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getData().get("Name").toString();
                        String email = document.getData().get("Email").toString();
                        String lastname = document.getData().get("LastName").toString();
                        String phoneNumber = document.getData().get("PhoneNumber").toString();
                        userActive =  new User(name, lastname, email, userID_Recived, phoneNumber,null,null);
                        userNameTV.setText(userActive.getName());
                        userName.setText(userActive.getName());
                        userLastName.setText(userActive.getLastName());
                        userEmail.setText("Comabaquinta");
                        userPhone.setText(userActive.getPhoneNumber());
                        getPhotoProfileUser(userActive.getId());
                    }
                }
            }
        });
    }


    private void getPhotoProfileUser(String id){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String pathImage = "userProfilePhoto/" + id + ".jpg";
        StorageReference storageRef = storage.getReference().child(pathImage);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(profilePhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                profilePhoto.setImageResource(R.drawable.im_logo_ceres_green);
            }
        });

    }
    /*private void searchUserInfo (){
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        String userID=autentication.getCurrentUser().getUid().toString();
        database.collection("UserInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            String idSearch;
                            for(QueryDocumentSnapshot document : task.getResult()){
                                idSearch = document.getData().get("ID").toString();
                                if(idSearch.equals(userID)){
                                    String name, email, lastname, phoneNumber;
                                    name=document.getData().get("Name").toString();
                                    email=document.getData().get("Email").toString();
                                    lastname=document.getData().get("LastName").toString();
                                    phoneNumber=document.getData().get("PhoneNumber").toString();
                                    userActive =  new User(name, email, userID, lastname, phoneNumber,null);
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
    }*/

    private User returnUser (User userP){
        return userP;
    }

    private void editUserInfo(String name, String lastName, String phoneNumber){
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        changePhoto();
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

    private void takePhoto(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                openCamaraAndTakePhoto();
                IsChangedPhoto = true;
            } else {
                Toast.makeText(this, "No hay una Camara en tu Dispositivo", Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void openCamaraAndTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    private void selectInGalery(){
        if(ContextCompat.checkSelfPermission(EditUserActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditUserActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_STORAGE);
        }else{
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            IsChangedPhoto = true;
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap photoI = (Bitmap) data.getExtras().get("data");
            profilePhoto.setImageBitmap(photoI);
        }
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE && data != null) {
            Uri imageUri = data.getData();
            profilePhoto.setImageURI(imageUri);
        }
    }

    private void changePhoto(){
        if(IsChangedPhoto) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("userProfilePhoto/" + userID_Recived + ".jpg");
            profilePhoto.setDrawingCacheEnabled(true);
            Bitmap bitmap = profilePhoto.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream stream = new ByteArrayInputStream(baos.toByteArray());
            UploadTask uploadTask = storageRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditUserActivity.this, "Se Cambio la Foto de Perfil Exitosamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // La carga de la foto ha fallado, manejar el error aquí
                }
            });
        }
    }
}