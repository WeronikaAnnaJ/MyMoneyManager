package com.example.mymoneymanager;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomContentProvider extends ContentProvider {
    private DataBaseManager dataBaseManager;
    private static final String AUTHORITY = "com.example.mymoneymanager.CustomContentProvider";
    public static final int ROWS = 1;
    public static final int ROWS_ID = 2;
    public static final String TABLE_BASE_PATH = "input_finances";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + TABLE_BASE_PATH);

    @Override
    public boolean onCreate() {
        dataBaseManager = new DataBaseManager(getContext());
        if (dataBaseManager != null) {
            return true;
        }
        return false;
    }

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, TABLE_BASE_PATH, ROWS);
        sURIMatcher.addURI(AUTHORITY, TABLE_BASE_PATH + "/#", ROWS_ID);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection_columns,
                        @Nullable String selection_criteria, @Nullable String[] selectionArgs, @Nullable String orderBy) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DataBaseManager.TABLE_NAME);
        int uriType = URIMatcher.match(uri);
        switch (uriType) {
            case ROWS_ID:
                queryBuilder.appendWhere(dataBaseManager.ID_COLUMN_NAME + "="
                        + uri.getLastPathSegment());
                break;
            case ROWS:
                break;
            default:
                throw new IllegalArgumentException();
        }

        Cursor cursor = queryBuilder.query(dataBaseManager.getReadableDatabase(),
                projection_columns, selection_criteria, selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    private static final UriMatcher URIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        URIMatcher.addURI(AUTHORITY, TABLE_BASE_PATH, ROWS);
        URIMatcher.addURI(AUTHORITY, TABLE_BASE_PATH + "/#", ROWS_ID);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
