package com.example.opcv.view.ludification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.business.ludification.Level;
import com.example.opcv.business.persistance.firebase.AuthCommunication;
import com.example.opcv.business.persistance.firebase.UserCommunication;
import com.example.opcv.business.persistance.firebase.LudificationCommunication;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.base.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RewardHomeActivity extends AppCompatActivity {

    private Button profile, myGardens, rewards, ludification, lvl1, lvl2, lvl3, lvl4, lvl5;
    int lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_home);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        lvl1 = (Button) findViewById(R.id.level1);
        lvl2 = (Button) findViewById(R.id.level2);
        lvl3 = (Button) findViewById(R.id.level3);
        lvl4 = (Button) findViewById(R.id.level4);
        lvl5 = (Button) findViewById(R.id.level5);

        AuthCommunication auth = new AuthCommunication();
        String user = auth.getCurrentUserUid();

        lvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Hola");
            }
        });

        lvl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hola");
            }
        });

        lvl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hola");
            }
        });

        lvl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hola");
            }
        });

        lvl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moon = new Intent(RewardHomeActivity.this, MoonCalendarActivity.class);
                startActivity(moon);
            }
        });
        UserCommunication persistance = new UserCommunication();
        persistance.getUserLevel(user, new UserCommunication.GetUserLvl() {
            @Override
            public void onComplete(String leveli) {

                double lvDouble = Double.parseDouble(leveli);
                lv = Double.valueOf(lvDouble).intValue();
                initializeButtons(lv);

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
                if(isOnline()){
                    Intent edit = new Intent(RewardHomeActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(RewardHomeActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void initializeButtons(int lv) {

        /*
        if (lv >=0 && lv <10){
            lvl1.setVisibility(View.VISIBLE);
            lvl1.setClickable(true);
        }else if (lv>= 10 && lv <30) {
            lvl1.setVisibility(View.VISIBLE);
            lvl1.setClickable(true);
            lvl2.setVisibility(View.VISIBLE);
            lvl2.setClickable(true);
        } else if (lv>=30 && lv <60) {
            lvl1.setVisibility(View.VISIBLE);
            lvl1.setClickable(true);
            lvl2.setVisibility(View.VISIBLE);
            lvl2.setClickable(true);
            lvl3.setVisibility(View.VISIBLE);
            lvl3.setClickable(true);
        } else if (lv >= 60 && lv <100) {
            lvl1.setVisibility(View.VISIBLE);
            lvl1.setClickable(true);
            lvl2.setVisibility(View.VISIBLE);
            lvl2.setClickable(true);
            lvl3.setVisibility(View.VISIBLE);
            lvl3.setClickable(true);
            lvl4.setVisibility(View.VISIBLE);
            lvl4.setClickable(true);
        } else if (lv>=100) {
            lvl1.setVisibility(View.VISIBLE);
            lvl1.setClickable(true);
            lvl2.setVisibility(View.VISIBLE);
            lvl2.setClickable(true);
            lvl3.setVisibility(View.VISIBLE);
            lvl3.setClickable(true);
            lvl4.setVisibility(View.VISIBLE);
            lvl4.setClickable(true);
            lvl5.setVisibility(View.VISIBLE);
            lvl5.setClickable(true);
        }*/
    }
}