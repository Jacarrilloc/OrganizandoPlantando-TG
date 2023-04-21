package com.example.opcv.gardens;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.auth.SelectPhotoActivity;
import com.example.opcv.business.gardenController.GardenLogic;
import com.example.opcv.info.GardenInfo;
import com.example.opcv.ludificationScreens.DictionaryHome;
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
import java.util.HashMap;
import java.util.Map;

public class CreateGardenActivity extends AppCompatActivity {

    private EditText nameGarden,infoGarden;
    private ImageView photo;
    private Button selectPhoto;
    private FirebaseAuth autentication;
    private FirebaseFirestore database;
    private Button create, otherGardensButton, profile, myGardens, ludification;
    private Switch gardenType;
    private GardenInfo newInfo;

    private FloatingActionButton backButtom;

    private static final int REQUEST_SELECT_PHOTO = 2000;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;

    private static final int GALLERY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_garden);

        nameGarden = findViewById(R.id.gardenName);
        infoGarden = findViewById(R.id.gardenInfo);
        gardenType = findViewById(R.id.switchGardenType);
        photo = findViewById(R.id.imageGardenCreate);
        selectPhoto = findViewById(R.id.SelectImageCreateGarden);

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        create = findViewById(R.id.add_garden_button);

        otherGardensButton = (Button) findViewById(R.id.gardens);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CreateGardenActivity.this)
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

        otherGardensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGardenActivity.this, MapsActivity.class));
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
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(CreateGardenActivity.this, DictionaryHome.class);
                startActivity(edit);
            }
        });
    }

    private void createGarden(){
        String name = nameGarden.getText().toString();
        String info = infoGarden.getText().toString();
        Boolean gardenPrivateOrPublic = gardenType.isChecked();
        GardenLogic newGarden = new GardenLogic(this.getApplication());
        newGarden.createGarden(name,info,gardenPrivateOrPublic);
        Toast.makeText(CreateGardenActivity.this, "Se Creó exitosamente la Huerta", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CreateGardenActivity.this, HomeActivity.class));
    }
    /*
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

            collectionRef.add(gardenInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String idGarden = documentReference.getId();
                    uploadPhotoGarden(idGarden);
                    Toast.makeText(CreateGardenActivity.this, "Se Creó exitosamente la Huerta", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateGardenActivity.this, HomeActivity.class).putExtra("idGarden", documentReference.getId().toString()));
                }
            });
        }
    }
*/
    private void takePhoto(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                openCamaraAndTakePhoto();
            } else {
                Toast.makeText(this, "No hay una Camara en tu Dispositivo", Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void selectInGalery(){
        if(ContextCompat.checkSelfPermission(CreateGardenActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateGardenActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_STORAGE);
        }else{
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (resultCode == RESULT_OK) {
                Bitmap photoI = (Bitmap) data.getExtras().get("data");
                photo.setImageBitmap(photoI);
            }
            if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE && data != null) {
                Uri imageUri = data.getData();
                photo.setImageURI(imageUri);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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