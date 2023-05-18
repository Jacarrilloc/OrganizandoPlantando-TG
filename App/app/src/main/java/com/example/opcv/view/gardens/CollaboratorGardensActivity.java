package com.example.opcv.view.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.model.persistance.firebase.GardenCommunication;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.adapter.MyCollaborationsListAdapter;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.model.items.ItemCollaboratorsRequest;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class CollaboratorGardensActivity extends AppCompatActivity {
    private Button rewards, profile, myGardens, ludification;
    private String userId;
    private ListView listGardens;
    private FirebaseAuth autentication;

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborator_gardens);

        autentication = FirebaseAuth.getInstance();
        ludification = (Button) findViewById(R.id.ludification);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        listGardens = findViewById(R.id.collaborationGardenList);
        GardenCommunication gardenCom = new GardenCommunication();

        Bundle extras = getIntent().getExtras();
        userId = getIntent().getStringExtra("userID");
        if (userId == null){
            AuthCommunication auth = new AuthCommunication();
            userId = auth.getCurrentUserUid();
        }
        if(extras != null){
            String id = extras.getString("ID");
        }
        if(userId != null){
            gardenCom.fillGardenUser(userId, this, this);
        }
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId != null) {
                    Intent edit = new Intent(CollaboratorGardensActivity.this, EditUserActivity.class);
                    edit.putExtra("userInfo", userId);
                    startActivity(edit);
                }
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CollaboratorGardensActivity.this, HomeActivity.class));
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CollaboratorGardensActivity.this, RewardHomeActivity.class));
            }
        });

        listGardens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object selectedItem = adapterView.getItemAtPosition(i);
                String itemName = ((ItemCollaboratorsRequest) selectedItem).getName();
                String userID = userId;
                String idGarden = ((ItemCollaboratorsRequest) selectedItem).getIdGarden();

                Intent start = new Intent(CollaboratorGardensActivity.this, GardenActivity.class);
                start.putExtra("ID",userID);
                start.putExtra("gardenName",itemName);
                start.putExtra("idGarden", idGarden);
                start.putExtra("idGardenFirebaseDoc",idGarden);
                start.putExtra("owner", "false");
                startActivity(start);
                finish();
            }
        });


        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(CollaboratorGardensActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(CollaboratorGardensActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void fillListGardens( List<ItemCollaboratorsRequest> gardenInfoDocument){
        try{
            Thread.sleep(65);
            MyCollaborationsListAdapter adapter = new MyCollaborationsListAdapter(this, gardenInfoDocument);
            listGardens.setAdapter(adapter);
            listGardens.setDividerHeight(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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

}