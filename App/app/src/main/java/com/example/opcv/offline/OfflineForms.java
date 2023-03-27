package com.example.opcv.offline;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class OfflineForms {

    private String pathFolder = "/path/to/your/Forms/";

    public boolean hasJsonFormsFiles() {
        File dir = new File(pathFolder);
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                return true;
            }
        }
        return false;
    }

    public boolean createOfflineForm(Map<String,Object> formInfo){

        JSONObject json = new JSONObject(formInfo);
        File dir = new File(pathFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String fileName = "Form_" + dateFormat.format(now) + ".json";

        File file = new File(dir,fileName);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(json.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
