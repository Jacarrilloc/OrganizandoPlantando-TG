package com.example.opcv.view.gardens;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.business.ludification.Level;
import com.example.opcv.model.persistance.firebase.UserCommunication;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.model.entity.GardenInfo;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.model.persistance.garden.GardenPersistance;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CreateGardenActivity extends AppCompatActivity {

    private EditText nameGarden,infoGarden;
    private TextView banner;
    private String idUser;
    private ImageView photo;
    private Button selectPhoto;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;
    private Button create, rewards, profile, myGardens, ludification;
    private Switch gardenType;
    private GardenInfo newInfo;

    private FloatingActionButton backButtom;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private Uri uriCamera;

    private Boolean IsChangedPhoto = false, isLevelTwo;
    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_garden);

        nameGarden = findViewById(R.id.gardenName);
        infoGarden = findViewById(R.id.gardenInfo);
        gardenType = findViewById(R.id.switchGardenType);
        photo = findViewById(R.id.imageGardenCreate);
        selectPhoto = findViewById(R.id.SelectImageCreateGarden);
        banner = (TextView) findViewById(R.id.alert);
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        create = findViewById(R.id.add_garden_button);
        rewards = (Button) findViewById(R.id.rewards);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        ludification = (Button) findViewById(R.id.ludification);
        backButtom = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);
        idUser = autentication.getCurrentUser().getUid().toString();
        UserCommunication com = new UserCommunication();
        Level levelLogic = new Level();
        com.getUserLevel(idUser, new UserCommunication.GetUserLvl() {
            @Override
            public void onComplete(String lvl) {
                isLevelTwo = levelLogic.levelTwoReward(lvl);
            }
        });


        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLevelTwo){
                    new AlertDialog.Builder(CreateGardenActivity.this)
                            .setMessage("¿Deseas Tomar una foto o elegir desde la galeria?")
                            .setNegativeButton("Tomar Foto", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    takePhoto();
                                }
                            })
                            .setPositiveButton("Seleccionar desde la Galeria", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    selectPhotoGallery();
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

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGardenActivity.this, RewardHomeActivity.class));
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGardenActivity.this, EditUserActivity.class));
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGardenActivity.this, HomeActivity.class));
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGarden();
                //startActivity(new Intent(CreateGardenActivity.this, GardenAddressActivity.class));
            }
        });


        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(CreateGardenActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(CreateGardenActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void createGarden(){
        String name = nameGarden.getText().toString();
        String info = infoGarden.getText().toString();
        Boolean gardenPrivateOrPublic = gardenType.isChecked();
        if(validateField(name,info)){
            FirebaseUser user = autentication.getCurrentUser();
            CollectionReference collectionRef = database.collection("Gardens");

            if(!gardenPrivateOrPublic){
                newInfo = new GardenInfo(user.getUid(),nameGarden.getText().toString(),infoGarden.getText().toString(),"Public");
            }
            if(gardenPrivateOrPublic){
                newInfo = new GardenInfo(user.getUid(),nameGarden.getText().toString(),infoGarden.getText().toString(),"Private");
            }


            Map<String, Object> gardenInfo = new HashMap<>();
            gardenInfo.put("ID_Owner",newInfo.getID_Owner());
            gardenInfo.put("GardenName",newInfo.getName());
            gardenInfo.put("InfoGarden",newInfo.getInfo());
            gardenInfo.put("GardenType", newInfo.getGardenType());
            gardenInfo.put("UriPath", null);

            collectionRef.add(gardenInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String idGarden = documentReference.getId();
                    GardenPersistance persistance = new GardenPersistance();
                    Drawable drawable = photo.getDrawable();
                    if(drawable != null){
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        bytes = stream.toByteArray();
                        startActivity(new Intent(CreateGardenActivity.this, GardenAddressActivity.class).putExtra("idGarden", documentReference.getId().toString()));
                    }else{
                        bytes = null;
                        startActivity(new Intent(CreateGardenActivity.this, GardenAddressActivity.class).putExtra("idGarden", documentReference.getId().toString()));
                    }
                    persistance.addGardenPhoto(bytes, idGarden, new GardenPersistance.GetUriGarden() {
                        @Override
                        public void onSuccess(String uri) {
                            //descomentar la siguiente linea si se necesita poner la uri en firestore
                            collectionRef.document(idGarden).update("UriPath", uri);
                            startActivity(new Intent(CreateGardenActivity.this, GardenAddressActivity.class).putExtra("idGarden", documentReference.getId().toString()));
                        }
                    });

                }
            });
        }
    }

    private boolean validateField(String name,String info){

        if(name.isEmpty() || info.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el nombre y la información de la Huerta", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
        photo.setImageURI(null);
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
                        photo.setImageURI(uriCamera);
                    }
                }
            });

    private void selectPhotoGallery(){
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        startActivityForResult(pickImage,PICK_IMAGE_REQUEST);
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
                        photo.setImageBitmap(selectedImage);
                        IsChangedPhoto = true;
                    }catch(FileNotFoundException e){
                        Log.i("Galery","ERROR:"+e.toString());
                    }
                }
        }
    }

    private void openCamaraAndTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    private void uploadPhotoGarden(String idGarden){
        try{
            Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
            if(bitmap != null){
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference gardenRef = storageRef.child("gardenMainPhoto");
                String imageName = idGarden + ".jpg";
                StorageReference imageRef = gardenRef.child(imageName);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = imageRef.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i("IMG","Image uploaded to Storage");
                    }
                });
            }
        }catch (Exception e){
            Log.d(TAG, "Error con la foto: ", e);
        }
    }

    public interface GetGardenUri{
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}