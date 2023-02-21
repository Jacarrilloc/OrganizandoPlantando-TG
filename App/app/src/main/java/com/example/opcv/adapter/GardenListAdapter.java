package com.example.opcv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.opcv.R;
import com.example.opcv.item_list.ItemGardenHomeList;

import java.util.List;

public class GardenListAdapter extends ArrayAdapter<ItemGardenHomeList> {

    private TextView gardenName;
    private ImageView image;
    private Context context;

    public GardenListAdapter(Context context, List<ItemGardenHomeList> items) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_garden_layout, parent, false);
        }

        ItemGardenHomeList item = getItem(position);

        gardenName = convertView.findViewById(R.id.garden_name_list_item);
        gardenName.setText(item.getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);

        image = convertView.findViewById(R.id.garden_imagen_list_item);

        return convertView;
    }
}
