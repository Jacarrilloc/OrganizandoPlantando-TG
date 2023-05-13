package com.example.opcv.model.persistance.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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



    public List<Map<String, Object>> getInfoForms(String idGarden, String formName) {
        List<Map<String, Object>> infoJsonForms = null;

        if (isOnline()) {
            updateDatabase(idGarden);
        }

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


    private void updateDatabase(String idGarden) {
        FirebaseDatabase info = new FirebaseDatabase();
        info.getAllInfoFormDatabase(idGarden, new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Map<String, Object>> data) throws InterruptedException {
                if (data != null) {
                    Log.i("Respository", "Info: " + data.size());
                    writeInLocal(data,idGarden);
                } else {
                    Log.i("Respository", "Info: null");
                }
            }
        });
    }

    private void writeInLocal(List<Map<String, Object>> data,String idGarden) throws InterruptedException {
        Log.i("writeInLocal", "Info: " + data.size());
        LocalDatabase info = new LocalDatabase(mContext);
        info.updateAllJson(data,idGarden);
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Map<String, Object>> data) throws InterruptedException;
    }

}
