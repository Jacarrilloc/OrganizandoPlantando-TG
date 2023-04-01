package com.example.opcv.conectionInfo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.localDatabase.DB_User;
import com.example.opcv.localDatabase.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public class NetworkMonitorService extends Service {

    private DatabaseHelper dbHelper;
    private String UserID;
    private ConnectivityManager connectivityManager;
    private BroadcastReceiver networkReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            // Comprueba si hay conexión a Internet
            if (isOnline()) {
                // Actualiza los datos de Firestore a partir de los datos de SQLite
                syncFirestoreWithSQLite();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // Obtiene una instancia del sistema de conectividad
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Registra un receptor de difusión para detectar cambios en la conexión a Internet
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Anula el registro del receptor de difusión
        unregisterReceiver(networkReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isOnline() {
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /*private void syncFirestore_CIH(String idGardenFb){
        DB_User info = new DB_User(this);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean conection = isOnline();
        if(conection){
            
        }
    }/*

    private void syncFirestoreWithSQLite() {

        /*

        // Obtener una instancia de la clase DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Obtener una instancia de FirebaseFirestore
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();

        // Obtener la colección "UserInfo" de Firestore
        CollectionReference usersCollection = dbFirestore.collection("UserInfo");

        // Obtener el estado de conexión a internet
        boolean isNetworkConnected = isOnline();

        // Si hay conexión a internet
        if (isNetworkConnected) {
            // Obtener todos los documentos de la colección "UserInfo" de Firestore
            usersCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    // Para cada documento de la colección
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Obtener el ID del documento
                        String documentId = document.getId();

                        // Obtener los datos del documento en un Map
                        Map<String, Object> userInfo = document.getData();

                        // Actualizar la tabla "UserInfo" en SQLite con los datos del Map
                        dbHelper.updateUserInfo(userInfo);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Persistence", "Error getting documents.", e);
                }
            });
        } else { // Si no hay conexión a internet
            // Obtener el ID del usuario actual
            AuthUtilities info = new AuthUtilities();
            String id = info.getCurrentUserUid();

            // Obtener los datos del usuario actual en la tabla "UserInfo" de SQLite
            Map<String, Object> userInfo = dbHelper.getUserInfo(id);

            // Si los datos no son nulos
            if (userInfo != null) {
                // Actualizar el documento correspondiente en la colección "UserInfo" de Firestore
                usersCollection.document(id).set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Persistence", "DocumentSnapshot successfully written!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Persistence", "Error writing document", e);
                    }
                });
            }
        }
    */}
}

