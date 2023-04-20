package com.example.opcv.ludificationScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.adapter.PlantsToolsAdapter;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.business.ludificationLogic.LudificationLogic;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.gardens.GardensAvailableActivity;
import com.example.opcv.item_list.ItemPlantsTools;
import com.example.opcv.persistance.ludificationPersistance.LudificationPersistance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShowDictionaryActivity extends AppCompatActivity {

    private TextView name;
    private FloatingActionButton add;
    private String element, idUser;
    private Button profile, myGardens, gardensMap, ludification;
    private EditText search;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dictionary);

        name = (TextView) findViewById(R.id.name);
        add = (FloatingActionButton) findViewById(R.id.addButton);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        gardensMap = (Button) findViewById(R.id.gardens);
        ludification = (Button) findViewById(R.id.ludification);
        search = (EditText) findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.plantsList);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idUser = extras.getString("userInfo");
            element = extras.getString("element");
        }

        if(element.equals("Plants")){
            name.setText("Plantas");
            LudificationPersistance persistance = new LudificationPersistance();
            persistance.getPlantElements(new LudificationPersistance.GetPlants() {
                @Override
                public void onComplete(Map<String, String> map) {
                    //String name, id;
                    //System.out.println("el map: "+map);
                    List<ItemPlantsTools> plantsTools = new ArrayList<>();
                    for(Map.Entry<String, String> entry : map.entrySet()){
                        persistance.getImage(element, entry.getKey(), new LudificationPersistance.GetURi() {
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
                    Intent edit = new Intent(ShowDictionaryActivity.this, CreatePlantActivity.class);
                    edit.putExtra("userInfo", idUser);
                    startActivity(edit);
                }
            });
        }
        else if(element.equals("Tools")){
            name.setText("Herramientas");
            LudificationPersistance persistance = new LudificationPersistance();
            persistance.getToolElements(new LudificationPersistance.GetPlants() {
                @Override
                public void onComplete(Map<String, String> map) {
                    List<ItemPlantsTools> plantsTools = new ArrayList<>();
                    for(Map.Entry<String, String> entry : map.entrySet()){
                        persistance.getImage(element, entry.getKey(), new LudificationPersistance.GetURi() {
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
                    Intent edit = new Intent(ShowDictionaryActivity.this, CreateToolActivity.class);
                    edit.putExtra("userInfo", idUser);
                    startActivity(edit);
                }
            });
        }

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowDictionaryActivity.this, GardensAvailableActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowDictionaryActivity.this, EditUserActivity.class);
                AuthUtilities auth = new AuthUtilities();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowDictionaryActivity.this, MapsActivity.class));
            }
        });
        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowDictionaryActivity.this, DictionaryHome.class);
                startActivity(edit);
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

    }
    public void fillList(List<ItemPlantsTools> requestDocument){
        PlantsToolsAdapter adapter = new PlantsToolsAdapter(this, requestDocument);
        listView.setAdapter(adapter);
        listView.setDividerHeight(15);
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