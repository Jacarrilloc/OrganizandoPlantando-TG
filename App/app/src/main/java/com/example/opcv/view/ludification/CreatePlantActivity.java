package com.example.opcv.view.ludification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.opcv.view.gardens.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.business.ludification.Ludification;
import com.example.opcv.business.ludification.Level;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;

public class CreatePlantActivity extends AppCompatActivity {

    private CheckBox flower, edible, fruit, medicinal, pet, precaution;
    private EditText name, descr;
    private String plantName, plantDescription, idUser;
    private Boolean flowerCheck, edibleCheck, fruitCheck, medicineCheck, petCheck, precautionCheck;
    private FloatingActionButton add;
    private Button profile, myGardens, gardensMap, ludification;
    private ImageView image;
    private static final int REQUEST_CODE_SELECT_IMAGE = 100;
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
        gardensMap = (Button) findViewById(R.id.gardens);
        ludification = (Button) findViewById(R.id.ludification);
        add = (FloatingActionButton) findViewById(R.id.addButton);
        image = (ImageView) findViewById(R.id.addImage);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idUser = extras.getString("userInfo");
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setBackground(null);
                image.setBackgroundColor(getResources().getColor(R.color.lightGreen2));
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
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
                Intent edit = new Intent(CreatePlantActivity.this, DictionaryHomeActivity.class);
                startActivity(edit);
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

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreatePlantActivity.this, MapsActivity.class));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() !=null){
                Uri selectedImage = data.getData();
                // image.setImageURI(null);
                image.setImageURI(selectedImage);

                imageSelected = true;
            }
            if(imageSelected){
                image.setDrawingCacheEnabled(true);
                image.buildDrawingCache();
                Bitmap bitmap = image.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(bitmap != null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    bytes = baos.toByteArray();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}