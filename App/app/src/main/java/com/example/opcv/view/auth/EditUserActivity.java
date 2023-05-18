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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.model.persistance.firebase.UserCommunication;
import com.example.opcv.view.ludification.RewardHomeActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class EditUserActivity extends AppCompatActivity {
    private Button signOff, delete, rewards, profile, myGardens, acceptChanges, changePhoto, ludification, changePasswordIcon;
    private TextView userNameTV, close, deleteP,levelInfo, changePasswordText;
    private EditText userName, userLastName, userEmail, userPhone;
    private ImageView profilePhoto, borderImage;
    private String userID_Recived;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private Uri uriCamera;
    private Boolean IsChangedPhoto = false, imageSelected = false;

    @Override
    protected void onStart() {
        super.onStart();
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
        rewards = (Button) findViewById(R.id.rewards);
        acceptChanges = (Button) findViewById(R.id.editUser);
        borderImage = (ImageView) findViewById(R.id.imageLevel);
        changePasswordIcon = (Button) findViewById(R.id.changePassword);
        changePasswordText = (TextView) findViewById(R.id.changePasswordText);
        UserCommunication persistance = new UserCommunication();

        profile.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));

        persistance.searchUserInfo(userID_Recived,userNameTV, userName, userLastName, userEmail, userPhone);
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
                Intent start = new Intent(EditUserActivity.this, HomeActivity.class);
                start.putExtra("userID", userID_Recived);
                startActivity(start);
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, RewardHomeActivity.class));
            }
        });

        changePasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, ChangePassword.class));
            }
        });

        changePasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, ChangePassword.class));
            }
        });

        acceptChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Lastname, Name, PhoneNumber;
                Lastname = userLastName.getText().toString();
                Name = userName.getText().toString();
                PhoneNumber = userPhone.getText().toString();
                if(validateField(Name, Lastname)){
                    persistance.editUserInfo(Name, Lastname, PhoneNumber, userID_Recived, IsChangedPhoto, profilePhoto, EditUserActivity.this);
                    Toast.makeText(EditUserActivity.this, "Se guardarón los cambios con exito", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(EditUserActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(EditUserActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
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