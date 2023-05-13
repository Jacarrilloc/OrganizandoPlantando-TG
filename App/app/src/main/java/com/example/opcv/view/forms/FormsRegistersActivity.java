package com.example.opcv.view.forms;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FormsRegistersActivity extends AppCompatActivity {
    private TextView registerNameText;
    private ListView ListViewRegisters;
    private FloatingActionButton backButtom;
    private String register_name, idGarden;
    private ProgressBar infoWhile;
    private Forms infoForms;

    private MutableLiveData<List<ItemRegistersList>> infoFormsLiveData = new MutableLiveData<>();

    private boolean recreateCalled = false;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            fillFormsRegisters();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            fillFormsRegisters();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            fillFormsRegisters();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_registers);

        registerNameText = (TextView) findViewById(R.id.registerName);
        ListViewRegisters = (ListView) findViewById(R.id.ListViewRegisters);
        backButtom = findViewById(R.id.returnArrowButtonFormsToGarden);

        register_name = getIntent().getStringExtra("Name");
        idGarden = getIntent().getStringExtra("idGardenFirebase");
        registerNameText.setText(register_name);
        infoWhile = findViewById(R.id.progressBarForms);

        infoForms = new Forms(this);

        if(ContextCompat.checkSelfPermission(FormsRegistersActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            try {
                fillFormsRegisters();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

        infoFormsLiveData.observe(this, new Observer<List<ItemRegistersList>>() {
            @Override
            public void onChanged(List<ItemRegistersList> infoFormsResult) {
                // Aquí puedes actualizar la interfaz de usuario con la nueva lista de resultados
                fillListGardens(infoFormsResult);
                infoWhile.setVisibility(View.GONE);
            }
        });
    }

    ActivityResultLauncher<String> getStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result == true){
                try {
                    fillFormsRegisters();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                Toast.makeText(FormsRegistersActivity.this, "Es necesario acceder al alamcenamiento", Toast.LENGTH_SHORT).show();
            }
        }
    });

    // Método fillFormsRegisters actualizado
    private void fillFormsRegisters() throws JSONException, IOException {
        infoWhile.setVisibility(View.VISIBLE);
        Forms infoForms = new Forms(FormsRegistersActivity.this);
        List<ItemRegistersList> infoFormsResult = infoForms.getInfoForms(idGarden, register_name);

        if (infoFormsResult != null) {
            infoFormsLiveData.setValue(infoFormsResult);
            infoWhile.setVisibility(View.GONE);
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        fillFormsRegisters(); // Llamar de nuevo a fillFormsRegisters después del retraso
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 1000); // 1000 milisegundos = 1 segundo
        }
    }

    private void fillListGardens( List<ItemRegistersList> gardenInfoDocument){
        Log.i("size","tamaño de lista al llenar: " + gardenInfoDocument.size());
        FormsRegistersAdapter adapter = new FormsRegistersAdapter(this, gardenInfoDocument);
        ListViewRegisters.setAdapter(adapter);
        ListViewRegisters.setDividerHeight(5);
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