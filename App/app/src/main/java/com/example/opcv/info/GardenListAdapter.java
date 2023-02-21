package com.example.opcv.info;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.opcv.EditUserActivity;
import com.example.opcv.NewToAppActivity;
import com.example.opcv.R;
import com.example.opcv.deleteAccountActivity;
import com.example.opcv.HomeActivity;
import com.example.opcv.huertaActivity;

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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = item.getName();
                Intent intent =  new Intent(view.getContext(), huertaActivity.class);
                intent.putExtra("name", nombre);
                view.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

}
