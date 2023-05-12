package com.example.opcv.view.adapter;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opcv.R;

import com.example.opcv.business.ludification.Level;
import com.example.opcv.model.items.ItemComments;
import com.example.opcv.model.persistance.firebase.LudificationCommunication;
import com.example.opcv.model.persistance.firebase.UserCommunication;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private Context context;
    private List<ItemComments> items;

    public CommentsAdapter(Context context, List<ItemComments> items) {
        this.context = context;
        this.items = items;
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {
        TextView comment;
        TextView commentator;
        FrameLayout levelLayout;
        TextView author;
        TextView publisherLevel;
        TextView namelevel;
        ImageView borderImage;
        CircleImageView image;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.description);
            commentator = itemView.findViewById(R.id.author);
            levelLayout = itemView.findViewById(R.id.showLevel);
            author = itemView.findViewById(R.id.name);
            publisherLevel = itemView.findViewById(R.id.level);
            namelevel = itemView.findViewById(R.id.nameLevel);
            borderImage = itemView.findViewById(R.id.imageLevel);
            image = itemView.findViewById(R.id.image);
        }
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_new_description, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemComments item = items.get(position);

        holder.levelLayout.setVisibility(View.GONE);

        holder.comment.setText(item.getComment());
        holder.commentator.setText(item.getNameCommentator());
        Level level = new Level();
        LudificationCommunication persistance = new LudificationCommunication();
        UserCommunication persistanceuser = new UserCommunication();

        holder.commentator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.levelLayout.getVisibility() == View.GONE) {
                    holder.author.setText(item.getNameCommentator());
                    persistance.getPublisherLevel(item.getIdUSer(), new LudificationCommunication.getPublisherLevel() {
                        @Override
                        public void onComplete(String leveli) {
                            holder.publisherLevel.setText(leveli);

                            double lvDouble = Double.parseDouble(holder.publisherLevel.getText().toString());
                            int lv = Double.valueOf(lvDouble).intValue();
                            holder.namelevel.setText(level.levelName(lv));

                            persistanceuser.getProfilePicture(item.getIdUSer(), new UserCommunication.GetUriUser() {
                                @Override
                                public void onComplete(String uri) {
                                    if (!Objects.equals(uri, "")) {
                                        Glide.with(context).load(uri).into(holder.image);
                                    } else {
                                        holder.image.setImageResource(R.drawable.im_logo_ceres);
                                    }
                                }
                            });

                            if (lv >= 0 && lv < 10) {
                                holder.borderImage.setImageResource(R.drawable.im_level_1);
                            } else if (lv >= 10 && lv < 30) {
                                holder.borderImage.setImageResource(R.drawable.im_level_2);
                            } else if (lv >= 30 && lv < 60) {
                                holder.borderImage.setImageResource(R.drawable.im_level_3);
                            } else if (lv >= 60 && lv < 100) {
                                holder.borderImage.setImageResource(R.drawable.im_level_4);
                            } else if (lv >= 100) {
                                holder.borderImage.setImageResource(R.drawable.im_level_5);
                            }

                            Animation fadeIn = new AlphaAnimation(0, 1);
                            fadeIn.setInterpolator(new DecelerateInterpolator());
                            fadeIn.setDuration(900);

                            holder.levelLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.levelLayout.setVisibility(View.VISIBLE);
                                    holder.levelLayout.startAnimation(fadeIn);
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
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.levelLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    holder.levelLayout.startAnimation(fadeOut);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}



