package com.example.opcv.ludificationScreens;

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
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.business.ludificationLogic.LudificationLogic;
import com.example.opcv.business.ludificationLogic.levelLogic;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.gardens.GardensAvailableActivity;
import com.example.opcv.persistance.ludificationPersistance.LudificationPersistance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

public class ShowDictionaryItemActivity extends AppCompatActivity {

    private Button profile, myGardens, gardensMap, ludification;
    private String idUser, element, docRef;
    private TextView authorName, elementName, likeNumber, dislikeNumber, description;
    private FloatingActionButton add;
    private ImageButton likeButton, dislikeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dictionary_item);

        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        gardensMap = (Button) findViewById(R.id.gardens);
        ludification = (Button) findViewById(R.id.ludification);
        authorName = (TextView) findViewById(R.id.nameAuthor);
        elementName = (TextView) findViewById(R.id.nameItem);
        likeNumber = (TextView) findViewById(R.id.likeNumber);
        dislikeNumber = (TextView) findViewById(R.id.dislikeNumber);
        description = (TextView) findViewById(R.id.descriptionAuthor);
        add = (FloatingActionButton) findViewById(R.id.addButton);
        likeButton = (ImageButton) findViewById(R.id.likebutton);
        dislikeButton = (ImageButton) findViewById(R.id.dislikebutton);

        LudificationPersistance persistance = new LudificationPersistance();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idUser = extras.getString("userInfo");//user loggeado
            element = extras.getString("element");
            docRef = extras.getString("idDoc");
        }

        if(element.equals("Plants")){
            persistance.searchPlantName(docRef, new LudificationPersistance.GetPlantName() {
                @Override
                public void onSuccess(String name) {
                    elementName.setText(name);
                }
            });
        }
        else if(element.equals("Tools")){
            persistance.searchToolName(docRef, new LudificationPersistance.GetToolName() {
                @Override
                public void onSuccess(String name) {
                    elementName.setText(name);
                }
            });
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se llama a metodo que recibe element, docref, true (es decir que es de like) y se suma a variable likes
                LudificationLogic logic = new LudificationLogic();
                logic.likesDislikes(docRef, true, element);
                likeButton.setEnabled(false);
                dislikeButton.setEnabled(false);
                int number = Integer.parseInt(likeNumber.getText().toString());
                number++;
                String numberText = String.valueOf(number);
                likeNumber.setText(numberText);
            }
        });
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se llama a metodo que recibe element, docref, false (es decir que es de dislike) y se suma a variable dislikes
                //llama a metodo de levelLogic deductlevel
                LudificationLogic logic = new LudificationLogic();
                levelLogic level = new levelLogic();
                level.deductLevel(docRef, element);
                logic.likesDislikes(docRef, false, element);
                likeButton.setEnabled(false);
                dislikeButton.setEnabled(false);
                int number = Integer.parseInt(dislikeNumber.getText().toString());
                number++;
                String numberText = String.valueOf(number);
                dislikeNumber.setText(numberText);
            }
        });


        persistance.getLikesDislikes(docRef, element, this, new LudificationPersistance.GetLikesDislikes() {
            @Override
            public void onSuccess(Map<String, String> publicLikes) {
                likeNumber.setText(publicLikes.get("Likes"));
                dislikeNumber.setText(publicLikes.get("Dislikes"));
                description.setText(publicLikes.get("Description"));
            }
        });
        persistance.searchPublisher(idUser, new LudificationPersistance.GetUserId() {
            @Override
            public void onSuccess(String name) {
                authorName.setText("Por "+name);
            }
        });


        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowDictionaryItemActivity.this, GardensAvailableActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowDictionaryItemActivity.this, EditUserActivity.class);
                AuthUtilities auth = new AuthUtilities();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        gardensMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowDictionaryItemActivity.this, MapsActivity.class));
            }
        });
        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(ShowDictionaryItemActivity.this, DictionaryHome.class);
                startActivity(edit);
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