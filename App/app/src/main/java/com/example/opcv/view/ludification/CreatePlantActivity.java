package com.example.opcv.view.ludification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.view.gardens.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.business.ludification.Ludification;
import com.example.opcv.business.ludification.Level;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreatePlantActivity extends AppCompatActivity {

    private CheckBox flower, edible, fruit, medicinal, pet, precaution;
    private EditText name, descr;
    private String plantName, plantDescription, idUser;
    private Boolean flowerCheck, edibleCheck, fruitCheck, medicineCheck, petCheck, precautionCheck;
    private FloatingActionButton add, back;
    private Button profile, myGardens, rewards, ludification;
    private ImageView image;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectImageUri;
    private byte[] bytes;
    private boolean imageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plant);

        name = (EditText) findViewById(R.id.namePlant);
        descr = (EditText) findViewById(R.id.description);
        flower = (CheckBox) findViewById(R.id.option1);
        edible = (CheckBox) findViewById(R.id.option3);
        fruit = (CheckBox) findViewById(R.id.option5);
        medicinal = (CheckBox) findViewById(R.id.option2);
        pet = (CheckBox) findViewById(R.id.option4);
        precaution = (CheckBox) findViewById(R.id.option6);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        add = (FloatingActionButton) findViewById(R.id.addButton);
        image = (ImageView) findViewById(R.id.addImage);
        back = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idUser = extras.getString("userInfo");
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ludification logic = new Ludification();
                Level level = new Level();
                plantName = name.getText().toString();
                plantDescription = descr.getText().toString();
                flowerCheck = flower.isChecked();
                edibleCheck = edible.isChecked();
                fruitCheck = fruit.isChecked();
                medicineCheck = medicinal.isChecked();
                petCheck = pet.isChecked();
                precautionCheck = precaution.isChecked();
                Drawable drawable = image.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                bytes = stream.toByteArray();
                if(logic.validateField(plantName, plantDescription, CreatePlantActivity.this, bytes)){
                    logic.addPlantElementsMap(plantName, plantDescription, flowerCheck, fruitCheck, edibleCheck, medicineCheck, petCheck, precautionCheck, CreatePlantActivity.this, idUser, bytes);
                    level.addLevel(idUser, true, CreatePlantActivity.this, "Plants");
                    //Notifications  notifications = new Notifications();
                    //notifications.notification("Has ganado puntos", "Felicidades! Ganaste 7 puntos por crear tu planta", CreatePlantActivity.this, DictionaryHome.class);
                    Intent edit = new Intent(CreatePlantActivity.this, DictionaryHomeActivity.class);
                    edit.putExtra("userInfo", idUser);
                    startActivity(edit);
                }
            }
        });
        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(CreatePlantActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(CreatePlantActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreatePlantActivity.this, GardensAvailableActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(CreatePlantActivity.this, EditUserActivity.class);
                AuthCommunication auth = new AuthCommunication();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreatePlantActivity.this, RewardHomeActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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

    private void selectPhoto(){
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
                        image.setImageBitmap(selectedImage);
                        imageSelected = true;
                    }catch(FileNotFoundException e){
                        Log.i("Galery","ERROR:"+e.toString());
                    }
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}