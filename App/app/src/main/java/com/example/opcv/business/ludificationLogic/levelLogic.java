package com.example.opcv.business.ludificationLogic;

import android.content.Context;
import android.widget.Toast;

import com.example.opcv.persistance.ludificationPersistance.LudificationPersistance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class levelLogic {
    final static int levelComment =3;
    final static int levelPublish =7;
    final static int levelDislikes =1;

    public void addLevel(String idUser, Boolean gains, Context context){//va a recibir una variable booleana si es true es que realizo una publicacion de lo contrario es un comentario
        LudificationPersistance persistance = new LudificationPersistance();
        Map<String, Object> map = new HashMap<>();
        if(gains){
            map.put("Level", levelPublish);
            persistance.addLevelUser(idUser, map);
            Toast.makeText(context, "Has subido "+levelPublish+" niveles, felicidades!", Toast.LENGTH_LONG).show();
        }
        else {
            map.put("Level", levelComment);
            persistance.addLevelUser(idUser, map);
            Toast.makeText(context, "Has subido "+levelComment+" niveles, felicidades!", Toast.LENGTH_LONG).show();
        }
    }
    public void deductLevel(String docRef, String element){//se llama en el boton de dar dislike
        LudificationPersistance persistance = new LudificationPersistance();
        persistance.deductUserPoints(docRef, levelDislikes, element);
    }
}