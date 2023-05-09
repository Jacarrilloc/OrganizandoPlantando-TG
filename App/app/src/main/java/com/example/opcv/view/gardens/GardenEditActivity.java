package com.example.opcv.view.gardens;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.opcv.business.ludification.Level;
import com.example.opcv.model.persistance.firebase.UserCommunication;
import com.example.opcv.model.persistance.garden.GardenPersistance;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class GardenEditActivity extends AppCompatActivity {
    private EditText gardenName, comunity, description;
    private Button acceptChanges, rewards, myGardens, profile, deleteGarden, addForm, changeImage, ludification;
    private ImageView addParticipants,gardenImage;
    private Switch switchGardenTypeModified;
    private CheckBox publicGarden, privateGarden;
    private FloatingActionButton backButtom;
    private FirebaseAuth autentication;
    private FirebaseFirestore database, database2;
    private FirebaseUser userLog;

    private Boolean IsChangedPhoto = false, isLevelTwo;
    private TextView adminMembersGarden, banner;

    private CollectionReference gardensRef;
    private String idUser, idGarden, nameGarden, infoGarden, name, id;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private Uri uriCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_edit);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idGarden = extras.getString("idGarden");
        }

        gardenImage = findViewById(R.id.gardenImageEditActivity);
        changeImage = findViewById(R.id.ChangeImageEditGarden);
        banner = (TextView) findViewById(R.id.alert);
        switchGardenTypeModified = findViewById(R.id.switchGardenTypeModified);
        backButtom = findViewById(R.id.returnArrowButtomEditToGarden);
        adminMembersGarden = (TextView) findViewById(R.id.adminMembers);
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        gardensRef = database.collection("Gardens");
        gardenName = (EditText) findViewById(R.id.gardenName);
        description = (EditText) findViewById(R.id.gardenDescription);
        comunity = (EditText) findViewById(R.id.gardenInfo);
        rewards = (Button) findViewById(R.id.rewards);
        myGardens = (Button) findViewById(R.id.myGardens);
        acceptChanges = (Button) findViewById(R.id.acceptChanges);
        deleteGarden = (Button) findViewById(R.id.deleteGarden);
        addForm = (Button) findViewById(R.id.addForm);
        idUser = autentication.getCurrentUser().getUid().toString();
        ludification = (Button) findViewById(R.id.ludification);
        profile = (Button) findViewById(R.id.profile);
        addParticipants = (ImageView) findViewById(R.id.addPersons);

        IsChangedPhoto = false;
        UserCommunication com = new UserCommunication();
        Level levelLogic = new Level();
        com.getUserLevel(idUser, new UserCommunication.GetUserLvl() {
            @Override
            public void onComplete(String lvl) {
                isLevelTwo = levelLogic.levelTwoReward(lvl);

            }
        });
        getImageGarden(idGarden);


        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLevelTwo){
                    new AlertDialog.Builder(GardenEditActivity.this)
                            .setMessage("¿Deseas Tomar una foto o elegir desde la galeria?")
                            .setNegativeButton("Tomar Foto", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    takePhoto();
                                }
                            })
                            .setPositiveButton("Seleccionar desde la Galeria", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    selectPhoto();
                                }
                            })
                            .show();
                }
                else{
                    banner.setVisibility(View.VISIBLE);
                    AlphaAnimation animation = new AlphaAnimation(1f, 0f);
                    animation.setDuration(5000);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            banner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    banner.startAnimation(animation);
                }

            }
        });

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(GardenEditActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(GardenEditActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        adminMembersGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent requests = new Intent(GardenEditActivity.this, GardenMembersActivity.class);
                System.out.println("es id es : " + idGarden);
                requests.putExtra("idGarden", idGarden);
                startActivity(requests);
                finish();
            }
        });

        gardensRef.document(idGarden).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nameGarden = documentSnapshot.getString("GardenName");
                gardenName.setText(nameGarden);
                infoGarden = documentSnapshot.getString("InfoGarden");
                description.setText(infoGarden);
                String gardenInfoType = documentSnapshot.getString("GardenType");
                if(gardenInfoType.equals("Public")){
                    switchGardenTypeModified.setChecked(false);
                } else if (gardenInfoType.equals("Private")) {
                    switchGardenTypeModified.setChecked(true);
                }
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenEditActivity.this, RewardHomeActivity.class));
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenEditActivity.this, HomeActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenEditActivity.this, EditUserActivity.class));
            }
        });

        acceptChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editGardenInfo()){

                    try {
                        Thread.sleep(50);
                        Intent start = new Intent(GardenEditActivity.this,HomeActivity.class);
                        String id = autentication.getCurrentUser().getUid().toString();
                        //System.out.println("El id es:.  "+id);
                        start.putExtra("ID", id);
                        start.putExtra("idGarden", idGarden);
                        start.putExtra("gardenName", name);

                        startActivity(start);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        });

        deleteGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("¿Estás seguro de que deseas eliminar esta huerta?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteGarden(idGarden);
                                Intent exit = new Intent(GardenEditActivity.this, HomeActivity.class);
                                exit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(exit);
                            }
                        }).create().show();


            }
        });

        addForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoForms = new Intent(GardenEditActivity.this, GardenFormsActivity.class);
                String idGardenFirebase = extras.getString("idGarden");
                infoForms.putExtra("idGardenFirebaseDoc",idGardenFirebase);
                startActivity(infoForms);
            }
        });

        addParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void getImageGarden(String idGarden){
        //se supone que con esto no deberia dar StorageException, pero si :(
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference Ref = database.collection("Gardens").document(idGarden);
        Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String uri = task.getResult().getString("UriPath");
                    if(uri != null){
                        Glide.with(GardenEditActivity.this).load(uri).into(gardenImage);
                    }
                    else{
                        gardenImage.setImageResource(R.drawable.im_logo_ceres_green);
                    }

                }
            }
        });

        /*StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String imageName = idGarden + ".jpg";
        StorageReference imageRef = storageRef.child("gardenMainPhoto/" + imageName);
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                gardenImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                gardenImage.setImageResource(R.drawable.im_logo_ceres_green);
            }
        });*/
    }

    private void deleteGarden(String idGarden) {
        GardenPersistance persistance = new GardenPersistance();
        persistance.deletePhotoGarden(idGarden);
        database2 = FirebaseFirestore.getInstance();
        database2.collection("Gardens").document(idGarden).collection("Requests")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                database2.collection("Gardens")
                                        .document(idGarden)
                                        .collection("Requests")
                                        .document(document.getId())
                                        .delete();
                            }
                        }
                    }
                });
        database2.collection("Gardens")
                .document(idGarden)
                .collection("Forms")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                database2.collection("Gardens")
                                        .document(idGarden)
                                        .collection("Forms")
                                        .document(document.getId())
                                        .delete();
                            }
                        }
                    }
                });
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

    private Boolean editGardenInfo(){
        name = gardenName.getText().toString();
        String info = description.getText().toString();
        Boolean gardenType = switchGardenTypeModified.isChecked();
        if(validateField(name, info)){
            FirebaseUser user = autentication.getCurrentUser();
            //CollectionReference collectionRef = database.collection("Gardens");
            if(IsChangedPhoto){
                changePhoto();
            }
            idUser = user.getUid();
            searchGarden(idUser, name, info, gardenType);
            Toast.makeText(GardenEditActivity.this, "Se modificó exitosamente tu huerta", Toast.LENGTH_SHORT).show();
            return true;

            //System.out.println("El id es: "+collectionRef.getPath().toString());
        }
        else{
            return false;
        }
    }

    private void searchGarden(String idUser, String name, String info, Boolean gardenType) {
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
                                            if(!gardenType){
                                                transaction.update(docRef, "GardenName", name, "InfoGarden", info, "GardenType", "Public");
                                            }
                                            if(gardenType){
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


    private boolean validateField(String name,String info){

        if(name.isEmpty() || info.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el nombre e información de la Huerta", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void selectPhoto(){
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
                        gardenImage.setImageBitmap(selectedImage);
                        IsChangedPhoto = true;
                    }catch(FileNotFoundException e){
                        Log.i("Galery","ERROR:"+e.toString());
                    }
                }
        }
    }

    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            launchCamera();
        }
    }

    private void launchCamera() {
        uriCamera = null;
        gardenImage.setImageURI(null);
        File file = new File(getFilesDir(), "picFromCamera");
        uriCamera = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
        mGetContentCamera.launch(uriCamera);
        IsChangedPhoto = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ActivityResultLauncher<Uri> mGetContentCamera = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result){
                    if(result){
                        gardenImage.setImageURI(uriCamera);
                    }
                }
            });

    private void changePhoto(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("gardenMainPhoto/" + idGarden + ".jpg");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        final DocumentReference Ref = database.collection("Gardens").document(idGarden);

        gardenImage.setDrawingCacheEnabled(true);
        Bitmap bitmap = gardenImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream stream = new ByteArrayInputStream(baos.toByteArray());
        UploadTask uploadTask = storageRef.putStream(stream);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //taskSnapshot.getUploadSessionUri()
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Ref.update("UriPath", uri);
                        Toast.makeText(GardenEditActivity.this, "Se Cambio la Imagen de la Huerta Exitosamente", Toast.LENGTH_SHORT).show();
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
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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