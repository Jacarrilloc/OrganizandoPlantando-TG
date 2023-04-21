package com.example.opcv.notifications;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.opcv.R;

public class Notifications {
    public void notification(String title, String text, Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default_channel", "Nombre del Canal", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Descripción del Canal");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "dafault")
                .setSmallIcon(R.drawable.im_logo_ceres_green)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);
        int notificationId = 0;
        notificationManager.notify(notificationId, builder.build());
    }
}
