package com.example.opcv.view.base;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.example.opcv.R;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.model.persistance.firebase.GardenCommunication;
import com.example.opcv.view.adapter.GardenListAdapter;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.auth.SignOffActivity;
import com.example.opcv.view.gardens.CollaboratorGardensActivity;
import com.example.opcv.view.gardens.CreateGardenActivity;
import com.example.opcv.view.gardens.GardenActivity;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.example.opcv.model.items.ItemGardenHomeList;
import com.example.opcv.view.gardens.GenerateReportsActivity;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Button otherGardensButton, profile, myGardens, collaboration, ludification, rewards;
    private ImageButton generateReport;
    private ListView listAviableGardensInfo;
    private FloatingActionButton addButton;
    private Animation animSlideUp;
    private ProgressDialog progressDialog;
    private String userId;

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
    protected void onStart() {
        super.onStart();
        AuthCommunication auth = new AuthCommunication();
        GardenCommunication com = new GardenCommunication();
        com.fillGardenUser(auth.getCurrentUserUid(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AuthCommunication auth = new AuthCommunication();
        GardenCommunication com = new GardenCommunication();
        com.fillGardenUser(auth.getCurrentUserUid(), this);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("¿Estás seguro de que quieres salir de Ceres?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finishAffinity();
                        HomeActivity.super.onBackPressed();
                    }
                }).create().show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Declaracion metodos de navegacion
        listAviableGardensInfo = findViewById(R.id.listAviableGardens);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        collaboration = (Button) findViewById(R.id.collaboratorGardens);
        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        otherGardensButton = (Button) findViewById(R.id.otherGardensButton);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        generateReport = (ImageButton) findViewById(R.id.generalReport);
        ludification = (Button) findViewById(R.id.ludification);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String previous = getIntent().getStringExtra("previusScreen");
            if(previous != null){
                if(previous.equals("true")){
                    showProgressDialog(3000);
                }
            }
        }

        userId = getIntent().getStringExtra("userID");


        if (userId == null){
            AuthCommunication auth = new AuthCommunication();
            userId = auth.getCurrentUserUid();
        }

        //fillGardenUser();
        AuthCommunication auth = new AuthCommunication();
        GardenCommunication com = new GardenCommunication();
        com.fillGardenUser(auth.getCurrentUserUid(), this);

        listAviableGardensInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Object selectedItem = adapterView.getItemAtPosition(i);
                String itemName = ((ItemGardenHomeList) selectedItem).getName();
                String userID = userId;
                String idGarden = ((ItemGardenHomeList) selectedItem).getIdGarden();
                Intent start = new Intent(HomeActivity.this, GardenActivity.class);
                start.putExtra("ID",userID);
                start.putExtra("gardenName",itemName);
                start.putExtra("idGarden", idGarden);
                start.putExtra("idGardenFirebaseDoc",idGarden);
                start.putExtra("owner", "true");
                startActivity(start);
                finish();
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser us = FirebaseAuth.getInstance().getCurrentUser();
                if(us != null && !us.isAnonymous()){
                    startActivity(new Intent(HomeActivity.this, CreateGardenActivity.class));
                }
                else{
                    Toast.makeText(HomeActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        otherGardensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, GardensAvailableActivity.class));
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(HomeActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(HomeActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myGardens.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser us = FirebaseAuth.getInstance().getCurrentUser();
                if(us != null && !us.isAnonymous()){
                    if(userId != null) {
                        Intent edit = new Intent(HomeActivity.this, EditUserActivity.class);
                        edit.putExtra("userInfo", userId);
                        startActivity(edit);
                    }
                    else{
                        AuthCommunication auth = new AuthCommunication();
                        userId = auth.getCurrentUserUid();
                        Intent edit = new Intent(HomeActivity.this, EditUserActivity.class);
                        edit.putExtra("userInfo", userId);
                        startActivity(edit);
                    }
                }
                else{
                    startActivity(new Intent(HomeActivity.this, SignOffActivity.class));
                }

            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser us = FirebaseAuth.getInstance().getCurrentUser();
                if(us != null && !us.isAnonymous()){
                    startActivity(new Intent(HomeActivity.this, RewardHomeActivity.class));
                }
                else{
                    Toast.makeText(HomeActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        collaboration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser us = FirebaseAuth.getInstance().getCurrentUser();
                if(us != null && !us.isAnonymous()){
                    Intent edit = new Intent(HomeActivity.this, CollaboratorGardensActivity.class);
                    edit.putExtra("userID", userId);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(HomeActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser us = FirebaseAuth.getInstance().getCurrentUser();
                if(us != null && !us.isAnonymous()){
                    Intent requests = new Intent(HomeActivity.this, GenerateReportsActivity.class);
                    requests.putExtra("idGardenFirebaseDoc","null");
                    requests.putExtra("idUser","null");
                    requests.putExtra("garden","false");//con esto se define si, al ejecutar GenerateReportsActivity es solo para la huerta o para todos
                    requests.putExtra("ownerName","null");
                    startActivity(requests);
                    finish();
                }
                else{
                    Toast.makeText(HomeActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void fillListGardens(List<ItemGardenHomeList> gardenInfoDocument) {

        try {
            if(isOnline()) {
                Thread.sleep(15);
            }
            // Crear un comparador que compare los objetos por nombre
            Comparator<ItemGardenHomeList> nameComparator = new Comparator<ItemGardenHomeList>() {
                @Override
                public int compare(ItemGardenHomeList item1, ItemGardenHomeList item2) {
                    return item1.getName().compareTo(item2.getName());
                }
            };
            // Ordenar la lista usando el comparador
            Collections.sort(gardenInfoDocument, nameComparator);
            GardenListAdapter adapter = new GardenListAdapter(this, gardenInfoDocument);
            listAviableGardensInfo.setAdapter(adapter);
            listAviableGardensInfo.setDividerHeight(5);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showProgressDialog(int durationMs) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cargando...");
        progressDialog.setMessage("Espera un momento, estamos configurando tu cuenta");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(durationMs / 750);
        progressDialog.setProgress(0);
        progressDialog.show();

        final int[] currentProgress = {0};
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.setProgress(currentProgress[0]++);
                    if (currentProgress[0] <= progressDialog.getMax()) {
                        handler.postDelayed(this, 1000);
                    } else {
                        if(!((Activity) HomeActivity.this).isFinishing()){
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                }
            }
        });
    }
}