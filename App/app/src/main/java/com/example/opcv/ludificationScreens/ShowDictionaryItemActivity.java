package com.example.opcv.ludificationScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.opcv.HomeActivity;
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
import com.example.opcv.persistance.userPersistance.UserPersistance;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowDictionaryItemActivity extends AppCompatActivity {

    private Button profile, myGardens, gardensMap, ludification;
    private String idUser, element, docRef, imageUri;
    private TextView authorName, elementName, likeNumber, dislikeNumber, description, tag1, tag2,tag3, tag4, tag5, tag6, author, publisherLevel, namelevel;
    private EditText input;
    private FloatingActionButton add, sendComment;
    private ImageButton likeButton, dislikeButton;
    private ListView listView;
    private FrameLayout authorLayout;
    private ImageView borderImage, dotborderImage;

    private CircleImageView image, imagePusblisher;

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
        listView = (ListView) findViewById(R.id.listViewComments);
        input = (EditText) findViewById(R.id.inputText);
        image = (CircleImageView) findViewById(R.id.imageItem);
        dotborderImage = (ImageView) findViewById(R.id.border);

        //Vista del autor de la descripción
        authorLayout = (FrameLayout) findViewById(R.id.authorCard);
        author = (TextView) findViewById(R.id.name);
        publisherLevel = (TextView) findViewById(R.id.level);
        namelevel = (TextView) findViewById(R.id.nameLevel);
        borderImage = (ImageView) findViewById(R.id.imageLevel);
        imagePusblisher = (CircleImageView) findViewById(R.id.image);

        LudificationPersistance persistance = new LudificationPersistance();
        UserPersistance userPersistance = new UserPersistance();
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
            dotborderImage.setImageResource(R.drawable.im_circle_border_purple);
            persistance.searchPlantName(docRef, new LudificationPersistance.GetPlantName() {
                @Override
                public void onSuccess(String name) {
                    elementName.setText(name);
                }
            });
            persistance.tags(element, docRef, new LudificationPersistance.GetTagsList() {
                @Override
                public void onComplete(ArrayList<String> list) {
                    FlexboxLayout tagsLayout = findViewById(R.id.tags_layout);

                    for (String tag : list) {
                        View cardView = LayoutInflater.from(ShowDictionaryItemActivity.this).inflate(R.layout.item_tag, tagsLayout, false);
                        TextView textView = cardView.findViewById(R.id.formsTittle);
                        textView.setText(tag);
                        tagsLayout.addView(cardView);
                    }
                }
            });
        }
        else if(element.equals("Tools")){
            dotborderImage.setImageResource(R.drawable.im_circle_border_blue);
            persistance.searchToolName(docRef, new LudificationPersistance.GetToolName() {
                @Override
                public void onSuccess(String name) {
                    elementName.setText(name);
                }
            });
            persistance.tags(element, docRef, new LudificationPersistance.GetTagsList() {
                @Override
                public void onComplete(ArrayList<String> list) {
                    FlexboxLayout tagsLayout = findViewById(R.id.tags_layout);

                    for (String tag : list) {
                        View cardView = LayoutInflater.from(ShowDictionaryItemActivity.this).inflate(R.layout.item_tag, tagsLayout, false);
                        TextView textView = cardView.findViewById(R.id.formsTittle);
                        textView.setText(tag);
                        tagsLayout.addView(cardView);
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

        authorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (authorLayout.getVisibility() == View.GONE) {
                    String newText = removeFirstThreeLetters(authorName.getText().toString());
                    author.setText(newText);
                    persistance.searchPublisherLevel(element, docRef, new LudificationPersistance.GetLevel() {
                        @Override
                        public void onSuccess(String leveli) {
                            publisherLevel.setText(leveli);
                            double lvDouble = Double.parseDouble(publisherLevel.getText().toString().trim().replace("#", ""));
                            int lv = Double.valueOf(lvDouble).intValue();
                            namelevel.setText(level.levelName(lv));

                            persistance.getPublisherID(element, docRef, new LudificationPersistance.GetPublisherId() {
                                @Override
                                public void onComplete(String userID) {
                                    userPersistance.getProfilePicture(userID, new UserPersistance.GetUriUser() {
                                        @Override
                                        public void onComplete(String uri) {
                                            if(!Objects.equals(uri, "")){
                                                Glide.with(ShowDictionaryItemActivity.this).load(uri).into(imagePusblisher);
                                            }
                                            else{
                                                imagePusblisher.setImageResource(R.drawable.im_logo_ceres);
                                            }

                                            if (lv >=0 && lv <10){
                                                borderImage.setImageResource(R.drawable.im_level_1);
                                            }else if (lv>= 10 && lv <30) {
                                                borderImage.setImageResource(R.drawable.im_level_2);
                                            } else if (lv>=30 && lv <60) {
                                                borderImage.setImageResource(R.drawable.im_level_3);
                                            } else if (lv >= 60 && lv <100) {
                                                borderImage.setImageResource(R.drawable.im_level_4);
                                            } else if (lv >= 100) {
                                                borderImage.setImageResource(R.drawable.im_level_5);
                                            }

                                            Animation fadeIn = new AlphaAnimation(0, 1);
                                            fadeIn.setInterpolator(new DecelerateInterpolator());
                                            fadeIn.setDuration(1000);

                                            authorLayout.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    authorLayout.setVisibility(View.VISIBLE);
                                                    authorLayout.startAnimation(fadeIn);
                                                }
                                            });
                                        }
                                    });



                                }
                            });


                        }
                    });
                } else {
                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setDuration(1000);

                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            authorLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });

                    authorLayout.startAnimation(fadeOut);
                }
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
                    level.addLevel(idUser, false, ShowDictionaryItemActivity.this, element);

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
                likeButton.setBackgroundResource(R.drawable.im_like_gray);
                dislikeButton.setBackgroundResource(R.drawable.im_dislike_gray);
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
                likeButton.setBackgroundResource(R.drawable.im_like_gray);
                dislikeButton.setBackgroundResource(R.drawable.im_dislike_gray);
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
                            ItemComments com = new ItemComments(entry.getKey(), name, entry.getValue());
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String name) {
                authorName.setText("Por "+name);
            }
        });




        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowDictionaryItemActivity.this, HomeActivity.class));
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
        listView.setDividerHeight(10);
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

    private void addTagsToLayout(List<String> tags, LinearLayout tagsLayout) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int currentRowWidth = 0;
        LinearLayout currentRow = createNewRow();

        for (String tag : tags) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.item_tag, tagsLayout, false);
            TextView textView = cardView.findViewById(R.id.formsTittle);
            textView.setText(tag);

            cardView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int cardViewWidth = cardView.getMeasuredWidth();

            if (currentRowWidth + cardViewWidth > screenWidth) {
                tagsLayout.addView(currentRow);
                currentRow = createNewRow();
                currentRowWidth = 0;
            }

            currentRow.addView(cardView);
            currentRowWidth += cardViewWidth;
        }

        if (currentRow.getChildCount() > 0) {
            tagsLayout.addView(currentRow);
        }
    }

    private LinearLayout createNewRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return row;
    }

    public String removeFirstThreeLetters(String inputText) {
        if (inputText.length() > 3) {
            return inputText.substring(3);
        } else {
            // Si el texto tiene menos de 3 caracteres, devuelve una cadena vacía
            return "";
        }
    }

}