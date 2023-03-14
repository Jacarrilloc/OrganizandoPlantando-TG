package com.example.opcv.conectionInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkMonitorService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Toast.makeText(context, "-------HAY CONEXION A INTERNET-------", Toast.LENGTH_SHORT).show();
            } else {
                // No hay conectividad, muestra un mensaje de error o intenta reconectarse
                Toast.makeText(context, "NO HAY CONEXION A INTERNET", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

