package com.example.mobilerehab;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AppointmentTask implements Runnable {
    private final Calendar calendar;
    private final AlarmManager alarmManager;
    private final Context context;

    public AppointmentTask(Context context, Calendar calendar) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.calendar = calendar;
    }

    @Override
    public void run() {
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, AppointmentNotifyService.class);
        intent.putExtra(AppointmentNotifyService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}