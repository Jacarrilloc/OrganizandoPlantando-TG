package com.example.opcv.view.gardens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.opcv.R;

public class WhatsappActivity extends AppCompatActivity {
    private Button addLink, returnButton;
    private EditText editLinkText;
    private String idGarden, id, garden, groupLink;
    private String owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp);
        addLink = (Button) findViewById(R.id.addLinkbutton);
        returnButton = (Button) findViewById(R.id.returnButtonLink);
        editLinkText = (EditText) findViewById(R.id.groupLInk);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("ID");
            garden = extras.getString("gardenName");
            idGarden = extras.getString("idGarden");
            groupLink = extras.getString("GroupLink");
            owner = "true";
        }

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = editLinkText.getText().toString();
                Intent newForm = new Intent(WhatsappActivity.this, GardenActivity.class);
                newForm.putExtra("ID", id);
                newForm.putExtra("idGarden", idGarden);
                newForm.putExtra("gardenName", garden);
                //newForm.putExtra("infoGarden", infoGarden);
                newForm.putExtra("owner", owner);
                newForm.putExtra("GroupLink", link);
                startActivity(newForm);
                finish();
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
}