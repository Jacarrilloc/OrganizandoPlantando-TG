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
import com.example.opcv.model.items.ItemGardenHomeList;
import com.example.opcv.business.persistance.garden.GardenPersistance;

import java.util.List;

public class GardenListAdapter extends ArrayAdapter<ItemGardenHomeList> {

    private TextView gardenName;
    private ImageView image, arrow;
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
        image.setVisibility(View.VISIBLE);
        GardenPersistance persistance = new GardenPersistance();
        String id = item.getIdGarden();

        if(item.getUri() != null){
            image = convertView.findViewById(R.id.garden_imagen_list_item);
            Glide.with(context).load(item.getUri()).into(image);
            image.setVisibility(View.VISIBLE);
        }
        else{
            image = convertView.findViewById(R.id.garden_imagen_list_item);
            image.setVisibility(View.VISIBLE);
        }
        /*
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Glide.with(image.getContext())
                        .load(imageRef)
                        .into(image);
                //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //image.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                image.setImageResource(R.drawable.im_logo_ceres_green);
            }
        });*/
        return convertView;
    }
}