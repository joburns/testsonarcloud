package com.X00135794.medminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmActivity  extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d("MyAlarm", "Alarm just fired");

    }
}
