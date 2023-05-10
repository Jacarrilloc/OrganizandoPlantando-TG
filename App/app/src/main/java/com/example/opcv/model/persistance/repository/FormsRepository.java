package com.example.opcv.model.persistance.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.model.persistance.repository.local_db.LocalDatabase;
import com.example.opcv.model.persistance.repository.remote_db.FirebaseDatabase;
import com.example.opcv.model.persistance.repository.remote_db.ResultAsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FormsRepository {
    private final Executor mExecutor;
    private final Context mContext;

    public FormsRepository(Context context) {
        mContext = context;
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public void insertForm(Map<String,Object> infoForm, String idGarden) {
        mExecutor.execute(() -> {
            // Ingresa la informacion en la base de datos local JSON
            LocalDatabase localDb = new LocalDatabase(mContext);
            localDb.createJsonForm(idGarden,infoForm);

            new Thread(() -> {
                while (!isOnline()) {
                    try {
                        Thread.sleep(1000); // Esperar 1 segundo antes de verificar de nuevo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                FirebaseDatabase onlineDB = new FirebaseDatabase();
                onlineDB.createInDatabase(idGarden, infoForm);

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }).start();
        });
    }

    public void deleteInfoDatabase(String idGarden,Map<String, Object> infoForm) throws IOException, JSONException {

        new Thread(() -> {
            while (!isOnline()) {
                try {
                    Thread.sleep(1000); // Esperar 1 segundo antes de verificar de nuevo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            FirebaseDatabase onlineDB = new FirebaseDatabase();
            onlineDB.deleteInDatabase(idGarden,infoForm);
        }).start();
        LocalDatabase deleteInfoLocal = new LocalDatabase(mContext);
        deleteInfoLocal.deleteInfoJson(idGarden,infoForm);
    }

    public void updateInfoDatabase(String idGarden,Map<String, Object> newInfoForm) throws JSONException, IOException {
        LocalDatabase updateInfo = new LocalDatabase(mContext);
        updateInfo.updateInfoJson(idGarden,newInfoForm);
        if(isOnline()){
            new Thread(() -> {
                while (!isOnline()) {
                    try {
                        Thread.sleep(1000); // Esperar 1 segundo antes de verificar de nuevo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                FirebaseDatabase onlineDB = new FirebaseDatabase();
                onlineDB.updateInDatabase(idGarden,newInfoForm);
            }).start();
        }
    }

    private boolean dataObtained = false;

    public List<Map<String, Object>> getInfoForms(String idGarden, String formName) throws IOException, JSONException {
        List<Map<String, Object>> infoJsonForms = null;
        /*
        if (isOnline()) {
            // Verifica si los datos ya han sido obtenidos antes de hacer una nueva consulta a la base de datos
            if (!dataObtained) {
                ResultAsyncTask task = new ResultAsyncTask(idGarden, formName, mContext);
                task.execute();

                try {
                    task.get(); // espera a que la tarea se complete
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                LocalDatabase info = new LocalDatabase(mContext);
                // Obtiene los datos de la base de datos y los almacena en una variable o estructura de datos
                infoJsonForms = info.getInfoJsonForms(idGarden, formName);

                // Actualiza la variable booleana para indicar que los datos ya han sido obtenidos
                dataObtained = true;
            }
        } else {
            LocalDatabase info = new LocalDatabase(mContext);
            infoJsonForms = info.getInfoJsonForms(idGarden, formName);
        }
         */
        LocalDatabase info = new LocalDatabase(mContext);
        infoJsonForms = info.getInfoJsonForms(idGarden, formName);
        return infoJsonForms;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
