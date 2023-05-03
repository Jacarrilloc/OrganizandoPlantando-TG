package com.example.opcv.business.persistance.repository.local_db;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface LocalDatabaseI {
    void createJsonForm(String idGarden, Map<String, Object> infoForm);
    List<Map<String,Object>> getInfoJsonForms(String idGarden, String formName) throws FileNotFoundException, JSONException;
}
