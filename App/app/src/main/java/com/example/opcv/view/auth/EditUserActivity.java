package com.example.opcv.view.auth;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.view.gardens.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.model.entity.User;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.business.persistance.firebase.LudificationCommunication;
import com.example.opcv.business.persistance.firebase.UserCommunication;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class EditUserActivity extends AppCompatActivity {
    private Button signOff, delete;
    private Button gardensMap, profile, myGardens, acceptChanges, changePhoto, ludification;
    private TextView userNameTV, close, deleteP,levelInfo;
    private EditText userName, userLastName, userEmail, userPhone;
    private ImageView profilePhoto, borderImage;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;
    private User userActive;
    private String userID_Recived, photoUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private Uri uriCamera;
    private Boolean IsChangedPhoto = false, imageSelected = false;
    private File photoFile;

    @Override
    protected void onStart() {
        super.onStart();
        searchUserInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        userID_Recived = getIntent().getStringExtra("userInfo");

        if (userID_Recived == null){
            AuthCommunication info = new AuthCommunication();
            userID_Recived = info.getCurrentUserUid();
        }
        levelInfo = findViewById(R.id.levelUser);
        changePhoto = findViewById(R.id.ChangeImageEditUser);
        profilePhoto = findViewById(R.id.userImageEditUserActivity);
        userNameTV = (TextView) findViewById(R.id.userName);
        userName =(EditText) findViewById(R.id.userName2);
        userLastName = (EditText) findViewById(R.id.lastNameInfo);
        userEmail = (EditText) findViewById(R.id.gardenName);
        userPhone = (EditText) findViewById(R.id.address);
        ludification = (Button) findViewById(R.id.ludification);
        signOff = (Button) findViewById(R.id.options);
        delete = (Button) findViewById(R.id.options3);
        close = (TextView) findViewById(R.id.options2);
        deleteP = (TextView) findViewById(R.id.options4);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        gardensMap = (Button) findViewById(R.id.gardens);
        acceptChanges = (Button) findViewById(R.id.editUser);
        borderImage = (ImageView) findViewById(R.id.imageLevel);

        UserCommunication persistance = new UserCommunication();

        searchUserInfo();
        persistance.getProfilePicture(userID_Recived, new UserCommunication.GetUriUser() {
            @Override
            public void onComplete(String uri) {
                if(!Objects.equals(uri, null)){
                    Glide.with(EditUserActivity.this).load(uri).into(profilePhoto);
                }else{
                    profilePhoto.setImageResource(R.drawable.im_logo_ceres);
                }
            }
        });

        persistance.getUserLevel(userID_Recived, new UserCommunication.GetUserLvl() {
            @Override
            public void onComplete(String leveli) {

                double lvDouble = Double.parseDouble(leveli);
                int lv = Double.valueOf(lvDouble).intValue();

                ProgressBar progressBar = findViewById(R.id.progressBarUserLevel);
                progressBar.setMax(100);
                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, lv);
                animation.setDuration(2000);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();

                levelInfo.setText("Nivel " + String.valueOf(lv));
                if (lv >=0 && lv <10){
                    borderImage.setImageResource(R.drawable.im_level_1);
                }else if (lv>= 10 && lv <30) {
                    borderImage.setImageResource(R.drawable.im_level_2);
                } else if (lv>=30 && lv <60) {
                    borderImage.setImageResource(R.drawable.im_level_3);
                } else if (lv >= 60 && lv <100) {
                    borderImage.setImageResource(R.drawable.im_level_4);
                } else if (lv >= 100) {
                    borderImage.setImageResource(R.drawable.im_level_5);
                }

            }
        });



        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditUserActivity.this)
                        .setMessage("¿Deseas Tomar una foto o elegir desde la galeria?")
                        .setNegativeButton("Tomar Foto", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                takePhotoUser();
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
                Lastname = userLastName.getText().toString();
                Name = userName.getText().toString();
                PhoneNumber = userPhone.getText().toString();
                if(validateField(Name, Lastname)){
                    editUserInfo(Name, Lastname, PhoneNumber);
                    Toast.makeText(EditUserActivity.this, "Se guardarón los cambios con exito", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(EditUserActivity.this, DictionaryHomeActivity.class);
                startActivity(edit);
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

                        int level;
                        try {
                            level = (int) document.getData().get("Level");
                        }catch (Exception e){
                            level = 0;
                        }

                        userActive =  new User(name, lastname, email, userID_Recived, phoneNumber,null,null, level);
                        userNameTV.setText(userActive.getName());
                        userName.setText(userActive.getName());
                        userLastName.setText(userActive.getLastName());
                        userEmail.setText("Comabaquinta");
                        userPhone.setText(userActive.getPhoneNumber());
                    }
                }
            }
        });
    }
    
    private void editUserInfo(String name, String lastName, String phoneNumber){
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        changePhotoProfile(new GetImageUri() {
            @Override
            public void onSuccess(String uri) {

                String userID = autentication.getCurrentUser().getUid().toString();
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
                                                    transaction.update(docRef, "LastName", lastName, "Name", name, "PhoneNumber", phoneNumber, "UriPath", uri);
                                                    return null;
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
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

    private void selectInGalery(){
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        startActivityForResult(pickImage,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profilePhoto.setImageBitmap(selectedImage);
                        IsChangedPhoto = true;
                    }catch(FileNotFoundException e){
                        Log.i("Galery","ERROR:"+e.toString());
                    }
                }
        }
    }

    private void takePhotoUser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            launchCamera();
        }
    }

    private void launchCamera() {
        uriCamera = null;
        profilePhoto.setImageURI(null);
        File file = new File(getFilesDir(), "picFromCamera");
        uriCamera = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
        mGetContentCamera.launch(uriCamera);
    }

    ActivityResultLauncher<Uri> mGetContentCamera = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result){
                    if(result){
                        profilePhoto.setImageURI(uriCamera);
                        IsChangedPhoto = true;
                    }
                }
            });

    private void changePhotoProfile(final GetImageUri callback){
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
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            callback.onSuccess(url);
                            Toast.makeText(EditUserActivity.this, "Se Cambio la Foto de Perfil Exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // La carga de la foto ha fallado, manejar el error aquí
                }
            });
        }
    }

    public interface GetImageUri{
        void onSuccess(String uri);
    }
    public interface GetUriUser{
        void onSuccess(String uri);
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
}