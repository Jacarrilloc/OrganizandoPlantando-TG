package com.example.opcv.ludificationScreens;

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

import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.business.ludificationLogic.LudificationLogic;
import com.example.opcv.business.ludificationLogic.levelLogic;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.gardens.GardensAvailableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;

public class CreateToolActivity extends AppCompatActivity {

    private CheckBox tool, fertilizer, care;
    private EditText name, descr;
    private String toolName, toolDescription, idUser;
    private Boolean toolCheck, fertilizerCheck, careCheck;
    private FloatingActionButton add;
    private Button profile, myGardens, gardensMap, ludification;
    private ImageView image;
    private static final int REQUEST_CODE_SELECT_IMAGE = 100;
    private Uri selectImageUri;
    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tool);

        name = (EditText) findViewById(R.id.nameTool);
        descr = (EditText) findViewById(R.id.description);
        tool = (CheckBox) findViewById(R.id.toolOption);
        fertilizer = (CheckBox) findViewById(R.id.fertilizerOption);
        care = (CheckBox) findViewById(R.id.careOption);
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LudificationLogic logic = new LudificationLogic();
                levelLogic level = new levelLogic();
                toolName = name.getText().toString();
                toolDescription = descr.getText().toString();
                toolCheck = tool.isChecked();
                fertilizerCheck = fertilizer.isChecked();
                careCheck = care.isChecked();
                if(logic.validateField(toolName, toolDescription, CreateToolActivity.this, bytes)){
                    logic.addToolElementsMap(toolName, toolDescription, toolCheck, fertilizerCheck, careCheck, CreateToolActivity.this, idUser, bytes);
                    level.addLevel(idUser, true, CreateToolActivity.this);
                    Intent edit = new Intent(CreateToolActivity.this, DictionaryHome.class);
                    edit.putExtra("userInfo", idUser);
                    startActivity(edit);
                }
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(CreateToolActivity.this, DictionaryHome.class);
                startActivity(edit);
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateToolActivity.this, GardensAvailableActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(CreateToolActivity.this, EditUserActivity.class);
                AuthUtilities auth = new AuthUtilities();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateToolActivity.this, MapsActivity.class));
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

        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() !=null){
            Uri selectedImage = data.getData();

            image.setImageURI(selectedImage);

            image.setDrawingCacheEnabled(true);
            image.buildDrawingCache();
            Bitmap bitmap = image.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if(bitmap != null){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                bytes = baos.toByteArray();
            }


        }
    }
}