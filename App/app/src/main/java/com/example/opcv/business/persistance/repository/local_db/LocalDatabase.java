package com.example.opcv.business.persistance.repository.local_db;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class LocalDatabase implements LocalDatabaseI {
    private Context context;

    public LocalDatabase(Context mContext) {
        context = mContext;
    }

    public void createJsonForm(String idGarden, Map<String, Object> infoForm) {
        // El permiso ya está concedido, ejecutamos la tarea
        File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
        if (!gardenDir.exists()) {
            gardenDir.mkdirs();
            Log.i("JSON:", "No existia la carpeta, se crea la carpeta de la huerta en: " + gardenDir.getAbsolutePath());
        }

        File infoFormFile = new File(gardenDir, "infoForm.json");
        try {
            JSONArray jsonArray;
            if (infoFormFile.exists()) {
                // Lee el archivo existente y convierte su contenido en un objeto JSON
                FileInputStream inputStream = new FileInputStream(infoFormFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                inputStream.close();
                String existingContent = stringBuilder.toString();
                if (!existingContent.isEmpty()) {
                    jsonArray = new JSONArray(existingContent);
                } else {
                    jsonArray = new JSONArray();
                }
            } else {
                jsonArray = new JSONArray();
            }

            // Convierte el nuevo objeto a un objeto JSON y lo agrega al arreglo
            JSONObject jsonObj = new JSONObject(infoForm);
            jsonArray.put(jsonObj);

            // Escribe el JSON con formato en el archivo
            FileWriter fileWriter = new FileWriter(infoFormFile);
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            jsonWriter.setIndent("  "); // 2 espacios para la sangría
            jsonWriter.beginArray(); // Inicia el arreglo
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonWriter.beginObject(); // Inicia un objeto dentro del arreglo
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object value = jsonObject.get(key);
                    jsonWriter.name(key);
                    jsonWriter.value(String.valueOf(value));
                }
                jsonWriter.endObject(); // Finaliza el objeto dentro del arreglo
            }
            jsonWriter.endArray(); // Finaliza el arreglo
            jsonWriter.flush();
            jsonWriter.close();
            Log.i("JSON:", "Se actualizó el archivo con la nueva información.");
        } catch (IOException | JSONException e) {
            Log.w("JSON:", "Error: " + e.getMessage().toString());
        }
    }

    public List<Map<String,Object>> getInfoJsonForms(String idGarden, String formName) throws FileNotFoundException, JSONException {

        File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
        if(gardenDir.exists()){
            File infoFormFile = new File(gardenDir, "infoForm.json");
            if(infoFormFile.exists()){
                FileInputStream inputStream = new FileInputStream(infoFormFile);
                String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
                JSONArray jsonArray = new JSONArray(new JSONTokener(jsonString));
                List<Map<String,Object>> formsListResult = new ArrayList<>();
                for( int i = 0 ;  i < jsonArray.length() ; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.getString("nameForm").equals(formName)){
                        Map<String, Object> formMap = new HashMap<>();
                        Iterator<String> iterator = jsonObject.keys();
                        while( iterator.hasNext() ){
                            String key = iterator.next();
                            Object value = jsonObject.get(key);
                            formMap.put(key,value);
                        }
                        formsListResult.add(formMap);
                    }
                }
                return formsListResult;
            }
        }
        return null;
    }

    public void deleteInfoJson(String idGarden,Map<String, Object> infoForm) throws IOException, JSONException {
        File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
        if(gardenDir.exists()) {
            File infoFormFile = new File(gardenDir, "infoForm.json");
            if (infoFormFile.exists()) {
                FileInputStream inputStream = new FileInputStream(infoFormFile);
                String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
                JSONArray jsonArray = new JSONArray(new JSONTokener(jsonString));
                List<Map<String,Object>> formsListResult = new ArrayList<>();
                for( int i = 0 ;  i < jsonArray.length() ; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Map<String, Object> map = new HashMap<>();
                    Iterator<String> keys = jsonObject.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        if (!key.equals("nameForm") && !key.equals("idForm") && !key.equals("Date")) {
                            map.put(key, jsonObject.get(key));
                        }
                        if (jsonObject.getString("nameForm").equals(infoForm.get("nameForm"))
                                && jsonObject.getString("idForm").equals(infoForm.get("idForm"))
                                && jsonObject.getString("Date").equals(infoForm.get("Date"))) {
                            Log.i("DELETE-JSON", "SE ENCONTRO EL ELEMENTO EN EL JSON");
                        }else{
                            formsListResult.add(map);
                        }
                    }
                    FileWriter fileWriter = new FileWriter(infoFormFile);
                    fileWriter.write(new JSONArray(formsListResult).toString());
                    fileWriter.flush();
                    fileWriter.close();
                }
            }
        }
    }
}