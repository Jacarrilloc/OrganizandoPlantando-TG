package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FormsActivity extends AppCompatActivity {

    private TextView formsName;

    private FloatingActionButton backButtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);

        formsName = findViewById(R.id.formsNameElement);
        backButtom = findViewById(R.id.returnArrowButtonFormsToGarden);

        String name = getIntent().getStringExtra("Name");
        formsName.setText(name);

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}