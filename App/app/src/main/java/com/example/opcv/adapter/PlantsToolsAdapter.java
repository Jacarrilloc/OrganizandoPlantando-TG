package com.example.opcv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opcv.R;
import com.example.opcv.item_list.ItemPlantsTools;

import java.util.HashMap;
import java.util.List;

public class PlantsToolsAdapter extends ArrayAdapter<ItemPlantsTools> {

    private Context context;
    private TextView name;
    private ImageView image;


    public PlantsToolsAdapter(Context context, List<ItemPlantsTools> items) {
        super(context, 0, items);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tool_dictionary, parent, false);
        }

        ItemPlantsTools item = getItem(position);
        String id = item.getId();
        name = convertView.findViewById(R.id.name);
        name.setText(item.getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);
        ItemPlantsTools IPT = new ItemPlantsTools(item.getName(), item.getId());

        image = convertView.findViewById(R.id.image);
        image.setVisibility(View.VISIBLE);
        return convertView;
    }
}
