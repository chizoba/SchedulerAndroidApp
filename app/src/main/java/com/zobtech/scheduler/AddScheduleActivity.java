package com.zobtech.scheduler;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddScheduleActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    // id's for timepicker and datepicker dialogs.
    static final int TIME_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID = 1;

    // variables to save the time and date from the time and date picker dialogs.
    int yr, month, day;
    int hour, minute;

    // views to set/display user's schedule, description, date and time.
    static EditText textSchedule;
    EditText textDescription;
    TextView textDate, textTime;

    // an instance of the DataBaseHelper class.
    private DataBaseHelper myDb;

    // an instance of a Uri class
    private Uri scheduleUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        // initializing the DataBaseHelper class.
        myDb = new DataBaseHelper(this);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            // initializing the text views
            textSchedule = (EditText) findViewById(R.id.schedule);
            textDescription = (EditText) findViewById(R.id.description);
            textDate = (TextView) findViewById(R.id.dateTextView);
            textTime = (TextView) findViewById(R.id.timeTextView);

            // get the current date and time
            Calendar today = Calendar.getInstance();
            yr = today.get(Calendar.YEAR);
            month = today.get(Calendar.MONTH);
            day = today.get(Calendar.DAY_OF_MONTH);

            hour = today.get(Calendar.HOUR_OF_DAY);
            minute = today.get(Calendar.MINUTE);
        }

        // check from the saved Instance
        scheduleUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
                .getParcelable(ScheduleActivity.SCHE);

        // Or passed from the other activity
        if (extras != null) {
            scheduleUri = extras
                    .getParcelable(ScheduleActivity.SCHE);

            // initializing the text views
            textSchedule = (EditText) findViewById(R.id.schedule);
            textDescription = (EditText) findViewById(R.id.description);
            textDate = (TextView) findViewById(R.id.dateTextView);
            textTime = (TextView) findViewById(R.id.timeTextView);

            // get the current date and time
            Calendar today = Calendar.getInstance();
            yr = today.get(Calendar.YEAR);
            month = today.get(Calendar.MONTH);
            day = today.get(Calendar.DAY_OF_MONTH);

            hour = today.get(Calendar.HOUR_OF_DAY);
            minute = today.get(Calendar.MINUTE);
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_schedule, menu);
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

        if (id == R.id.save) {
            // set error messages if text views are empty
            if (textSchedule.getText().toString().length() == 0)
                textSchedule.setError("A schedule title is required!");
            else if (textDate.getText().toString().length() == 0)
                textDate.setError("Please set a date");
            else if (textTime.getText().toString().length() == 0)
                textTime.setError("Please set a time");

            else {
                saveSelected();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int hour_minute) {
                    hour = hourOfDay;
                    minute = hour_minute;

                    String time = getTime(hourOfDay, hour_minute);

                    TextView timeTextView = (TextView) findViewById(R.id.timeTextView);

                    timeTextView.setText(time);
                }
            };

    private String getTime(int hr, int min) {
        Time time = new Time(hr, min, 0);
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(time);
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(
                        DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    yr = year;
                    month = monthOfYear;
                    day = dayOfMonth;

                    String dateSet = day + "-" + (month + 1) + "-" + year;
                    DateFormat dfFrom = new SimpleDateFormat("dd-MM-yyyy");
                    Date inputDate = null;
                    try {
                        inputDate = dfFrom.parse(dateSet);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    DateFormat dfTo = new SimpleDateFormat("MMMM dd, yyyy");
                    String outputDate = dfTo.format(inputDate);

                    TextView dateTextView = (TextView) findViewById(R.id.dateTextView);

                    dateTextView.setText(outputDate);
                }
            };

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, hour, minute, false);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(
                        this, mDateSetListener, yr, month, day);
        }
        return null;
    }


    @SuppressWarnings("deprecation")
    public void showTimePickerDialog(View view) {
        showDialog(TIME_DIALOG_ID);
    }

    @SuppressWarnings("deprecation")
    public void showDatePickerDialog(View view) {
        showDialog(DATE_DIALOG_ID);
    }

    public void saveSelected(){

        Intent i = new Intent();

        String message = textSchedule.getText().toString();

        // get an instance of the Calender class
        Calendar calendar = Calendar.getInstance();

        // set the Calender instance with date and time
        calendar.set(yr, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        i.setClass(AddScheduleActivity.this, SchedulerService.class);
        i.putExtra("msg", message);
        int idd = (int) System.currentTimeMillis();

        myDb = new DataBaseHelper(AddScheduleActivity.this);
        SQLiteDatabase db = myDb.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.clear();

        // put values in text views in database columns.
        values.put(DataBaseHelper.COLUMN_SCHEDULE_TITLE, textSchedule.getText().toString());
        values.put(DataBaseHelper.COLUMN_DATE, textDate.getText().toString());
        values.put(DataBaseHelper.COLUMN_TIME, textTime.getText().toString());
        values.put(DataBaseHelper.COLUMN_DESCRIPTION, textDescription.getText().toString());
        values.put(DataBaseHelper.COLUMN_NOTIFICATION_ID, idd);

        if (scheduleUri == null) {
            Uri uri = SchedulerContentProvider.CONTENT_URI;
            //New schedule
            scheduleUri = getApplicationContext().getContentResolver().insert(uri, values);
        } else {
            //Update schedule
            getContentResolver().update(scheduleUri, values, null, null);
        }

        PendingIntent pendingIntent = PendingIntent.getService(AddScheduleActivity.this, idd, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Intent intent = new Intent(AddScheduleActivity.this, ScheduleActivity.class);
        startActivity(intent);

        Toast toast = Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT);
        toast.show();

        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, scheduleUri, null, null, null, null);    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();

        // set text views with values from database.
        textSchedule.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_SCHEDULE_TITLE)));
        textDescription.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_DESCRIPTION)));
        textDate.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_DATE)));
        textTime.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_TIME)));

        // always close the cursor
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
