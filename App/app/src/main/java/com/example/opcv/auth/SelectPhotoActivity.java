package com.example.opcv.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.info.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SelectPhotoActivity extends AppCompatActivity {

    private Button takePhoto,SelectPhoto,finishRegisterInfo;
    private ImageView ImageSource;
    private AuthUtilities authUtilities;
    private User newUserInfo;
    private Uri imageUri;
    private String currentPhotoPath;
    private FloatingActionButton backButtom;
    private String password;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int REQUEST_SELECT_PHOTO = 2000;
    private static final int GALLERY_REQUEST_CODE = 100;
    private Boolean IsChangedPhoto = false;
    private byte[] bytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        takePhoto = findViewById(R.id.takePhotoButtom);
        SelectPhoto = findViewById(R.id.SelectPhotoButtom);
        finishRegisterInfo = findViewById(R.id.finishRegisterInfo);
        ImageSource = findViewById(R.id.profileImageSelected);
        backButtom = findViewById(R.id.returnArrowButtonSeleectPhoto);

        Intent intent = getIntent();
        newUserInfo = (User) intent.getSerializableExtra("newUserInfo");
        password = intent.getStringExtra("password");

        authUtilities = new AuthUtilities();

        finishRegisterInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserInDatabase();
                callHome();
            }
        });

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoUser();
            }
        });

        SelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotoUser();
            }
        });
    }

    private void selectPhotoUser(){
        if(ContextCompat.checkSelfPermission(SelectPhotoActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SelectPhotoActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_STORAGE);
        }else{
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            IsChangedPhoto = true;
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            } else {
                Toast.makeText(SelectPhotoActivity.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createUserInDatabase(){
        if(validateField(this, bytes)){
        if(bytes == null){
            int drawableId = R.drawable.im_logo_ceres_green;

            //Drawable drawable = getResources().getDrawable(R.drawable.im_logo_ceres);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            bytes = stream.toByteArray();
        }
            if(authUtilities.createUser(newUserInfo.getEmail(),password,newUserInfo,bytes,SelectPhotoActivity.this)){

            }
        }
    }

    public boolean validateField(Context context, byte[] bytes){
        if(bytes == null){
            Toast.makeText(context, "Es necesario Ingresar una imagen", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void takePhotoUser(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                PackageManager pm = getPackageManager();
                if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    openCamaraAndTakePhoto();
                    IsChangedPhoto = true;
                } else {
                    Toast.makeText(this, "No hay una Camara en tu Dispositivo", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

    }
    private void openCamaraAndTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }

            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this,
                        "com.example.opcv.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == 0 && resultCode == RESULT_OK){
                Bitmap photoI = (Bitmap) data.getExtras().get("data");
                ImageSource.setImageBitmap(photoI);
                ImageSource.setDrawingCacheEnabled(true);
                ImageSource.buildDrawingCache();
                Bitmap bitmap = ImageSource.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(bitmap != null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    bytes = baos.toByteArray();
                }
            }
            if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() !=null){
                Uri selectedImage = data.getData();
                // image.setImageURI(null);
                ImageSource.setImageURI(selectedImage);

                IsChangedPhoto = true;
                if(IsChangedPhoto){
                    ImageSource.setDrawingCacheEnabled(true);
                    ImageSource.buildDrawingCache();
                    Bitmap bitmap = ImageSource.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if(bitmap != null){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        bytes = baos.toByteArray();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void callHome(){
        Intent intent = new Intent(SelectPhotoActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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