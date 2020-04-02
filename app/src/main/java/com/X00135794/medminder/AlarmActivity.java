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
        String med = intent.getStringExtra("medName");
        notifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MedminderID",
                    "MedminderNotification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for distributing medication reminders");
            notifyMgr.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "MedminderID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(med) // title for notification
                .setContentText("You need to take your" + med)// message for notification
                .setAutoCancel(true); // clear notification after click


        Intent i = new Intent(context, UserAccountActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        notifyMgr.notify(0, mBuilder.build());
    }
}
