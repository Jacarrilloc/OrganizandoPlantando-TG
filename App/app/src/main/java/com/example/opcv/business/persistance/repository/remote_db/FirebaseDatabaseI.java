package com.example.opcv.business.persistance.repository.remote_db;

import java.util.Map;

public interface FirebaseDatabaseI {
    void createInDatabase(String idGarden, Map<String,Object> infoForm);
}
