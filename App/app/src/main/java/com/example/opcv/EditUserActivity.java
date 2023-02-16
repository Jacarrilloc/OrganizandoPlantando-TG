package com.example.opcv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditUserActivity extends AppCompatActivity {
    private Button signOff, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        signOff = (Button) findViewById(R.id.options);
        signOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, signOffActivity.class));
            }
        });

        delete = (Button) findViewById(R.id.options3);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, deleteAccountActivity.class));
            }
        });

    }
}