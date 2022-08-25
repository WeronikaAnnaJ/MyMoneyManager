package com.example.mymoneymanager;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Parameter;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class ChartDataModel {

    private int month;
    private int year;
    private int numberOfDaysInMonth;
    private float[] coordinates;
    private DataBaseManager database;
    private float amountBefore;
    private int lastDay;

    public ChartDataModel(int month, int year, DataBaseManager database) {
        this.month = month;
        this.year = year;
        this.database = database;
        this.numberOfDaysInMonth = getNumberOfDaysInMonth();
        this.amountBefore = (float) getSumAmountBefore(month, year);
        this.coordinates = setCoordinates();
        setLastDay();
    }

    public String getCoordinatesAttr() {
        String s = "";
        for (int i = 0; i < coordinates.length; i++) {
            s += coordinates[i] + ",";
        }
        return s;
    }

    private void setLastDay() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        if (currentMonth == month && currentYear == year) {
            lastDay = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = numberOfDaysInMonth;
        }
    }

    public int getNumberOfDaysInMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public double getSumAmountBefore(int month, int year) {
        return database.getSumAmountBefore(month, year);

    }

    public float[] getCoordinates() {
        return this.coordinates;
    }

    public static int getActualMonth() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("Actual month + " + calendar.get(Calendar.MONTH));
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getActualYear() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("Actual YEAr + " + calendar.get(Calendar.YEAR));
        return calendar.get(Calendar.YEAR);
    }

    public int getLastDay() {
        return lastDay;
    }

    public float[] setCoordinates() {
        String monthStr = String.format("%02d", month);
        double[] daysAmount = database.getRecordsAmount(monthStr, String.valueOf(year), numberOfDaysInMonth);
        double sumBefore = this.amountBefore;
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = 0;
        if (currentMonth == month && currentYear == year) {
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        }

        daysAmount[0] = sumBefore;
        float coordinates[] = new float[numberOfDaysInMonth + 1];
        coordinates[0] = (float) sumBefore;
        for (int day = 1; day < daysAmount.length; day++) {
            if ((currentDay != 0) && (day > currentDay)) {
                coordinates[day] = 0;
            } else {
                coordinates[day] = (float) (coordinates[day - 1] + daysAmount[day]);
            }
            //    System.out.println(day + " only -> " + daysAmount[day]);
            //   System.out.println(day + " saldo -> " + coordinates[day]);
        }
        return coordinates;


    }


}
