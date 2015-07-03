package com.zobtech.scheduler;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Junior on 02/06/2015.
 */
public class SchedulerContentProvider extends ContentProvider {

    // database
    private DataBaseHelper database;

    // used for the UriMatcher
    private static final int SCHEDULES = 1;
    private static final int SCHEDULE_ID = 2;

    // content provider's namespace
    private static final String AUTHORITY = "com.zobtech.scheduler.contentprovider";
    private static final String BASE_PATH = "schedules";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);


    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/schedules";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/schedule";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, SCHEDULES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SCHEDULE_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DataBaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        //        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(DataBaseHelper.TABLE_NAME);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case SCHEDULES:
                break;
            case SCHEDULE_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(DataBaseHelper.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            //---get all books---
            case SCHEDULES:
                return "vnd.android.cursor.dir/vnd.zobtech.schedules";
            //---get a particular book---
            case SCHEDULE_ID:
                return "vnd.android.cursor.item/vnd.zobtech.schedules ";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case SCHEDULES:
                id = sqlDB.insert(DataBaseHelper.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case SCHEDULES:
                rowsDeleted = sqlDB.delete(DataBaseHelper.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case SCHEDULE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DataBaseHelper.TABLE_NAME,
                            DataBaseHelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(DataBaseHelper.TABLE_NAME,
                            DataBaseHelper.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case SCHEDULES:
                rowsUpdated = sqlDB.update(DataBaseHelper.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case SCHEDULE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DataBaseHelper.TABLE_NAME,
                            values,
                            DataBaseHelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DataBaseHelper.TABLE_NAME,
                            values,
                            DataBaseHelper.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
