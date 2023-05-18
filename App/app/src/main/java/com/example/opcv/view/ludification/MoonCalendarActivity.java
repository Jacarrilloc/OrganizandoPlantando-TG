package com.example.opcv.view.ludification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.R;
import com.example.opcv.business.ludification.MoonCalendar;
import com.example.opcv.model.persistance.firebase.AuthCommunication;
import com.example.opcv.view.auth.EditUserActivity;
import com.example.opcv.view.base.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MoonCalendarActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private CalendarView calendarView;
    private TextView moonPhaseTextView, moonPhaseTittle;
    private ImageView phaseMoon;
    private Button profile, myGardens, rewards, ludification;
    private FloatingActionButton back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moon_calendar);

        calendarView = findViewById(R.id.calendarView);
        moonPhaseTextView = findViewById(R.id.moon_phase_textview);
        phaseMoon = findViewById(R.id.moonPhase);
        moonPhaseTittle = findViewById(R.id.moonphaseTittle);
        profile = (Button) findViewById(R.id.profile);
        myGardens = (Button) findViewById(R.id.myGardens);
        rewards = (Button) findViewById(R.id.rewards);
        ludification = (Button) findViewById(R.id.ludification);
        back = (FloatingActionButton) findViewById(R.id.returnArrowButtonToHome);

        MoonCalendar moonCalendar = new MoonCalendar();

        //Fecha actual
        Date currentDate = new Date();
        double moonPhase = MoonCalendar.getMoonPhase(currentDate);
        String moonPhaseDescription = moonCalendar.interpretMoonPhase(moonPhase);
        showPhase(moonPhaseDescription);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Date selectedDate = calendar.getTime();
                double moonPhase = MoonCalendar.getMoonPhase(selectedDate);
                String moonPhaseDescription = moonCalendar.interpretMoonPhase(moonPhase);
                showPhase(moonPhaseDescription);
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoonCalendarActivity.this, HomeActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(MoonCalendarActivity.this, EditUserActivity.class);
                AuthCommunication auth = new AuthCommunication();
                String userId = auth.getCurrentUserUid();
                edit.putExtra("userInfo", userId);
                startActivity(edit);

            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoonCalendarActivity.this, RewardHomeActivity.class));
            }
        });
        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    Intent edit = new Intent(MoonCalendarActivity.this, DictionaryHomeActivity.class);
                    startActivity(edit);
                }
                else{
                    Toast.makeText(MoonCalendarActivity.this, "Para acceder necesitas conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @SuppressLint("SetTextI18n")
    public void showPhase(String phase){
        if(Objects.equals(phase, "Luna nueva")){
            phaseMoon.setImageResource(R.drawable.im_new_moon);
            moonPhaseTittle.setText("Es fase de Luna nueva y puedes:");
            moonPhaseTextView.setText("Cobertura de cultivo con tierra.\n" +
                    "Abonar.\n" +
                    "Eliminar las malas hierbas.\n" +
                    "Quitar las hojas marchitas.\n" +
                    "Sembrar pasto.");
        } else if (Objects.equals(phase, "Luna llena")) {
            phaseMoon.setImageResource(R.drawable.im_full_moon);
            moonPhaseTittle.setText("Es fase de Luna llena y puedes:");
            moonPhaseTextView.setText("Podar.\n" +
                    "Plantar especies perennes.\n" +
                    "Trasplantar.\n" +
                    "Propagación vegetativa.");
        } else if (Objects.equals(phase, "Cuarto creciente")) {
            phaseMoon.setImageResource(R.drawable.im_first_quarter);
            moonPhaseTittle.setText("Es fase de Cuarto creciente y puedes:");
            moonPhaseTextView.setText("Podar árboles enfermos o frutales.\n" +
                    "Cultivar suelos arenosos.\n" +
                    "Sembrar flores, hortalizas de hoja.\n" +
                    "Injerto.\n" +
                    "Evite regar las plantas con flores.");
        } else if (Objects.equals(phase, "Cuarto menguante")) {
            phaseMoon.setImageResource(R.drawable.im_third_quarter);
            moonPhaseTittle.setText("Es fase de Cuarto menguante y puedes:");
            moonPhaseTextView.setText("Sembrar tubérculos.\n" +
                    "Quitar las hojas marchitas.\n" +
                    "Regar plantas con flores.\n" +
                    "Abonar.\n" +
                    "Planta árboles de hoja larga.");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}