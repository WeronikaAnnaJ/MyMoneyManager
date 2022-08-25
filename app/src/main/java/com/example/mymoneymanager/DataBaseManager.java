package com.example.mymoneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataBaseManager extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "moneyManager";
    public static final String TABLE_NAME = "input_finances";
    public static final String ID_COLUMN_NAME = "id";
    public static final String CATEGORY_COLUMN_NAME = "category";
    public static final String TYPE_COLUMN_NAME = "type";
    public static final String MPK_COLUMN_NAME = "mpk";
    public static final String AMOUNT_COLUMN_NAME = "amount";
    public static final String DATE_COLUMN_NAME = "date";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID_COLUMN_NAME + " INTEGER PRIMARY KEY," + CATEGORY_COLUMN_NAME + " TEXT,"
            + TYPE_COLUMN_NAME + " TEXT, " + MPK_COLUMN_NAME + " TEXT, " +
            AMOUNT_COLUMN_NAME + " REAL," + DATE_COLUMN_NAME + " TEXT" + ")";

    public static final String DB_SCHEMA = CREATE_TABLE;

    public DataBaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DB_SCHEMA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public void dropTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    void addInput(RecordModel recordModel) {
        System.out.println("ADD RECORD - > ");
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, recordModel.getCategory());
        values.put(TYPE_COLUMN_NAME, recordModel.getType());
        values.put(MPK_COLUMN_NAME, recordModel.getMPK());
        values.put(AMOUNT_COLUMN_NAME, recordModel.getAmount());
        values.put(DATE_COLUMN_NAME, recordModel.getStringDate());
        database.insert(TABLE_NAME, null, values);
        database.close();
    }


    RecordModel getRecord(int id) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME, new String[]{ID_COLUMN_NAME, CATEGORY_COLUMN_NAME,
                        TYPE_COLUMN_NAME, MPK_COLUMN_NAME, AMOUNT_COLUMN_NAME,
                        DATE_COLUMN_NAME}, ID_COLUMN_NAME + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        RecordModel recordModel = new RecordModel();
        recordModel.setId(Integer.parseInt(cursor.getString(0)));
        recordModel.setCategory(cursor.getString(1));
        recordModel.setType(cursor.getString(2));
        recordModel.setMPK(cursor.getString(3));
        recordModel.setAmount(Double.parseDouble(cursor.getString(4)));
        recordModel.setDate(cursor.getString(5));

        return recordModel;
    }


    public ArrayList<RecordModel> getAllRecords() {
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<RecordModel> rowList = new ArrayList<RecordModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY SUBSTR(date,6) DESC ,SUBSTR(date,3,5) DESC, SUBSTR(date, 6) DESC";
        database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                RecordModel recordModel = new RecordModel();
                recordModel.setId(Integer.parseInt(cursor.getString(0)));
                recordModel.setCategory(cursor.getString(1));
                recordModel.setType(cursor.getString(2));
                recordModel.setMPK(cursor.getString(3));
                recordModel.setAmount(Double.parseDouble(cursor.getString(4)));
                recordModel.setDate(cursor.getString(5));
                rowList.add(recordModel);
            } while (cursor.moveToNext());
        }
        return rowList;
    }


    public int updateRecord(RecordModel recordModel) {
        //nie dziala update
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, recordModel.getCategory()); // Contact Name
        values.put(TYPE_COLUMN_NAME, recordModel.getType()); // Contact Name
        values.put(MPK_COLUMN_NAME, recordModel.getMPK()); // Contact Name
        values.put(AMOUNT_COLUMN_NAME, recordModel.getAmount()); // Contact Name
        values.put(DATE_COLUMN_NAME, recordModel.getStringDate()); // Contact Name

        return database.update(TABLE_NAME, values, ID_COLUMN_NAME + "=?",
                new String[]{String.valueOf(recordModel.getId())});
    }

    public void deleteRecord(RecordModel recordModel) {
        String id = String.valueOf(recordModel.getId());
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, ID_COLUMN_NAME + "=?",
                new String[]{id});
        database.close();
    }

    public double getAllRecordsAmountSum() {
        String countQuery = "SELECT SUM(" + AMOUNT_COLUMN_NAME + ") FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);

        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return 0;
    }

    public double getSumAmountBefore(int month, int year) {
        double sum = 0;
        String selectQuery = " SELECT sum(amount),date FROM input_finances " +

                " WHERE date NOT LIKE  '%" + month + "/" + year + "' " +

                "  GROUP BY date ";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursorDays = database.rawQuery(selectQuery, null);
        if (cursorDays.moveToFirst()) {
            do {
                double daySum = cursorDays.getDouble(0);
                String date = cursorDays.getString(1);
                String[] parts = date.split("/"); //returns an array with the 2 parts

                if (((Integer.parseInt(parts[1]) < month) && (Integer.parseInt(parts[2]) == year)) || (Integer.parseInt(parts[2]) < year)) {
                    sum += daySum;
                    System.out.println("date : " + date);
                    System.out.println("daySum : " + daySum);
                    int dayInMonth = Integer.parseInt(date.substring(0, 2));
                    System.out.println("dayInMonth : " + dayInMonth);
                }


            } while (cursorDays.moveToNext());
        }
        return sum;

    }


    public double[] getRecordsAmount(String month, String year, int daysInMonth) {
        double days[] = new double[daysInMonth + 1];
        String selectQuery = " SELECT date, sum(amount) FROM input_finances " +

                " WHERE date LIKE  '%" + month + "/" + year + "' " +

                "  GROUP BY date ";

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursorDays = database.rawQuery(selectQuery, null);
        if (cursorDays.moveToFirst()) {
            do {
                String date = cursorDays.getString(0);
                System.out.println("date : " + date);
                double daySum = cursorDays.getDouble(1);
                System.out.println("daySum : " + daySum);
                int dayInMonth = Integer.parseInt(date.substring(0, 2));
                System.out.println("dayInMonth : " + dayInMonth);
                days[dayInMonth] = daySum;

            } while (cursorDays.moveToNext());
        }
        return days;
    }
}

