package com.example.opcv.view.forms;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.opcv.R;
import com.example.opcv.view.adapter.FormsRegistersAdapter;
import com.example.opcv.model.items.ItemRegistersList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FormsRegistersActivity extends AppCompatActivity {
    private TextView registerNameText;
    private ListView ListViewRegisters;
    private FloatingActionButton backButtom;
    private String register_name, idGarden;
    private FirebaseFirestore database;
    private FirebaseAuth autentication;
    private Button gardens, myGardens, profile;

    @Override
    protected void onStart() {
        super.onStart();
        fillFormsRegisters();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_registers);

        registerNameText = (TextView) findViewById(R.id.registerName);
        ListViewRegisters = (ListView) findViewById(R.id.ListViewRegisters);
        backButtom = findViewById(R.id.returnArrowButtonFormsToGarden);

        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        register_name = getIntent().getStringExtra("Name");
        idGarden = getIntent().getStringExtra("idGardenFirebase");
        registerNameText.setText(register_name);
    }
    private void fillFormsRegisters(){
        System.out.println("id garden "+idGarden+" name "+register_name);

        CollectionReference Ref = database.collection("Gardens").document(idGarden).collection("Forms");
        Query query = Ref.whereEqualTo("nameForm", register_name);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Se genero error: ", e);
                    return;
                }
                List<ItemRegistersList> formsRequests = new ArrayList<>();
                for(DocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.exists()) {
                        //System.out.println("Se genera"+documentSnapshot.getId());
                        ItemRegistersList newItem = new ItemRegistersList(idGarden, register_name, documentSnapshot.getId(), documentSnapshot.get("Date").toString());//xd
                        formsRequests.add(newItem);
                    }
                }
                fillListGardens(formsRequests);
            }
        });
    }
    private void fillListGardens( List<ItemRegistersList> gardenInfoDocument){
        FormsRegistersAdapter adapter = new FormsRegistersAdapter(this, gardenInfoDocument);
        ListViewRegisters.setAdapter(adapter);
        ListViewRegisters.setDividerHeight(5);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);
        super.attachBaseContext(newBase);
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration config = new Configuration(newConfig);
        adjustFontScale(getApplicationContext(), config);
    }
    public static void adjustFontScale(Context context, Configuration configuration) {
        if (configuration.fontScale != 1) {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}