package com.zobtech.scheduler;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class DetailActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // instance of text views
    TextView detailSchedule, detailDate, detailTime, detailDescription;
    private Uri _uri;



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // initializing instances of text views
        detailSchedule = (TextView) findViewById(R.id.detailTextViewSchedule);
        detailDate = (TextView) findViewById(R.id.detailTextViewDate);
        detailTime = (TextView) findViewById(R.id.detailTextViewTime);
        detailDescription = (TextView) findViewById(R.id.detailTextViewDescription);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

        if (id == R.id.delete) {
            getContentResolver().delete(_uri, null, null);
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        }

        if (id == R.id.edit) {
            Intent intent = new Intent(this, AddScheduleActivity.class);
            intent.putExtra(ScheduleActivity.SCHE, _uri);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long rowID = getIntent().getLongExtra(ScheduleActivity.SCHE, -1);
        _uri = Uri.parse(SchedulerContentProvider.CONTENT_URI +"/"+ rowID);
        return new CursorLoader(this, _uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        cursor.moveToFirst();

        // display values gotten from database in text views
        detailSchedule.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_SCHEDULE_TITLE)));
        detailDescription.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_DESCRIPTION)));
        detailDate.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_DATE)));
        detailTime.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(DataBaseHelper.COLUMN_TIME)));

        if (detailDescription.getText().toString().length() == 0) {
            detailDescription.setText("No description set");
        }

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
