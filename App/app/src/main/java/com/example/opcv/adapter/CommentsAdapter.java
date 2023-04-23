package com.example.opcv.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opcv.R;
import com.example.opcv.business.ludificationLogic.levelLogic;
import com.example.opcv.fbComunication.CollaboratorUtilities;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.example.opcv.item_list.ItemComments;
import com.example.opcv.persistance.ludificationPersistance.LudificationPersistance;

import java.util.List;

public class CommentsAdapter extends ArrayAdapter<ItemComments> {

    private Context context;
    private TextView comment, commentator , author, publisherLevel, namelevel;
    private FrameLayout levelLayout;
    private ImageView borderImage;
    private SparseArray<FrameLayout> levelLayoutArray = new SparseArray<>();
    private SparseArray<TextView> authorArray = new SparseArray<>();
    private SparseArray<TextView> publisherLevelArray = new SparseArray<>();
    private SparseArray<TextView> nameLevelArray = new SparseArray<>();
    private SparseArray<ImageView> borderImageArray = new SparseArray<>();

    public CommentsAdapter(Context context, List<ItemComments> items) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_new_description, parent, false);
        }
        ItemComments item = getItem(position);

        comment = convertView.findViewById(R.id.description);
        commentator = convertView.findViewById(R.id.author);
        comment.setText(item.getComment());
        commentator.setText(item.getNameCommentator());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);
        levelLogic level = new levelLogic();
        LudificationPersistance persistance = new LudificationPersistance();
        levelLayout = convertView.findViewById(R.id.showLevel);
        levelLayoutArray.put(position, levelLayout);
        TextView author = convertView.findViewById(R.id.name);
        authorArray.put(position, author);
        TextView publisherLevel = convertView.findViewById(R.id.level);
        publisherLevelArray.put(position, publisherLevel);
        TextView namelevel = convertView.findViewById(R.id.nameLevel);
        nameLevelArray.put(position, namelevel);
        ImageView borderImage = convertView.findViewById(R.id.imageLevel);
        borderImageArray.put(position, borderImage);

        ItemComments IC = new ItemComments(item.getComment(), item.getNameCommentator(), item.getIdUSer());

        commentator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout currentLevelLayout = levelLayoutArray.get(position);
                TextView currentAuthor = authorArray.get(position);
                TextView currentPublisherLevel = publisherLevelArray.get(position);
                TextView currentNameLevel = nameLevelArray.get(position);
                ImageView currentBorderImage = borderImageArray.get(position);
                if (currentLevelLayout.getVisibility() == View.GONE) {
                    currentAuthor.setText(item.getNameCommentator());

                    persistance.getPublisherLevel(IC.getIdUSer(), new LudificationPersistance.getPublisherLevel() {
                        @Override
                        public void onComplete(String leveli) {
                            currentPublisherLevel.setText(leveli);

                            System.out.println(publisherLevel.getText().toString());
                            double lvDouble = Double.parseDouble(publisherLevel.getText().toString().trim().replace("#", ""));
                            int lv = Double.valueOf(lvDouble).intValue();
                            currentNameLevel.setText(level.levelName(lv));

                            if (lv >=0 && lv <10){
                                currentBorderImage.setImageResource(R.drawable.im_level_1);
                            }else if (lv>= 10 && lv <30) {
                                currentBorderImage.setImageResource(R.drawable.im_level_2);
                            } else if (lv>=30 && lv <60) {
                                currentBorderImage.setImageResource(R.drawable.im_level_3);
                            } else if (lv >= 60 && lv <100) {
                                currentBorderImage.setImageResource(R.drawable.im_level_4);
                            } else if (lv >= 100) {
                                currentBorderImage.setImageResource(R.drawable.im_level_5);
                            }

                            Animation fadeIn = new AlphaAnimation(0, 1);
                            fadeIn.setInterpolator(new DecelerateInterpolator());
                            fadeIn.setDuration(900);

                            currentLevelLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    currentLevelLayout.setVisibility(View.VISIBLE);
                                    currentLevelLayout.startAnimation(fadeIn);
                                }
                            });
                        }
                    });
                } else {
                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setDuration(900);

                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            currentLevelLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });

                    currentLevelLayout.startAnimation(fadeOut);
                }
            }
        });

        return convertView;
    }
}