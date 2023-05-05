package com.example.opcv.business.ludification;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MoonCalendar {

    public String interpretMoonPhase(double moonPhase) {
        if ((moonPhase >= 0 && moonPhase <= 0.118) || (moonPhase >= 0.9 && moonPhase <= 1)) {
            return "Luna nueva";
        } else if (moonPhase > 0.118 && moonPhase <= 0.382) {
            return "Cuarto creciente";
        } else if (moonPhase > 0.382 && moonPhase <= 0.618) {
            return "Luna llena";
        } else if (moonPhase > 0.618 && moonPhase < 0.9) {
            return "Cuarto menguante";
        } else {
            return "Error";
        }
    }


    public static double getMoonPhase(Date date) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // January = 1
        int day = cal.get(Calendar.DAY_OF_MONTH);
        double jd = dateToJulian(year, month, day) - 2451550.1;
        double t = jd / 29.53058867;
        double tInt = Math.floor(t);
        double tFrac = t - tInt;
        return (tFrac * 29.53058867) / 29.53;
    }

    private static double dateToJulian(int year, int month, int day) {
        if (month <= 2) {
            year -= 1;
            month += 12;
        }
        double a = Math.floor(year / 100.0);
        double b = 2 - a + Math.floor(a / 4.0);
        return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + b - 1524.5;
    }
}
