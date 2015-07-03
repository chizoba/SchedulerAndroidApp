package com.zobtech.scheduler;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Junior on 28/05/2015.
 */
public class ScheduleCursorAdapter extends CursorAdapter {


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ScheduleCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.list_item, parent, false);

        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views

        TextView textViewSchedule = (TextView) view.findViewById(R.id.list_item_schedule_textview);
        textViewSchedule.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_SCHEDULE_TITLE)));

        TextView textViewDate = (TextView) view.findViewById(R.id.list_item_date_textview);
        textViewDate.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_DATE)));

        TextView textViewTime = (TextView) view.findViewById(R.id.list_item_time_textview);
        textViewTime.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUMN_TIME)));
    }


}