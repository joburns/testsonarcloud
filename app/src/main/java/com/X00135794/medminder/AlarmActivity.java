package com.X00135794.medminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmActivity  extends BroadcastReceiver {

    private NotificationManager notifyMgr;

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d("MyAlarm", "Alarm just fired");
        notifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            notifyMgr.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Medminder Test notification") // title for notification
                .setContentText("This is a test")// message for notification
                .setAutoCancel(true); // clear notification after click


        Intent i = new Intent(context, UserAccountActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        notifyMgr.notify(0, mBuilder.build());
    }
}
