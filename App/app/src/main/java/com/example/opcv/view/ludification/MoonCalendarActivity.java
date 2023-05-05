package com.example.opcv.view.ludification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.opcv.R;
import com.example.opcv.business.ludification.MoonCalendar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MoonCalendarActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private CalendarView calendarView;
    private TextView moonPhaseTextView, moonPhaseTittle;
    private ImageView phaseMoon;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moon_calendar);

        calendarView = findViewById(R.id.calendarView);
        moonPhaseTextView = findViewById(R.id.moon_phase_textview);
        phaseMoon = findViewById(R.id.moonPhase);
        moonPhaseTittle = findViewById(R.id.moonphaseTittle);

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
}