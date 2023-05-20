package com.example.opcv.model.persistance.repository.local_db;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
import java.util.concurrent.CountDownLatch;


public class LocalDatabase implements LocalDatabaseI {
    private Context context;
    private boolean createJsonFormCalled = false;

    public LocalDatabase(Context mContext) {
        context = mContext;
    }

    public boolean isCreateJsonFormCalled() {
        return createJsonFormCalled;
    }

    @Override
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

                //Log.i("JSON:", "Se actualizó el archivo con la nueva información.");
            } catch (IOException e) {
                //Log.w("JSON:", "Error: " + e.getMessage());
            }
        }
        createJsonFormCalled = true;
    }

    public void updateAllJson(List<Map<String, Object>> newInfo, String idGarden) throws InterruptedException {
        createJsonFormCalled = true;
        if (newInfo.isEmpty()) {
            return;
        }

        File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
        if (!gardenDir.exists()) {
            gardenDir.mkdirs();
            Log.i("JSON:", "No existía la carpeta, se crea la carpeta de la huerta en: " + gardenDir.getAbsolutePath());
        }

        File infoFormFile = new File(gardenDir, "infoForm.json");

        JsonArray jsonArray;
        if (infoFormFile.exists()) {
            try {
                FileReader fileReader = new FileReader(infoFormFile);
                jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();
                fileReader.close();
            } catch (IOException e) {
                Log.e("JSON:", "Error al leer el archivo infoForm.json: " + e.getMessage());
                return;
            }
        } else {
            jsonArray = new JsonArray();
        }

        Gson gson = new Gson();

        // Eliminar elementos del JSON que no están en la lista de nuevos elementos
        for (int i = jsonArray.size() - 1; i >= 0; i--) {
            JsonObject existingObject = jsonArray.get(i).getAsJsonObject();
            if (!isInNewInfo(existingObject, newInfo)) {
                jsonArray.remove(i);
            }
        }

        // Agregar nuevos elementos al JSON
        for (Map<String, Object> infoMap : newInfo) {
            JsonObject jsonObject = gson.toJsonTree(infoMap).getAsJsonObject();
            if (!jsonArray.contains(jsonObject)) {
                jsonArray.add(jsonObject);
            }
        }

        // Utilizar CountDownLatch para bloquear la ejecución hasta que se complete el proceso
        CountDownLatch latch = new CountDownLatch(1);

        try {
            FileWriter writer = new FileWriter(infoFormFile);
            gson.toJson(jsonArray, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e("JSON:", "Error al actualizar el archivo infoForm.json: " + e.getMessage());
        } finally {
            latch.countDown(); // Liberar el CountDownLatch
        }

        latch.await(); // Bloquear la ejecución hasta que se complete el proceso
    }


    private boolean isInNewInfo(JsonObject existingObject, List<Map<String, Object>> newInfo) {
        Gson gson = new Gson();
        String existingJson = gson.toJson(existingObject);

        for (Map<String, Object> infoMap : newInfo) {
            String newJson = gson.toJson(infoMap);
            if (existingJson.equals(newJson)) {
                return true;
            }
        }

        return false;
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

    public void updateInfoJson(String idGarden, Map<String, Object> newInfo) {
        File gardenDir = new File(context.getExternalFilesDir(null), "Gardenforms/" + idGarden);
        File infoFormFile = new File(gardenDir, "infoForm.json");

        if (!infoFormFile.exists()) {
            Log.i("JSON:", "El archivo de información no existe.");
            return;
        }

        try {
            Gson gson = new Gson();

            FileReader fileReader = new FileReader(infoFormFile);
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> existingForms = gson.fromJson(fileReader, listType);
            fileReader.close();

            boolean infoUpdated = false;

            for (int i = 0; i < existingForms.size(); i++) {
                Map<String, Object> form = existingForms.get(i);

                if (form.get("CreatedBy").equals(newInfo.get("CreatedBy")) &&
                        form.get("Date").equals(newInfo.get("Date"))) {
                    // Actualiza la información existente con los nuevos valores
                    existingForms.set(i, newInfo);
                    infoUpdated = true;
                    break;
                }
            }

            if (!infoUpdated) {
                // Si no se encontró la información existente, la agrega como un nuevo formulario
                existingForms.add(newInfo);
            }

            // Escribe la lista actualizada en el archivo
            FileWriter fileWriter = new FileWriter(infoFormFile);
            gson.toJson(existingForms, fileWriter);
            fileWriter.flush();
            fileWriter.close();

            //Log.i("JSON:", "Se actualizó el archivo con la nueva información.");
        } catch (IOException e) {
            //Log.w("JSON:", "Error: " + e.getMessage());
        }
        createJsonFormCalled = true;
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
        createJsonFormCalled = true;
    }
}