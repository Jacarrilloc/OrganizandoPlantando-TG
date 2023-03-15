package com.example.opcv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.fbComunication.CollaboratorUtilities;
import com.example.opcv.item_list.ItemCollaborator;

import java.util.List;

public class GardenMembersAdapter extends ArrayAdapter<ItemCollaborator> {

    private TextView userName;
    private ImageView image;
    private Context context;
    private Button delete;

    public GardenMembersAdapter(Context context, List<ItemCollaborator> items) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_garden_members_adapter, parent, false);
        }

        ItemCollaborator item = getItem(position);

        userName = convertView.findViewById(R.id.userName);
        userName.setText(item.getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);
        ItemCollaborator ICR = new ItemCollaborator(item.getName(), item.getIdUser(), item.getIdGarden());
        CollaboratorUtilities CRU = new CollaboratorUtilities();
        CollaboratorUtilities CRU2 = new CollaboratorUtilities();
        delete = convertView.findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(item);
                CRU.deleteUser(ICR.getIdUser(), ICR.getIdGarden(), context);
                CRU2.deleteGarden(ICR.getIdUser(), ICR.getIdGarden());
                notifyDataSetChanged();

            }
        });

        image = convertView.findViewById(R.id.UserImage);
        image.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        return convertView;
    }
}