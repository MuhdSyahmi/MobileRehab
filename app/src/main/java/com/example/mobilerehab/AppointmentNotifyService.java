package com.example.mobilerehab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AppointmentNotifyService extends Service {

    public static final String INTENT_NOTIFY = "com.example.mobilerehab.INTENT_NOTIFY";
    private static final int NOTIFICATION = 123;
    private final IBinder mBinder = new ServiceBinder();
    private NotificationManager notificationManager;

    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        if (intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void showNotification() {
        CharSequence title = "Notification";
        int icon = R.drawable.icon;
        CharSequence text = "Your notification time is upon us.";
        long time = System.currentTimeMillis();

        Notification notification = new Notification(icon, text, time);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, PatientHome.class), 0);

        Notification.Builder builder = new Notification.Builder(AppointmentNotifyService.this);

        builder.setAutoCancel(false);

        builder.setTicker("this is ticker text");
        builder.setContentTitle("Notification");
        builder.setContentText("Your notification time is upon us.");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(contentIntent);
        builder.setOngoing(true);
        builder.setSubText("This is subtext...");
        builder.setNumber(100);
        builder.build();

        notification = builder.getNotification();
        notificationManager.notify(11, notification);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(NOTIFICATION, notification);

        stopSelf();
    }

    public class ServiceBinder extends Binder {
        AppointmentNotifyService getService() {
            return AppointmentNotifyService.this;
        }
    }

}
