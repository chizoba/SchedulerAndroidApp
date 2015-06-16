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
    //        Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    //        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    //        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();

        String message = intent.getStringExtra("msg");

        NotificationManager  notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, ScheduleActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        int icon = R.mipmap.ic_launcher;
        String tickerText = "New Schedule";
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);

        String contentTitle = "Scheduler";
        String contentText = message;
        notification.setLatestEventInfo(this, contentTitle, contentText, pendingIntent);
        notification.defaults=Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS;

        notificationManager.notify(123, notification);



    }
}
