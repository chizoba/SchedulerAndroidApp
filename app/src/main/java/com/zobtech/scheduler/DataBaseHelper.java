package com.zobtech.scheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Junior on 25/05/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "Schedule.db";

    // Database table
    public static final String TABLE_NAME = "schedule_table";

    // Database Columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SCHEDULE_TITLE = "schedule";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_NOTIFICATION_ID = "notification_id";
    public static final String COLUMN_DESCRIPTION = "description";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + "("
            + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SCHEDULE_TITLE
            + " TEXT NOT NULL, "
            + COLUMN_DATE
            + " TEXT NOT NULL, "
            + COLUMN_TIME
            + " TEXT NOT NULL, "
            + COLUMN_NOTIFICATION_ID
            + " INTEGER, "
            + COLUMN_DESCRIPTION
            + " TEXT NOT NULL)";

    //Database version
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(DataBaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
