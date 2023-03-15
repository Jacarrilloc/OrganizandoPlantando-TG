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
import com.example.opcv.item_list.ItemCollaboratorsRequest;

import java.util.List;

public class CollaboratorListAdapter extends ArrayAdapter<ItemCollaboratorsRequest> {

    private TextView userName;
    private ImageView image;
    private Context context;
    private Button accept, deny;

    public CollaboratorListAdapter(Context context, List<ItemCollaboratorsRequest> items) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_collaborator_adapter, parent, false);
        }

        ItemCollaboratorsRequest item = getItem(position);

        userName = convertView.findViewById(R.id.userName);
        userName.setText(item.getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_to_left);
        animation.setStartOffset(position * 100);
        convertView.startAnimation(animation);
        ItemCollaboratorsRequest ICR = new ItemCollaboratorsRequest(item.getName(), item.getIdUser(), item.getIdGarden());
        CollaboratorUtilities CRU = new CollaboratorUtilities();
        accept = convertView.findViewById(R.id.acceptedButton);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CRU.acceptRequest(ICR.getIdUser(), ICR.getIdGarden(), true);
                remove(item);
                notifyDataSetChanged();
                Toast.makeText(context, "Se aceptó con éxito al usuario como colaborador", Toast.LENGTH_SHORT).show();
            }
        });
        deny = convertView.findViewById(R.id.rejectedButton);
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CRU.acceptRequest(ICR.getIdUser(), ICR.getIdGarden(), false);
                remove(item);
                notifyDataSetChanged();
                Toast.makeText(context, "Se rechazó con éxito al usuario ", Toast.LENGTH_SHORT).show();
            }
        });

        image = convertView.findViewById(R.id.UserImage);
        image.setVisibility(View.VISIBLE);
        return convertView;
    }
}