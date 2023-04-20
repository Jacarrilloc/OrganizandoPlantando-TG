package com.example.opcv.ludificationScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.adapter.CommentsAdapter;
import com.example.opcv.adapter.PlantsToolsAdapter;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.business.ludificationLogic.LudificationLogic;
import com.example.opcv.business.ludificationLogic.levelLogic;
import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.gardens.GardensAvailableActivity;
import com.example.opcv.item_list.ItemComments;
import com.example.opcv.persistance.ludificationPersistance.LudificationPersistance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowDictionaryItemActivity extends AppCompatActivity {

    private Button profile, myGardens, gardensMap, ludification;
    private String idUser, element, docRef, imageUri;
    private TextView authorName, elementName, likeNumber, dislikeNumber, description, tag1, tag2,tag3, tag4, tag5, tag6;
    private EditText input;
    private FloatingActionButton add, sendComment;
    private ImageButton likeButton, dislikeButton;
    private ListView listView;
    private CircleImageView image;

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
        sendComment = (FloatingActionButton) findViewById(R.id.sendButton);
        tag1 = (TextView) findViewById(R.id.tag1);
        tag2 = (TextView) findViewById(R.id.tag2);
        tag3 = (TextView) findViewById(R.id.tag3);
        tag4 = (TextView) findViewById(R.id.tag4);
        tag5 = (TextView) findViewById(R.id.tag5);
        tag6 = (TextView) findViewById(R.id.tag6);
        listView = (ListView) findViewById(R.id.listViewComments);
        input = (EditText) findViewById(R.id.inputText);
        image = (CircleImageView) findViewById(R.id.imageItem);

        LudificationPersistance persistance = new LudificationPersistance();
        LudificationLogic logic = new LudificationLogic();
        levelLogic level = new levelLogic();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idUser = extras.getString("userInfo");//user loggeado
            element = extras.getString("element");
            docRef = extras.getString("idDoc");
        }
        persistance.getImage(element, docRef, new LudificationPersistance.GetURi() {
            @Override
            public void onSuccess(String uri) {
                if(uri != null){
                    Glide.with(ShowDictionaryItemActivity.this).load(uri).into(image);
                }
            }
        });

        if(element.equals("Plants")){
            persistance.searchPlantName(docRef, new LudificationPersistance.GetPlantName() {
                @Override
                public void onSuccess(String name) {
                    elementName.setText(name);
                }
            });
            persistance.tags(element, docRef, new LudificationPersistance.GetTagsList() {
                @Override
                public void onComplete(ArrayList<String> list) {
                    int size = list.size();
                    switch (size){
                        case 0:
                            tag1.setVisibility(View.INVISIBLE);
                            tag2.setVisibility(View.INVISIBLE);
                            tag3.setVisibility(View.INVISIBLE);
                            tag5.setVisibility(View.INVISIBLE);
                            tag6.setVisibility(View.INVISIBLE);
                            tag4.setVisibility(View.INVISIBLE);
                            break;
                        case 1:
                            tag1.setText(list.get(0));
                            tag2.setVisibility(View.INVISIBLE);
                            tag3.setVisibility(View.INVISIBLE);
                            tag5.setVisibility(View.INVISIBLE);
                            tag6.setVisibility(View.INVISIBLE);
                            tag4.setVisibility(View.INVISIBLE);
                            break;
                        case 2:
                            tag1.setText(list.get(0));
                            tag2.setText(list.get(1));
                            tag3.setVisibility(View.INVISIBLE);
                            tag5.setVisibility(View.INVISIBLE);
                            tag6.setVisibility(View.INVISIBLE);
                            tag4.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            tag1.setText(list.get(0));
                            tag2.setText(list.get(1));
                            tag3.setText(list.get(2));
                            tag5.setVisibility(View.INVISIBLE);
                            tag6.setVisibility(View.INVISIBLE);
                            tag4.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            tag1.setText(list.get(0));
                            tag2.setText(list.get(1));
                            tag3.setText(list.get(2));
                            tag4.setText(list.get(3));
                            tag5.setVisibility(View.INVISIBLE);
                            tag6.setVisibility(View.INVISIBLE);
                            break;
                        case 5:
                            tag1.setText(list.get(0));
                            tag2.setText(list.get(1));
                            tag3.setText(list.get(2));
                            tag4.setText(list.get(3));
                            tag5.setText(list.get(4));
                            tag6.setVisibility(View.INVISIBLE);
                            break;
                        case 6: tag1.setText(list.get(0));
                            tag2.setText(list.get(1));
                            tag3.setText(list.get(2));
                            tag4.setText(list.get(3));
                            tag5.setText(list.get(4));
                            tag6.setText(list.get(5));
                            break;
                    }
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
            tag1.setVisibility(View.INVISIBLE);
            tag2.setVisibility(View.INVISIBLE);
            tag3.setVisibility(View.INVISIBLE);
            persistance.tags(element, docRef, new LudificationPersistance.GetTagsList() {
                @Override
                public void onComplete(ArrayList<String> list) {
                    int size = list.size();
                    switch (size){
                        case 0:
                            tag5.setVisibility(View.INVISIBLE);
                            tag6.setVisibility(View.INVISIBLE);
                            tag4.setVisibility(View.INVISIBLE);
                            break;
                        case 1:
                            tag5.setVisibility(View.INVISIBLE);
                            tag6.setVisibility(View.INVISIBLE);
                            tag4.setText(list.get(0));
                            break;
                        case 2:
                            tag4.setText(list.get(0));
                            tag5.setText(list.get(1));
                            tag6.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            tag4.setText(list.get(0));
                            tag5.setText(list.get(1));
                            tag6.setText(list.get(2));
                            break;
                    }
                }
            });
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.setVisibility(View.INVISIBLE);
                add.setClickable(false);
                add.setFocusable(false);
                sendComment.setVisibility(View.VISIBLE);
                sendComment.setClickable(true);
                sendComment.setFocusable(true);
                input.setVisibility(View.VISIBLE);
                input.setClickable(true);
                input.setFocusable(true);
                input.setEnabled(true);
                input.setText("");
                input.setHint("Ingrese comentario");
            }
        });
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.setVisibility(View.VISIBLE);
                add.setClickable(true);
                add.setFocusable(true);
                sendComment.setVisibility(View.INVISIBLE);
                sendComment.setClickable(false);
                sendComment.setFocusable(false);
                //añadir la logica para recibir de un edittext
                String textInput = String.valueOf(input.getText());
                if(!textInput.isEmpty()){
                    logic.addComments(element, idUser,  textInput, docRef, ShowDictionaryItemActivity.this);
                    level.addLevel(idUser, false, ShowDictionaryItemActivity.this);

                }
                recreate();
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se llama a metodo que recibe element, docref, true (es decir que es de like) y se suma a variable likes
                //LudificationLogic logic = new LudificationLogic();
                logic.likesDislikes(docRef, true, element);
                likeButton.setEnabled(false);
                dislikeButton.setEnabled(false);
                int number = Integer.parseInt(likeNumber.getText().toString());
                number++;
                String numberText = String.valueOf(number);
                likeNumber.setText(numberText);
                //likeButton.setImageResource(R.drawable.im_like_gray);
                //dislikeButton.setImageResource(R.drawable.im_dislike_gray);
            }
        });
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se llama a metodo que recibe element, docref, false (es decir que es de dislike) y se suma a variable dislikes
                //llama a metodo de levelLogic deductlevel
                //LudificationLogic logic = new LudificationLogic();
                //levelLogic level = new levelLogic();
                level.deductLevel(docRef, element);
                logic.likesDislikes(docRef, false, element);
                likeButton.setEnabled(false);
                dislikeButton.setEnabled(false);
                int number = Integer.parseInt(dislikeNumber.getText().toString());
                number++;
                String numberText = String.valueOf(number);
                dislikeNumber.setText(numberText);
                //dislikeButton.setImageResource(R.drawable.im_dislike_gray);
                //likeButton.setImageResource(R.drawable.im_like_gray);
            }
        });

        //a continuacion se hace para mostrar los comentarios
        persistance.retrieveComments(element, docRef, new LudificationPersistance.GetComments() {
            @Override
            public void onComplete(Map<String, String> mapComments) {
                List<ItemComments> list = new ArrayList<>();
                for(Map.Entry<String, String> entry : mapComments.entrySet()){
                    persistance.getPublisherName(entry.getValue(), new LudificationPersistance.GetPublisher() {
                        @Override
                        public void onComplete(String name) {
                            ItemComments com = new ItemComments(entry.getKey(), name);
                            list.add(com);
                            fillList(list);
                        }
                    });

                }

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

        persistance.searchPublisher(element, docRef, new LudificationPersistance.GetUserId() {
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

    private void fillList(List<ItemComments> comments){
        CommentsAdapter adapter = new CommentsAdapter(this, comments);
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