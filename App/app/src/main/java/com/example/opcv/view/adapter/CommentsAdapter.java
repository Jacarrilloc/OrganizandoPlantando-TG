package com.example.opcv.view.adapter;

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

import com.bumptech.glide.Glide;
import com.example.opcv.R;
import com.example.opcv.business.ludification.Level;
import com.example.opcv.model.items.ItemComments;
import com.example.opcv.model.persistance.firebase.LudificationCommunication;
import com.example.opcv.model.persistance.firebase.UserCommunication;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private SparseArray<CircleImageView> imageArray = new SparseArray<>();

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
        Level level = new Level();
        LudificationCommunication persistance = new LudificationCommunication();
        UserCommunication persistanceuser = new UserCommunication();
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
        CircleImageView image = convertView.findViewById(R.id.image);
        imageArray.put(position, image);

        ItemComments IC = new ItemComments(item.getComment(), item.getNameCommentator(), item.getIdUSer());

        commentator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout currentLevelLayout = levelLayoutArray.get(position);
                TextView currentAuthor = authorArray.get(position);
                TextView currentPublisherLevel = publisherLevelArray.get(position);
                TextView currentNameLevel = nameLevelArray.get(position);
                ImageView currentBorderImage = borderImageArray.get(position);
                CircleImageView currentImage =imageArray.get(position);
                if (currentLevelLayout.getVisibility() == View.GONE) {
                    currentAuthor.setText(item.getNameCommentator());
                    persistance.getPublisherLevel(IC.getIdUSer(), new LudificationCommunication.getPublisherLevel() {
                        @Override
                        public void onComplete(String leveli) {
                            currentPublisherLevel.setText(leveli);

                            double lvDouble = Double.parseDouble(publisherLevel.getText().toString());
                            int lv = Double.valueOf(lvDouble).intValue();
                            currentNameLevel.setText(level.levelName(lv));

                            persistanceuser.getProfilePicture(IC.getIdUSer(), new UserCommunication.GetUriUser() {
                                @Override
                                public void onComplete(String uri) {
                                    if(!Objects.equals(uri, "")){
                                        Glide.with(context).load(uri).into(currentImage);
                                    }
                                    else{
                                        currentImage.setImageResource(R.drawable.im_logo_ceres);
                                    }
                                }
                            });

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

