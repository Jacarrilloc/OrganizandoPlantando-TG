package com.example.opcv.business.ludificationLogic;

import android.content.Context;

import com.example.opcv.persistance.ludificationPersistance.LudificationPersistance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class levelLogic {
    final static int levelComment =3;
    final static int levelPublish =7;
    final static int levelDislikes =1;
    int dislikePlants=0;

    public void addLevel(String idUser, Boolean gains){//va a recibir una variable booleana si es true es que realizo una publicacion de lo contrario es un comentario
        LudificationPersistance persistance = new LudificationPersistance();
        Map<String, Object> map = new HashMap<>();
        if(gains){
            map.put("Level", levelPublish);
            persistance.addLevelUser(idUser, map);
        }
        else {
            map.put("Level", levelComment);
            persistance.addLevelUser(idUser, map);
        }
    }
    public void deductLevel(String idPublisher, String element, String docRef, Context context){//se llama en el boton de dar dislike
        LudificationPersistance persistance = new LudificationPersistance();

    }
}
