package com.example.opcv.business.ludificationLogic;

import android.content.Context;
import android.widget.Toast;

import com.example.opcv.item_list.ItemPlantsTools;
import com.example.opcv.persistance.ludificationPersistance.LudificationPersistance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LudificationLogic {

    public void addPlantElementsMap(String name, String description, Boolean flower, Boolean fruit, Boolean edible, Boolean medicine, Boolean petFriendly, Boolean precaution, Context context, String idUser, byte[] bytes){
        LudificationPersistance persistance = new LudificationPersistance();
        Map<String, Object> plantInfo = new HashMap<>();
        persistance.addPhoto(name, bytes, new LudificationPersistance.GetURi() {
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
        LudificationPersistance persistance = new LudificationPersistance();
        Map<String, Object> toolInfo = new HashMap<>();
        persistance.addPhoto(name, bytes, new LudificationPersistance.GetURi() {
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
    //public boolean validatePhoto(St)


    public void likesDislikes(String docRef, boolean isLike, String element){
        LudificationPersistance persistance = new LudificationPersistance();
        if(isLike){//si es true es porque es de likes
            persistance.addLikesFirebase(docRef, element);
        }
        else{
            persistance.addDislikesFirebase(docRef, element);
        }
    }

    public void addComments(String element, String idPublisher, String comment, String docRef, Context context){
        LudificationPersistance persistance = new LudificationPersistance();
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
