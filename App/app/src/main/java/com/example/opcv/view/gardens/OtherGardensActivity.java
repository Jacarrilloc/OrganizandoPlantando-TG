package com.example.opcv.view.gardens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.business.persistance.firebase.UserCommunication;
import com.example.opcv.view.auth.SignOffActivity;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.business.persistance.firebase.CollaboratorCommunication;
import com.example.opcv.model.entity.GardenInfo;
import com.example.opcv.view.ludification.DictionaryHomeActivity;
import com.example.opcv.view.ludification.RewardHomeActivity;
import com.example.opcv.view.ludification.ShowDictionaryItemActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class OtherGardensActivity extends AppCompatActivity {
    private Button rewards, profile, myGardens, join, visit, ludification;
    private TextView nameGarden,descriptionGarden;
    private FirebaseFirestore database;
    private CollectionReference gardensRef;
    private String gardenID, garden, infoGarden, id;
    private FloatingActionButton returnButton;
    private ImageView image;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isAnonymous()) {
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_gardens);

        join= (Button) findViewById(R.id.requesteJoin);
        nameGarden = (TextView) findViewById(R.id.gardenNameText);
        descriptionGarden = (TextView) findViewById(R.id.descriptionGarden);
        returnButton = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);
        image = (ImageView) findViewById(R.id.gardenProfilePicture);
        rewards = (Button) findViewById(R.id.rewards);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        ludification = (Button) findViewById(R.id.ludification);
        database = FirebaseFirestore.getInstance();
        gardensRef = database.collection("Gardens");
        AuthCommunication authCommunication = new AuthCommunication();
        FirebaseUser user = authCommunication.guestUser();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("ID");
            garden = extras.getString("gardenName");
            gardenID = extras.getString("idGarden");
            SearchInfoGardenSreen(id,garden);
        }
        getImageGarden(gardenID);
        UserCommunication communication = new UserCommunication();


        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null && !user.isAnonymous()){
                    startActivity(new Intent(OtherGardensActivity.this, RewardHomeActivity.class));
                }
                else{
                    Toast.makeText(OtherGardensActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null && !user.isAnonymous()){
                    startActivity(new Intent(OtherGardensActivity.this, EditUserActivity.class));
                }
                else{
                    startActivity(new Intent(OtherGardensActivity.this, SignOffActivity.class));
                }
            }
        });
        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OtherGardensActivity.this, HomeActivity.class));
            }
        });


        communication.userAlreadyRequested(id, gardenID, new UserCommunication.GetUserRequest() {
            @Override
            public void onComplete(Boolean response) {
                //si es true significa que el usuario ya hizo el request y toca inhabilitar el boton
                if(response){
                    join.setClickable(false);
                    join.setVisibility(View.INVISIBLE);
                    join.setEnabled(false);
                }
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null && !user.isAnonymous()){
                    CollaboratorCommunication cU = new CollaboratorCommunication();
                    //Para que si le da click a unirse ya ese boton quede deshabilitado
                    Map<String, Object> request = new HashMap<>();
                    request.put("Garden", gardenID);
                    communication.addUserRequests(id, request);

                    cU.addRequests(OtherGardensActivity.this, id, gardenID);
                    Toast.makeText(OtherGardensActivity.this, "Se envio la solicitud al dueño de la huerta", Toast.LENGTH_SHORT).show();
                    join.setVisibility(View.INVISIBLE);
                    join.setClickable(false);
                }
                else{
                    Toast.makeText(OtherGardensActivity.this, "No tienes permiso para usar esto. Crea una cuenta para interactuar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(OtherGardensActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(OtherGardensActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getImageGarden(String idGarden){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String imageName = idGarden + ".jpg";
        StorageReference imageRef = storageRef.child("gardenMainPhoto/" + imageName);
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                image.setImageResource(R.drawable.im_logo_ceres_green);
            }
        });
    }
    private void SearchInfoGardenSreen(String idUser,String name){
        DocumentReference ref = gardensRef.document(gardenID);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String infoDoc=null;
                if (documentSnapshot.exists()) {

                    String typeDoc = documentSnapshot.getString("GardenType");
                    infoDoc = documentSnapshot.getString("InfoGarden");
                    GardenInfo gardenInfo = new GardenInfo(idUser,name,infoDoc,typeDoc);

                    fillSreen(gardenInfo);
                }
                /*else {
                    System.out.println("Se genero error al mostrar la información");
                }*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Se genero error: ", e);
            }
        });

    }

    private void fillSreen(GardenInfo gardenInfo){
        nameGarden.setText(gardenInfo.getName());
        descriptionGarden.setText(gardenInfo.getInfo());
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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

}