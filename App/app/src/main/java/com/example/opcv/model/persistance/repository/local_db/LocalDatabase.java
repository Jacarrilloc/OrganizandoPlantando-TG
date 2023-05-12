package com.example.opcv.model.persistance.repository.local_db;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

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
import java.lang.reflect.Type;
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
        if (infoForm.get("CreatedBy") != null) {
            // El permiso ya está concedido, ejecutamos la tarea
            File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
            if (!gardenDir.exists()) {
                gardenDir.mkdirs();
                Log.i("JSON:", "No existia la carpeta, se crea la carpeta de la huerta en: " + gardenDir.getAbsolutePath());
            }

            File infoFormFile = new File(gardenDir, "infoForm.json");
            try {
                Gson gson = new Gson();

                List<Map<String, Object>> existingForms = new ArrayList<>();

                if (infoFormFile.exists()) {
                    // Lee el archivo existente y convierte su contenido en una lista de mapas
                    FileReader fileReader = new FileReader(infoFormFile);
                    Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                    existingForms = gson.fromJson(fileReader, listType);
                    fileReader.close();

                    // Verifica si la información ya existe en el archivo
                    if (existingForms.contains(infoForm)) {
                        Log.i("JSON:", "La información ya existe en el archivo.");
                        return;
                    }
                }

                // Agrega la nueva información al archivo
                existingForms.add(infoForm);

                // Escribe la lista actualizada en el archivo
                FileWriter fileWriter = new FileWriter(infoFormFile);
                gson.toJson(existingForms, fileWriter);
                fileWriter.flush();
                fileWriter.close();

                Log.i("JSON:", "Se actualizó el archivo con la nueva información.");
            } catch (IOException e) {
                Log.w("JSON:", "Error: " + e.getMessage());
            }
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


    public List<Map<String, Object>> getInfoJsonForms(String idGarden, String formName) {
        try {
            File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
            if (gardenDir.isDirectory()) {
                File infoFormFile = new File(gardenDir, "infoForm.json");
                if (infoFormFile.exists() && infoFormFile.length() > 0) {
                    Gson gson = new Gson();

                    FileReader fileReader = new FileReader(infoFormFile);
                    Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                    List<Map<String, Object>> formsList = gson.fromJson(fileReader, listType);
                    fileReader.close();

                    List<Map<String, Object>> formsListResult = new ArrayList<>();
                    for (Map<String, Object> form : formsList) {
                        if (form.get("nameForm").equals(formName)) {
                            formsListResult.add(form);
                        }
                    }
                    return formsListResult;
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    public void updateInfoJson(String idGraden,Map<String,Object> newInfo) throws JSONException, IOException {
        deleteInfoJson(idGraden, newInfo);
        createJsonForm(idGraden, newInfo);
    }

    public void deleteInfoJson(String idGarden, Map<String, Object> infoForm) throws IOException {
        File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
        if (gardenDir.exists()) {
            File infoFormFile = new File(gardenDir, "infoForm.json");
            if (infoFormFile.exists()) {
                Gson gson = new Gson();

                FileReader fileReader = new FileReader(infoFormFile);
                Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                List<Map<String, Object>> formsList = gson.fromJson(fileReader, listType);
                fileReader.close();

                String date = (String) infoForm.get("Date");
                String createdBy = (String) infoForm.get("CreatedBy");
                String idForm = String.valueOf(infoForm.get("idForm"));

                for (int i = 0; i < formsList.size(); i++) {
                    Map<String, Object> form = formsList.get(i);
                    String jsonDate = (String) form.get("Date");
                    String jsonCreatedBy = (String) form.get("CreatedBy");
                    String jsonIdForm = String.valueOf(form.get("idForm"));
                    if (jsonDate.equals(date) && jsonCreatedBy.equals(createdBy) && jsonIdForm.equals(idForm)) {
                        formsList.remove(i);
                        break;
                    }
                }

                FileWriter fileWriter = new FileWriter(infoFormFile);
                gson.toJson(formsList, fileWriter);
                fileWriter.close();
            }
        }
    }
}