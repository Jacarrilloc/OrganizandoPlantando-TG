package com.example.opcv.business.ludification;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.example.opcv.business.persistance.firebase.LudificationCommunication;

import java.util.HashMap;
import java.util.Map;

public class Ludification {

    public void addPlantElementsMap(String name, String description, Boolean flower, Boolean fruit, Boolean edible, Boolean medicine, Boolean petFriendly, Boolean precaution, Context context, String idUser, byte[] bytes){
        LudificationCommunication persistance = new LudificationCommunication();
        Map<String, Object> plantInfo = new HashMap<>();
        persistance.addPhoto(name, bytes, new LudificationCommunication.GetURi() {
            @Override
            public void onSuccess(String uri) {
                plantInfo.put("PlantName", name);
                plantInfo.put("GivesFruit", fruit);
                plantInfo.put("GivesFlower", flower);
                plantInfo.put("Edible", edible);
                plantInfo.put("Medicinal", medicine);
                plantInfo.put("PetFriendly", petFriendly);
                plantInfo.put("Precaution", precaution);
                plantInfo.put("PlantDescription", description);
                plantInfo.put("DisLikes", 0);
                plantInfo.put("Likes", 0);
                plantInfo.put("Publisher", idUser);
                plantInfo.put("PlantImage", uri);
                persistance.addPlantDictionary(plantInfo, context, idUser);
            }
        });


    }

    public void addToolElementsMap(String name, String description, Boolean tool, Boolean fertilizer, Boolean care, Context context, String idUser, byte[] bytes){
        LudificationCommunication persistance = new LudificationCommunication();
        Map<String, Object> toolInfo = new HashMap<>();
        persistance.addPhoto(name, bytes, new LudificationCommunication.GetURi() {
            @Override
            public void onSuccess(String uri) {
                toolInfo.put("ToolName", name);
                toolInfo.put("Tool", tool);
                toolInfo.put("Fertilizer", fertilizer);
                toolInfo.put("Care", care);
                toolInfo.put("ToolDescription", description);
                toolInfo.put("DisLikes", 0);
                toolInfo.put("Likes", 0);
                toolInfo.put("Publisher", idUser);
                toolInfo.put("ToolImage", uri);
                persistance.addToolDictionary(toolInfo, context, idUser);
            }
        });

    }

    public boolean validateField(String name, String description, Context context, byte[] bytes){
        if(name.isEmpty() || description.isEmpty()){
            Toast.makeText(context, "Es necesario Ingresar el nombre y la descripción de la Huerta", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(bytes == null){
            Toast.makeText(context, "Es necesario Ingresar una imagen", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean validatePhoto(Drawable drawable, Context context){
        if(drawable == null){
            Toast.makeText(context, "Es necesario una foto.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void likesDislikes(String docRef, boolean isLike, String element){
        LudificationCommunication persistance = new LudificationCommunication();
        if(isLike){//si es true es porque es de likes
            persistance.addLikesFirebase(docRef, element);
        }
        else{
            persistance.addDislikesFirebase(docRef, element);
        }
    }

    public void addComments(String element, String idPublisher, String comment, String docRef, Context context){
        LudificationCommunication persistance = new LudificationCommunication();
        Map<String, Object> commentsMap = new HashMap<>();
        commentsMap.put("Comment", comment);
        commentsMap.put("PublisherID", idPublisher);
        try{
            persistance.addComments(element, docRef, commentsMap);
            Toast.makeText(context, "Se añadió el comentario con exito.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Ocurrió un error al subir el comentario, intente de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }



}
