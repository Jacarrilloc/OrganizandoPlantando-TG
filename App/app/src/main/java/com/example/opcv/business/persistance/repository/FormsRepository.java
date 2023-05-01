package com.example.opcv.business.persistance.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.business.persistance.repository.local_db.LocalDatabase;
import com.example.opcv.business.persistance.repository.remote_db.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
