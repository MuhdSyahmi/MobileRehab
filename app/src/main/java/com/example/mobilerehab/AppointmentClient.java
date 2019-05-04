package com.example.mobilerehab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Calendar;

public class AppointmentClient {

    private static AppointmentService appointmentService;
    private Context mContext;
    private boolean aBoolean;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with our service has been established,
            // giving us the service object we can use to interact with our service.
            appointmentService = ((AppointmentService.ServiceBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            appointmentService = null;
        }
    };

    public AppointmentClient(Context context) {
        mContext = context;
    }

    public static void setAlarmForNotification(Calendar calendar) {
        appointmentService.setAlarm(calendar);
    }

    public void doBindService() {

        mContext.bindService(new Intent(mContext, AppointmentService.class), mConnection, Context.BIND_AUTO_CREATE);
        aBoolean = true;

    }

    public void doUnbindService() {
        if (aBoolean) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            aBoolean = false;
        }
    }

}
