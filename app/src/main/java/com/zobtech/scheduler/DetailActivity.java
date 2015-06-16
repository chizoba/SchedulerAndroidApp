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


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    TextView detailSchedule, detailDate, detailTime, detailDescription;
    private Uri todoUri;



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailSchedule = (TextView) findViewById(R.id.detailTextViewSchedule);
        detailDate = (TextView) findViewById(R.id.detailTextViewDate);
        detailTime = (TextView) findViewById(R.id.detailTextViewTime);
        detailDescription = (TextView) findViewById(R.id.detailTextViewDescription);
        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        todoUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
                .getParcelable(SchedulerContentProvider.CONTENT_ITEM_TYPE);
        // Or passed from the other activity
        if (extras != null) {
            todoUri = extras
                    .getParcelable(SchedulerContentProvider.CONTENT_ITEM_TYPE);
        }
        fillData(todoUri);

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

            getContentResolver().delete(todoUri, null, null);
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            getLoaderManager().initLoader(0, null, this);

        }
        return super.onOptionsItemSelected(item);
    }


    private void fillData(Uri uri) {
        String[] projection = {DataBaseHelper.COLUMN_SCHEDULE_TITLE,
                DataBaseHelper.COLUMN_DATE, DataBaseHelper.COLUMN_TIME, DataBaseHelper.COLUMN_DESCRIPTION};
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();


            detailSchedule.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_SCHEDULE_TITLE)));
            detailDescription.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_DESCRIPTION)));
            detailDate.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_DATE)));
            detailTime.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(DataBaseHelper.COLUMN_TIME)));

            // always close the cursor
            cursor.close();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = SchedulerContentProvider.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    //        mAdapter.swapCursor(null);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
}
