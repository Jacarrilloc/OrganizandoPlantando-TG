package com.example.opcv.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.opcv.R;
import com.example.opcv.model.items.ItemCollaboratorsRequest;

import java.util.List;

public class MyCollaborationsListAdapter extends ArrayAdapter<ItemCollaboratorsRequest> {
    private TextView userName;
    private ImageView image;
    private Context context;

    public MyCollaborationsListAdapter(Context context, List<ItemCollaboratorsRequest> items) {
        super(context, 0, items);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_my_collaboration_gardens, parent, false);
        }

        ItemCollaboratorsRequest item = getItem(position);

        userName = convertView.findViewById(R.id.garden_name_list_item);
        userName.setText(item.getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);
        if(item.getUri() != null){
            image = convertView.findViewById(R.id.garden_imagen_list_item);
            Glide.with(context).load(item.getUri()).into(image);
            image.setVisibility(View.VISIBLE);
        }
        else{
            image = convertView.findViewById(R.id.garden_imagen_list_item);
            image.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}