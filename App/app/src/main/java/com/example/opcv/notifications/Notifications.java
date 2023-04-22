package com.example.opcv.notifications;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.opcv.R;

public class Notifications {
    public void notification(String title, String text, Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        /*Intent intent1 = new Intent(context, intent.getClass());
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);*/


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "dafault")
                .setSmallIcon(R.drawable.im_logo_ceres_green)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true);
       Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       builder.setSound(soundUri);
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }
}
