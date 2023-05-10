package com.example.opcv.view.ludification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.view.auth.SignOffActivity;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.adapter.PlantsToolsAdapter;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.model.items.ItemPlantsTools;
import com.example.opcv.model.persistance.firebase.LudificationCommunication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowDictionaryActivity extends AppCompatActivity {

    private TextView name;
    private FloatingActionButton add, back;
    private String element, idUser;
    private Button profile, myGardens, rewards, ludification;
    private androidx.appcompat.widget.SearchView searchView;
    private GridView listView;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dictionary);

        name = (TextView) findViewById(R.id.name);
        add = (FloatingActionButton) findViewById(R.id.addButton);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.searchView);
        listView = (GridView) findViewById(R.id.plantsList);
        back = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);
        AuthCommunication authCommunication = new AuthCommunication();
        FirebaseUser user = authCommunication.guestUser();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idUser = extras.getString("userInfo");
            element = extras.getString("element");
        }

        if(element.equals("Plants")){
            name.setText("Plantas");
            LudificationCommunication persistance = new LudificationCommunication();
            persistance.getPlantElements(new LudificationCommunication.GetPlants() {
                @Override
                public void onComplete(Map<String, String> map) {
                    //String name, id;
                    //System.out.println("el map: "+map);
                    List<ItemPlantsTools> plantsTools = new ArrayList<>();
                    for(Map.Entry<String, String> entry : map.entrySet()){
                        persistance.getImage(element, entry.getKey(), new LudificationCommunication.GetURi() {
                            @Override
                            public void onSuccess(String uri) {

                                ItemPlantsTools newItem = new ItemPlantsTools(entry.getValue(), entry.getKey(), uri);
                                plantsTools.add(newItem);
                                fillList(plantsTools);
                            }
                        });

                    }

                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(user != null && !user.isAnonymous()){
                        Intent edit = new Intent(ShowDictionaryActivity.this, CreatePlantActivity.class);
                        edit.putExtra("userInfo", idUser);
                        startActivity(edit);
                    }
                    else{
                        Toast.makeText(ShowDictionaryActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if(element.equals("Tools")){
            name.setText("Herramientas");
            LudificationCommunication persistance = new LudificationCommunication();
            persistance.getToolElements(new LudificationCommunication.GetPlants() {
                @Override
                public void onComplete(Map<String, String> map) {
                    List<ItemPlantsTools> plantsTools = new ArrayList<>();
                    for(Map.Entry<String, String> entry : map.entrySet()){
                        persistance.getImage(element, entry.getKey(), new LudificationCommunication.GetURi() {
                            @Override
                            public void onSuccess(String uri) {
                                ItemPlantsTools newItem = new ItemPlantsTools(entry.getValue(), entry.getKey(), uri);
                                plantsTools.add(newItem);
                                fillList(plantsTools);
                            }
                        });
                    }
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(user != null && !user.isAnonymous()){
                        Intent edit = new Intent(ShowDictionaryActivity.this, CreateToolActivity.class);
                        edit.putExtra("userInfo", idUser);
                        startActivity(edit);
                    }
                    else{
                        Toast.makeText(ShowDictionaryActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowDictionaryActivity.this, HomeActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null && !user.isAnonymous()){
                    Intent edit = new Intent(ShowDictionaryActivity.this, EditUserActivity.class);
                    AuthCommunication auth = new AuthCommunication();
                    String userId = auth.getCurrentUserUid();
                    edit.putExtra("userInfo", userId);
                    startActivity(edit);
                }
                else{
                    startActivity(new Intent(ShowDictionaryActivity.this, SignOffActivity.class));
                }
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null && !user.isAnonymous()){
                    startActivity(new Intent(ShowDictionaryActivity.this, RewardHomeActivity.class));
                }
                else{
                    Toast.makeText(ShowDictionaryActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(ShowDictionaryActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(ShowDictionaryActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object selectedItem = adapterView.getItemAtPosition(i);
                String docRef = ((ItemPlantsTools) selectedItem).getId();//se obtiene el id de ese elemento
                Intent edit = new Intent(ShowDictionaryActivity.this, ShowDictionaryItemActivity.class);
                edit.putExtra("userInfo", idUser);
                edit.putExtra("element", element);
                edit.putExtra("idDoc", docRef);
                startActivity(edit);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    public void fillList(List<ItemPlantsTools> requestDocument){
        PlantsToolsAdapter adapter = new PlantsToolsAdapter(this, requestDocument, element);
        listView.setAdapter(adapter);
        listView.setVerticalSpacing(15);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return true;
            }
        });

    }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}