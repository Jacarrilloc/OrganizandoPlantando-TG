package com.example.opcv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opcv.R;
import com.example.opcv.fbComunication.CollaboratorUtilities;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.example.opcv.item_list.ItemComments;

import java.util.List;

public class CommentsAdapter extends ArrayAdapter<ItemComments> {

    private Context context;
    private TextView comment, commentator;

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
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);

        ItemComments IC = new ItemComments(item.getComment(), item.getNameCommentator());
        commentator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hacer logica para cuando se da click al nombre del autor
            }
        });

        return convertView;
    }
}
