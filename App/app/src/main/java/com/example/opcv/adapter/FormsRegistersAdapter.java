package com.example.opcv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.opcv.R;
import com.example.opcv.fbComunication.FormsUtilities;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.example.opcv.item_list.ItemRegistersList;

import java.util.List;

public class FormsRegistersAdapter extends ArrayAdapter<ItemRegistersList> {
    private TextView dateText;
    private Button seeMoreButton;
    private ImageButton edit, delete;
    private Context context;

    public FormsRegistersAdapter(Context context, List<ItemRegistersList> objects) {
        super(context, 0, objects);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_list_registro_lombricultura, parent, false);
        }
        dateText = (TextView) view.findViewById(R.id.dateRegister);
        seeMoreButton = (Button) view.findViewById(R.id.seeMorButton);
        edit = (ImageButton) view.findViewById(R.id.editButton);
        delete = (ImageButton) view.findViewById(R.id.deleteButton);

        ItemRegistersList item = getItem(position);
        ItemRegistersList IRL = new ItemRegistersList(item.getIdGarden(), item.getFormName(), item.getIdFormCollection());
        FormsUtilities FU = new FormsUtilities();

        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FU.editForms();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dateText.setText(item.getIdGarden());

        view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        if(view != null){
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        return view;
    }
}
