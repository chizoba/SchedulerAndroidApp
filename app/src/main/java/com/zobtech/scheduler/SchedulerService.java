package com.zobtech.scheduler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


public class SchedulerService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        String message = intent.getStringExtra("msg");

        NotificationManager  notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int icon = R.mipmap.ic_launcher;
        String tickerText = "New Schedule";
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);

        Intent notificationIntent = new Intent(this, ScheduleActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String contentTitle = "Scheduler";
        String contentText = message;
        notification.setLatestEventInfo(this, contentTitle, contentText, pendingIntent);
        notification.defaults=Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS;

        // Clear the notification when it is pressed
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(123, notification);

    }
}
