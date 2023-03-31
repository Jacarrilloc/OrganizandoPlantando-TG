package com.example.opcv.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.info.User;
import com.example.opcv.localDatabase.DB_User;
import com.example.opcv.localDatabase.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_SELECT_PHOTO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_SELECT_PHOTO);
            } else {
                Toast.makeText(SelectPhotoActivity.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createUserInDatabase(){
        if(authUtilities.createUser(newUserInfo.getEmail(),password,newUserInfo,imageUri,SelectPhotoActivity.this)){
            Toast.makeText(this, "Usuario Creado Exitosamente", Toast.LENGTH_SHORT).show();
        }
        addToSQL(newUserInfo);
    }

    private void takePhotoUser(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageSource.setImageURI(imageUri);
        }
        if (requestCode == REQUEST_SELECT_PHOTO && resultCode == RESULT_OK) {
            imageUri = data.getData();
            ImageSource.setImageURI(imageUri);
        }
    }

    private void addToSQL(User newUserInfo){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if(db != null){
            Toast.makeText(this, "Se crea la base de Datos", Toast.LENGTH_SHORT).show();
            Map<String,Object> info = newUserInfo.toMap();
            DB_User newI = new DB_User(this);
            long i = newI.insertUserInfo(info);
            if(i > 0){
                Toast.makeText(this, "REGISTRO GUARDADO", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
        callHome(newUserInfo);
    }
    private void callHome(User newUserInfo){
        Intent intent = new Intent(SelectPhotoActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("userID", authUtilities.getCurrentUserUid());
        startActivity(intent);
    }
}