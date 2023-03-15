package com.example.opcv.gardens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.opcv.R;
import com.example.opcv.formsScreen.Form_RAC;

public class WhatsappActivity extends AppCompatActivity {
    private Button addLink, returnButton;
    private EditText editLinkText;
    private String idGarden, id, garden, groupLink;
    private String owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp);
        addLink = (Button) findViewById(R.id.addLinkbutton);
        returnButton = (Button) findViewById(R.id.returnButtonLink);
        editLinkText = (EditText) findViewById(R.id.groupLInk);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("ID");
            garden = extras.getString("gardenName");
            idGarden = extras.getString("idGarden");
            groupLink = extras.getString("GroupLink");
            owner = "true";
        }

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = editLinkText.getText().toString();
                Intent newForm = new Intent(WhatsappActivity.this, huertaActivity.class);
                newForm.putExtra("ID", id);
                newForm.putExtra("idGarden", idGarden);
                newForm.putExtra("gardenName", garden);
                //newForm.putExtra("infoGarden", infoGarden);
                newForm.putExtra("owner", owner);
                newForm.putExtra("GroupLink", link);
                startActivity(newForm);
                finish();
            }
        });
    }
}