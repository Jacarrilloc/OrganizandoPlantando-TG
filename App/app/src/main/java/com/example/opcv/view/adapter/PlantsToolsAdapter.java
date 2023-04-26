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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.opcv.R;
import com.example.opcv.model.items.ItemPlantsTools;

import java.util.List;
import java.util.Objects;

public class PlantsToolsAdapter extends ArrayAdapter<ItemPlantsTools> {

    private Context context;
    private TextView name;
    private ImageView image;
    private String element;


    public PlantsToolsAdapter(Context context, List<ItemPlantsTools> items, String element) {
        super(context, 0, items);
        this.context = context;
        this.element = element;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (Objects.equals(element, "Plants")) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_plant_description, parent, false);
            }
        } else if (Objects.equals(element, "Tools")) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_tool_dictionary, parent, false);
            }
        }

        ItemPlantsTools item = getItem(position);
        String id = item.getId();
        name = convertView.findViewById(R.id.name);
        name.setText(item.getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);
        //ItemPlantsTools IPT = new ItemPlantsTools(item.getName(), item.getId());
        if(item.getUri() != null){
            image = convertView.findViewById(R.id.image);
            Glide.with(context).load(item.getUri()).into(image);
            image.setVisibility(View.VISIBLE);
        }
        else{
            image = convertView.findViewById(R.id.image);
            image.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}