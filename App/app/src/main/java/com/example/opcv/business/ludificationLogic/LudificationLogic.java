package com.example.opcv.business.ludificationLogic;

import android.content.Context;
import android.widget.Toast;

import com.example.opcv.item_list.ItemPlantsTools;
import com.example.opcv.persistance.ludificationPersistance.LudificationPersistance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LudificationLogic {

    public void addPlantElementsMap(String name, String description, Boolean flower, Boolean fruit, Boolean edible, Boolean medicine, Boolean petFriendly, Boolean precaution, Context context, String idUser){
        LudificationPersistance persistance = new LudificationPersistance();
        Map<String, Object> plantInfo = new HashMap<>();
        plantInfo.put("PlantName", name);
        plantInfo.put("GivesFruit", fruit);
        plantInfo.put("GivesFlower", flower);
        plantInfo.put("Edible", edible);
        plantInfo.put("Medicinal", medicine);
        plantInfo.put("PetFriendly", petFriendly);
        plantInfo.put("Precaution", precaution);
        plantInfo.put("PlantDescription", description);
        persistance.addPlantDictionary(plantInfo, context, idUser);
    }

    public void addToolElementsMap(String name, String description, Boolean tool, Boolean fertilizer, Boolean care, Context context, String idUser){
        LudificationPersistance persistance = new LudificationPersistance();
        Map<String, Object> toolInfo = new HashMap<>();
        toolInfo.put("ToolName", name);
        toolInfo.put("Tool", tool);
        toolInfo.put("Fertilizer", fertilizer);
        toolInfo.put("Care", care);
        toolInfo.put("ToolDescription", description);
        persistance.addToolDictionary(toolInfo, context, idUser);
    }

    public boolean validateField(String name, String description, Context context){
        if(name.isEmpty() || description.isEmpty()){
            Toast.makeText(context, "Es necesario Ingresar el nombre y la descripción de la Huerta", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void fillPlantsToolsAvailable(Map<String, String> map){

    }
}
