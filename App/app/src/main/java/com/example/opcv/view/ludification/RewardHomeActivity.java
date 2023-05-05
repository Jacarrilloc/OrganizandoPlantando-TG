package com.example.opcv.view.ludification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.opcv.R;
import com.example.opcv.business.ludification.Level;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.business.persistance.firebase.LudificationCommunication;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.base.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RewardHomeActivity extends AppCompatActivity {
    private Button profile, myGardens, rewards, ludification;
    private String idUser;
    private ImageButton reward1, reward2, reward3, reward4, reward5;
    private int level;

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
        setContentView(R.layout.activity_reward_home);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        reward1 = (ImageButton) findViewById(R.id.reward1);
        reward2 = (ImageButton) findViewById(R.id.reward2);
        reward3 = (ImageButton) findViewById(R.id.reward3);
        reward4 = (ImageButton) findViewById(R.id.reward4);
        reward5 = (ImageButton) findViewById(R.id.reward5);
        AuthCommunication auth = new AuthCommunication();
        Level levelLogic = new Level();
        idUser = auth.getCurrentUserUid();
        LudificationCommunication ludi = new LudificationCommunication();
        ludi.getPublisherLevel(idUser, new LudificationCommunication.getPublisherLevel() {
            @Override
            public void onComplete(String name) {
                level = Integer.parseInt(name);
            }
        });

        //se deja listo para poner lo que se desbloquea
        reward1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Puedes entrar");
            }
        });

        reward2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelLogic.checkLevel(level, "Reward2")){
                    System.out.println("Puedes entrar");
                }
                else{
                    System.out.println("No Puedes entrar");
                }
            }
        });

        reward3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelLogic.checkLevel(level, "Reward3")){
                    System.out.println("Puedes entrar");
                }
                else{
                    System.out.println("No Puedes entrar");
                }
            }
        });

        reward4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelLogic.checkLevel(level, "Reward4")){
                    System.out.println("Puedes entrar");
                }
                else{
                    System.out.println("No Puedes entrar");
                }
            }
        });

        reward5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(levelLogic.checkLevel(level, "Reward5")){
                    System.out.println("Puedes entrar");
                }
                else{
                    System.out.println("No Puedes entrar");
                }
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RewardHomeActivity.this, HomeActivity.class));
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(RewardHomeActivity.this, EditUserActivity.class);
                AuthCommunication auth = new AuthCommunication();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RewardHomeActivity.this, RewardHomeActivity.class));
            }
        });
        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(RewardHomeActivity.this, DictionaryHomeActivity.class);
                startActivity(edit);
            }
        });
    }
}