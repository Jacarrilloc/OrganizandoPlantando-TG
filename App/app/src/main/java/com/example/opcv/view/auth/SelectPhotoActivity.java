package com.example.opcv.view.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.model.entity.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectPhotoActivity extends AppCompatActivity {

    private Button takePhoto,SelectPhoto,finishRegisterInfo;
    private ImageView ImageSource;
    private AuthCommunication authUtilities;
    private User newUserInfo;
    private String currentPhotoPath;
    private FloatingActionButton backButtom;
    private String password;
    private static final int PICK_IMAGE_REQUEST = 1;

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

        authUtilities = new AuthCommunication();

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
                //takePhotoUser();
            }
        });

        SelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotoUser();
            }
        });
    }

    private void createUserInDatabase(){
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

    private void selectPhotoUser(){
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
                        ImageSource.setImageBitmap(selectedImage);
                    }catch(FileNotFoundException e){
                        Log.i("Galery","ERROR:"+e.toString());
                    }
                }
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