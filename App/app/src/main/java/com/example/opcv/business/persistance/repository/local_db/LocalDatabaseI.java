package com.example.opcv.business.persistance.repository.local_db;

import java.util.Map;

public interface LocalDatabaseI {
    void createJsonForm(String idGarden, Map<String, Object> infoForm);
}
