package com.example.opcv.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.opcv.R;
import com.example.opcv.item_list.ItemGardenHomeList;
import com.example.opcv.item_list.ItemShowGardenAvailable;

import java.util.List;

public class GardenAvailableListAdapter extends ArrayAdapter<ItemShowGardenAvailable> {

    private TextView gardenName;
    private ImageView image;
    private Context context;
    private Button accept, deny;

    public GardenAvailableListAdapter(Context context, List<ItemShowGardenAvailable> items) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_garden_available_list_adapter, parent, false);
        }

        ItemShowGardenAvailable item = getItem(position);

        gardenName = convertView.findViewById(R.id.gardenName);
        gardenName.setText(item.getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);

        accept = convertView.findViewById(R.id.acceptedButton);
        deny = convertView.findViewById(R.id.rejectedButton);
        image = convertView.findViewById(R.id.gardenImage);
        image.setVisibility(View.VISIBLE);

        return convertView;

    }
}