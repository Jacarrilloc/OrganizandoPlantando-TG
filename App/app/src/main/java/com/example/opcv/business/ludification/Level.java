package com.example.opcv.business.ludification;

import android.content.Context;
import android.widget.Toast;

import com.example.opcv.notifications.Notifications;
import com.example.opcv.business.persistance.firebase.LudificationCommunication;

import java.util.HashMap;
import java.util.Map;

public class Level {
    final static int levelComment =3;
    final static int levelPublish =7;
    final static int levelDislikes =1;

    public void addLevel(String idUser, Boolean gains, Context context, String element){//va a recibir una variable booleana si es true es que realizo una publicacion de lo contrario es un comentario
        LudificationCommunication persistance = new LudificationCommunication();
        String translate="";
        if(element.equals("Tools")){
            translate = "Herramienta";
        }
        else if(element.equals("Plants")){
            translate = "Planta";
        }
        Map<String, Object> map = new HashMap<>();
        if(gains){
            map.put("Level", levelPublish);
            persistance.addLevelUser(idUser, map);
            Toast.makeText(context, "Has ganado puntos .Felicidades! Ganaste 7 puntos por crear tu "+translate, Toast.LENGTH_SHORT).show();
            Notifications notifications = new Notifications();
            notifications.notification("Has ganado puntos", "Felicidades! Ganaste 7 puntos por crear tu "+translate, context);
        }
        else {
            map.put("Level", levelComment);
            persistance.addLevelUser(idUser, map);
            Toast.makeText(context, "Felicidades! Ganaste 3 puntos por hacer un comentario.", Toast.LENGTH_SHORT).show();
            Notifications notifications = new Notifications();
            notifications.notification("Has ganado puntos", "Felicidades! Ganaste 3 puntos por hacer un comentario.", context);
        }
    }
    public void deductLevel(String docRef, String element){//se llama en el boton de dar dislike
        LudificationCommunication persistance = new LudificationCommunication();
        persistance.deductUserPoints(docRef, levelDislikes, element);
    }

    public String levelName(int lv){
        if(lv >= 0 && lv<10){
            return "Aprendiz Verde";
        } else if (lv>= 10 && lv <30) {
            return "Jardinero(a) Novato(a)";
        } else if (lv>=30 && lv <60) {
            return "Maestro(a) de Jardinería";
        } else if (lv >= 60 && lv <100) {
            return "Genio Botánico(a)";
        } else if (lv >= 100) {
            return "Señor(a) de las Plantas";
        }
        return "Error";
    }
}
