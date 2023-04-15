package com.example.opcv.persistance.ludificationPersistance;

import android.content.Context;
import android.widget.Toast;

import com.example.opcv.fbComunication.AuthUtilities;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Map;

public class LudificationPersistance implements Serializable {

    public void addPlantDictionary(Map<String, Object> plantInfo, Context context, String idUser){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AuthUtilities auth = new AuthUtilities();
        auth.getUserDocumentId(idUser, new AuthUtilities.GetUserDocument() {
            @Override
            public void onComplete(String idDocu) {
                System.out.println("xd: "+idDocu);
                DocumentReference documentReference = db.collection("UserInfo").document(idDocu);//Esto sera para añadir el nivel al usuario cuando se implemente
            }
        });

        CollectionReference ref = db.collection("Plants");
        try{
            ref.add(plantInfo);
            Toast.makeText(context, "Planta añadida con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Ocurrio un error al subir la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToolDictionary(Map<String, Object> plantInfo, Context context, String idUser){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AuthUtilities auth = new AuthUtilities();
        auth.getUserDocumentId(idUser, new AuthUtilities.GetUserDocument() {
            @Override
            public void onComplete(String idDocu) {
                System.out.println("xd: "+idDocu);
                DocumentReference documentReference = db.collection("UserInfo").document(idDocu);//Esto sera para añadir el nivel al usuario cuando se implemente
            }
        });
        CollectionReference ref = db.collection("Tools");
        try{
            ref.add(plantInfo);
            Toast.makeText(context, "Herramienta añadida con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Ocurrio un error al subir la información", Toast.LENGTH_SHORT).show();
        }
    }
}
