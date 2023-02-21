package com.example.opcv.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.opcv.R;

import java.util.List;

public class GardenListAdapter extends ArrayAdapter<ItemGardenHomeList> {

    private TextView gardenName;
    private ImageView image;

    public GardenListAdapter(Context context, List<ItemGardenHomeList> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_garden_layout, parent, false);
        }

        ItemGardenHomeList item = getItem(position);

        gardenName = convertView.findViewById(R.id.garden_name_list_item);
        gardenName.setText(item.getName());

        image = convertView.findViewById(R.id.garden_imagen_list_item);

        return convertView;
    }
}
