package com.example.opcv.view.gardens;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.example.opcv.R;
import com.example.opcv.model.persistance.firebase.GardenCommunication;
import com.example.opcv.view.adapter.CollaboratorListAdapter;
import com.example.opcv.model.items.ItemCollaboratorsRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class GardenRequestsActivity extends AppCompatActivity {
    private FloatingActionButton backButton;
    private FirebaseAuth autentication;
    private ListView listGardens;
    private Animation animSlideUp;
    private String gardenId, garden;

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_requests);

        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        autentication = FirebaseAuth.getInstance();
        listGardens = findViewById(R.id.listViewCollabs);
        backButton = (FloatingActionButton) findViewById(R.id.returnArrowButtonFormsToGarden);
        GardenCommunication gardenCom = new GardenCommunication();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            garden = extras.getString("Name");
            gardenId = extras.getString("idGardenFirebase");

        }
        gardenCom.fillGardenRequests(gardenId, this, this);
    }

    public void fillListRequests(List<ItemCollaboratorsRequest> requestDocument){
        CollaboratorListAdapter adapter = new CollaboratorListAdapter(this, requestDocument);
        listGardens.setAdapter(adapter);
        listGardens.setDividerHeight(15);
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