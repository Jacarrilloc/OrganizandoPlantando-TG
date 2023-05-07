package com.example.opcv.business.persistance.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.business.persistance.repository.local_db.LocalDatabase;
import com.example.opcv.business.persistance.repository.local_db.LocalDatabaseI;
import com.example.opcv.business.persistance.repository.remote_db.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
        LocalDatabase deleteInfoLocal = new LocalDatabase(mContext);
        deleteInfoLocal.deleteInfoJson(idGarden,infoForm);

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

    public List<Map<String,Object>> getInfoForms(String idGarden, String formName) throws FileNotFoundException, JSONException {
        if(isOnline()){
            //Aqui debe ir el llamado a un metodo que actualice los datos del json con la informacion de Firebase
        }
        LocalDatabase infoForm = new LocalDatabase(mContext);
        return infoForm.getInfoJsonForms(idGarden,formName);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
