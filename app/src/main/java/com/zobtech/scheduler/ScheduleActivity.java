package com.zobtech.scheduler;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScheduleActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int DELETE_ID = Menu.FIRST + 1;
    public static final String SCHE = "";

    // creating instances of List View and ScheduleCursorAdapter
    ListView listView;
    ScheduleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // initializing the instance of ScheduleCursorAdapter
        mAdapter = new ScheduleCursorAdapter(this, null, 0);

        // initializing the instance of List View
        listView = (ListView) findViewById(R.id.listview_schedule);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        // initializing the floating action button object
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.show();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ScheduleActivity.this, AddScheduleActivity.class), 1);
            }
        });

        // setting the instance of ScheduleCursorAdapter to the instance of the List View
        listView.setAdapter(mAdapter);

        // set a text view when the list view is empty
        TextView emptyText = (TextView) findViewById(R.id.emptyText);
        listView.setEmptyView(emptyText);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                long rowID = parent.getItemIdAtPosition(position);
                Intent intent = new Intent(ScheduleActivity.this, DetailActivity.class);
                intent.putExtra(SCHE, rowID);
                startActivity(intent);

            }
        });

        registerForContextMenu(listView);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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
        if (id == R.id.about) {
            startActivity(new Intent(ScheduleActivity.this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(SchedulerContentProvider.CONTENT_URI + "/"
                        + info.id);
                getContentResolver().delete(uri, null, null);
                getLoaderManager().initLoader(0, null, this);

                return true;
        }
        return super.onContextItemSelected(item);
    }

    // loader callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = SchedulerContentProvider.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

}
