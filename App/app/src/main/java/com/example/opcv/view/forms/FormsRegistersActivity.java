package com.example.opcv.view.forms;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.business.forms.Forms;
import com.example.opcv.view.adapter.FormsRegistersAdapter;
import com.example.opcv.model.items.ItemRegistersList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormsRegistersActivity extends AppCompatActivity {
    private TextView registerNameText;
    private ListView ListViewRegisters;
    private FloatingActionButton backButtom;
    private String register_name, idGarden;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private Forms infoForms;

    @Override
    protected void onStart() {
        super.onStart();
        fillFormsRegisters();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fillFormsRegisters();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillFormsRegisters();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_registers);
        Toast.makeText(this, "FormsRegisterActivity", Toast.LENGTH_SHORT).show();

        registerNameText = (TextView) findViewById(R.id.registerName);
        ListViewRegisters = (ListView) findViewById(R.id.ListViewRegisters);
        backButtom = findViewById(R.id.returnArrowButtonFormsToGarden);

        register_name = getIntent().getStringExtra("Name");
        idGarden = getIntent().getStringExtra("idGardenFirebase");
        registerNameText.setText(register_name);

        infoForms = new Forms(this);

        Toast.makeText(this, "Name: "+ register_name, Toast.LENGTH_SHORT).show();

        if(ContextCompat.checkSelfPermission(FormsRegistersActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            fillFormsRegisters();
        }else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this, "El permiso es necesario para guardar los formularios", Toast.LENGTH_SHORT).show();
            getStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else{
            getStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    ActivityResultLauncher<String> getStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result == true){
                fillFormsRegisters();
            }else{
                Toast.makeText(FormsRegistersActivity.this, "Es necesario acceder al alamcenamiento", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private void fillFormsRegisters() {
        long startTime = System.currentTimeMillis();
        showLoadingScreen();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Forms infoForms = new Forms(FormsRegistersActivity.this);
                List<ItemRegistersList> infoFormsResult = null;
                try {
                    infoFormsResult = infoForms.getInfoForms(idGarden,register_name);
                    Log.i("size","Info Traida: " + infoFormsResult.size());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<ItemRegistersList> finalInfoFormsResult = infoFormsResult;
                final boolean[] isCodeBlockExecuted = {false};

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isCodeBlockExecuted[0]) {
                            Log.i("size","Info enviada LLenar: " + finalInfoFormsResult.size());
                            fillListGardens(finalInfoFormsResult);
                            hideLoadingScreen();
                            long endTime = System.currentTimeMillis();
                            long duration = endTime - startTime;
                            System.out.println("El bloque de código tardó " + duration + " milisegundos en ejecutarse.");
                            isCodeBlockExecuted[0] = true;
                        }
                    }
                });
            }
        }).start();
    }


    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando datos...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void fillListGardens( List<ItemRegistersList> gardenInfoDocument){
        Log.i("size","tamaño de lista al llenar: " + gardenInfoDocument.size());
        FormsRegistersAdapter adapter = new FormsRegistersAdapter(this, gardenInfoDocument);
        ListViewRegisters.setAdapter(adapter);
        ListViewRegisters.setDividerHeight(5);
    }

    private void showLoadingScreen() {
        // Mostrar la pantalla de carga, por ejemplo:
        ProgressBar progressBar = findViewById(R.id.progressBarForms);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingScreen() {
        // Ocultar la pantalla de carga, por ejemplo:
        ProgressBar progressBar = findViewById(R.id.progressBarForms);
        progressBar.setVisibility(View.GONE);
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