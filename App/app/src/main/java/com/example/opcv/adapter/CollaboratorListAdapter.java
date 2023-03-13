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
import android.widget.ImageView;
import android.widget.TextView;
import com.example.opcv.R;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.example.opcv.item_list.ItemGardenHomeList;
import com.example.opcv.item_list.ItemShowGardenAvailable;

import java.util.List;

public class CollaboratorListAdapter extends ArrayAdapter<ItemCollaboratorsRequest> {

    private TextView userName;
    private ImageView image;
    private Context context;

    public CollaboratorListAdapter(Context context, List<ItemCollaboratorsRequest> items) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_collaborator_adapter, parent, false);
        }

        ItemCollaboratorsRequest item = getItem(position);

        userName = convertView.findViewById(R.id.userName);
        userName.setText(item.getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);

        image = convertView.findViewById(R.id.UserImage);
        image.setVisibility(View.VISIBLE);

        return convertView;

    }
}