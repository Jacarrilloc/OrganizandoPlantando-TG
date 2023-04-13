package com.example.opcv.ludification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.opcv.R;

public class DictionaryHome extends AppCompatActivity {

    private ImageButton plants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_home);

        plants = (ImageButton) findViewById(R.id.plants);

        plants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DictionaryHome.this, ShowDictionaryActivity.class));
            }
        });
    }
}