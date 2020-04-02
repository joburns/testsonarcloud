package com.X00135794.medminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MedItemActivity extends AppCompatActivity {

    private TextView medName;
    private TextView medDose;
    private Button cancelAlarm;
    private Button updateAlarm;
    private Button resetAlarm;
    private CardView alarmView;
    private TimePicker timePicker;

    private String med;
    private String dose;
    private String desc;
    private Date date;
    int requestCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Medication");
        medName = findViewById(R.id.medItemName);
        medDose = findViewById(R.id.medItemDosage);

        cancelAlarm = findViewById(R.id.btnCancel);
        updateAlarm = findViewById(R.id.btnUpdate);
        resetAlarm = findViewById(R.id.btnSetUpdateAlarm);
        alarmView = findViewById(R.id.cvAlarmView);
        timePicker = findViewById(R.id.timePickerUpdate);

        // getting this current intent to get the passed in values
        Intent intent = getIntent();
        med = intent.getStringExtra("medName");
        dose = intent.getStringExtra("medDose");
        desc = intent.getStringExtra("medDesc");
        date =  new Date();
        date.setTime(intent.getLongExtra("medStartDate", -1));
        requestCode = intent.getIntExtra("rCode",0);
        medName.setText("Medication name: " + med);
        medDose.setText("Dosage: " +dose);


        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent i = new Intent(MedItemActivity.this, AlarmActivity.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MedItemActivity.this,requestCode,i,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmMgr.cancel(pendingIntent);
                Toast.makeText(MedItemActivity.this, "Alarm is canceled", Toast.LENGTH_SHORT).show();
                updateAlarm.setText("Add Alarm");
            }
        });


        updateAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmView.setVisibility(View.VISIBLE);
            }
        });
        resetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                if(android.os.Build.VERSION.SDK_INT >=23){
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(), timePicker.getMinute(), 0);
                }else{
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                }

                setAlarm(calendar.getTimeInMillis(), requestCode);
                updateAlarm.setText("Update Alarm");
                alarmView.setVisibility(View.GONE);
            }
        });


    }

    private void setAlarm(long timeInMillis, int requestCode) {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(this, AlarmActivity.class);

        i.putExtra("medName",med);
        i.putExtra("medDosage",dose);
        i.putExtra("medDesc",desc);
        i.putExtra("medStartDate",date);
        i.putExtra("rCode",requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,requestCode,i,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }
}
