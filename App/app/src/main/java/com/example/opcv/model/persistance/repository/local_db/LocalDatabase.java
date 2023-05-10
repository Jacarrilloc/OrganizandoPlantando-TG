package com.example.opcv.model.persistance.repository.local_db;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

                // Verifica si la información ya existe en el archivo
                boolean found = false;
                JSONObject newObject = new JSONObject(infoForm);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject existingObject = jsonArray.getJSONObject(i);
                    if (newObject.toString().equals(existingObject.toString())) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    Log.i("JSON:", "La información ya existe en el archivo.");
                    return;
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


    public void updateAllJson(List<Map<String, Object>> newInfo, String idGarden) {
        if (newInfo.isEmpty()) {
            return;
        }

        File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
        if (!gardenDir.exists()) {
            gardenDir.mkdirs();
            Log.i("JSON:", "No existia la carpeta, se crea la carpeta de la huerta en: " + gardenDir.getAbsolutePath());
        }

        File infoFormFile = new File(gardenDir, "infoForm.json");
        try {
            JSONArray jsonArray;
            if (!infoFormFile.exists()) {
                jsonArray = new JSONArray();
            } else {
                String jsonContent = readFileToString(infoFormFile);
                jsonArray = new JSONArray(jsonContent);
            }

            for (Map<String, Object> infoMap : newInfo) {
                boolean found = false;
                JSONObject jsonObject = new JSONObject(infoMap);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject existingObject = jsonArray.getJSONObject(i);
                    if (jsonObject.toString().equals(existingObject.toString())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    jsonArray.put(jsonObject);
                }
            }

            FileWriter writer = new FileWriter(infoFormFile);
            writer.write(jsonArray.toString());
            writer.flush();
            writer.close();
        } catch (IOException | JSONException e) {
            Log.e("JSON:", "Error al actualizar archivo infoForm.json: " + e.getMessage());
        }
    }



    private String readFileToString(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        inputStream.close();
        return stringBuilder.toString();
    }


    public List<Map<String,Object>> getInfoJsonForms(String idGarden, String formName) {
        try {
            File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
            if(gardenDir.isDirectory()){
                File infoFormFile = new File(gardenDir, "infoForm.json");
                if (infoFormFile.exists()) {
                    if (infoFormFile.length() > 0) { // Agregamos esta verificación para asegurarnos de que el archivo tenga contenido
                        FileInputStream inputStream = new FileInputStream(infoFormFile);
                        String jsonString = new Scanner(inputStream).useDelimiter("\\Z").next();
                        JSONArray jsonArray = new JSONArray(new JSONTokener(jsonString));
                        List<Map<String,Object>> formsListResult = new ArrayList<>();
                        for( int i = 0 ;  i < jsonArray.length() ; i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("nameForm").equals(formName)){
                                Map<String, Object> formMap = new HashMap<>();
                                Iterator<String> iterator = jsonObject.keys();
                                while(iterator.hasNext()){
                                    String key = iterator.next();
                                    Object value = jsonObject.get(key);
                                    if(value instanceof JSONObject){
                                        formMap.put(key, toMap((JSONObject) value));
                                    } else {
                                        formMap.put(key,value);
                                    }
                                }
                                formsListResult.add(formMap);
                            }
                        }
                        return formsListResult;
                    }
                }
            }
            return null;
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            // Manejo específico de la excepción NoSuchElementException
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    private static Map<String, Object> toMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keysIterator = jsonObject.keys();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }



    public void updateInfoJson(String idGraden,Map<String,Object> newInfo) throws JSONException, IOException {
        deleteInfoJson(idGraden, newInfo);
        createJsonForm(idGraden, newInfo);
    }

    public void deleteInfoJson(String idGarden, Map<String, Object> infoForm) throws IOException, JSONException {
        File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
        if (gardenDir.exists()) {
            File infoFormFile = new File(gardenDir, "infoForm.json");
            if (infoFormFile.exists()) {
                BufferedReader reader = null;
                StringBuilder jsonBuilder = new StringBuilder();
                try {
                    reader = new BufferedReader(new FileReader(infoFormFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line).append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
                String json = jsonBuilder.toString();
                String date = (String) infoForm.get("Date");
                String createdBy = (String) infoForm.get("CreatedBy");
                String idForm = String.valueOf(infoForm.get("idForm"));
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String jsonDate = jsonObject.getString("Date");
                    String jsonCreatedBy = jsonObject.getString("CreatedBy");
                    String jsonIdForm = jsonObject.getString("idForm");
                    if (jsonDate.equals(date) && jsonCreatedBy.equals(createdBy) && jsonIdForm.equals(idForm)) {
                        jsonArray.remove(i);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(infoFormFile);
                            fos.write(jsonArray.toString(2).getBytes("UTF-8"));
                            fos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                fos.close();
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

}