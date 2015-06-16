package com.zobtech.scheduler;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {


    DataBaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);

                    startActivity(new Intent(MainActivity.this, ScheduleActivity.class));

                } catch (Exception e) {
                    Log.e("ex", e.toString());
                } finally {
                    finish();
                }

            }

        };
        timer.start();
        myDb = new DataBaseHelper(this);


        //to hide action bar despite using a show action bar theme
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        String m = (String) actionBar.getTitle();

        Calendar c = Calendar.getInstance();


        int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR);
        int day = c.get(Calendar.DATE);
        String calTime = hours + ":" + minutes;
        if (AddScheduleActivity.notifyTime == calTime) {
            createNotification();
        }

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void createNotification() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, DetailActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Scheduler")
                .setContentText(AddScheduleActivity.textSchedule.getText().toString()).setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent).addAction(R.mipmap.ic_launcher, "Call", pIntent)
                .addAction(R.mipmap.ic_launcher, "More", pIntent)
                .addAction(R.mipmap.ic_launcher, "And more", pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, noti);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}