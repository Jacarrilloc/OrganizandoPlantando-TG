package com.example.opcv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.opcv.R;

import java.util.List;

public class FormsListAdapter extends ArrayAdapter<String> {

    private TextView formName;

    public FormsListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_list_forms_adapter, parent, false);
        }
        formName = (TextView) view.findViewById(R.id.textForms);
        String text = getItem(position);
        formName.setText(text);
        return view;
    }
}
