package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.opcv.adapter.FormsListAdapter;

import java.util.Arrays;


public class GardenForms extends AppCompatActivity {

    private ListView gardenFormsList;
    private String[] itemsList;
    private Animation animSlideUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_adapter);

        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        gardenFormsList = findViewById(R.id.gardenFormsList);
        itemsList = getResources().getStringArray(R.array.list_forms);
        FormsListAdapter adapter = new FormsListAdapter(this,R.layout.item_list_forms_adapter, Arrays.asList(itemsList));
        gardenFormsList.setDividerHeight(5);
        gardenFormsList.setAdapter(adapter);

        gardenFormsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object selectedItem = adapterView.getItemAtPosition(i);
            }
        });
    }
}