package com.example.mymoneymanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Calendar;

public class ListChartCustomView extends View {

    private int yX;
    private int yStartY;
    private int yStopY;
    private int xStartX;
    private int xStopX;
    private int xY;
    private int zeroPointY;
    private int zeroPointX;
    private int arrowLength;

    private float yMin;
    private float yMax;
    private int yStep;
    private static int nY = 16;
    private int xN;
    private int xNDraw;
    private int xLength;
    private int xSinglePart;
    private int xMin;
    private int xMax;

    private int ySpaceVertical;
    private float[] coordinates;
    private int textSize;

    private int hight;
    private int width;
    private int canvasHeight;
    private int canvasWidth;
    private String attr;
    private int lastC;

    public ListChartCustomView(Context context) {
        super(context);
    }

    public ListChartCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ListChartCustomView,
                0, 0);
        try {
            attr = a.getString(R.styleable.ListChartCustomView_coordinates);
        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (coordinates == null) return;
        setChartMeasurements(canvas, coordinates);
        super.onDraw(canvas);
        drawChart(canvas, xMax, coordinates);
    }

    protected void setData(float[] coordinates, int xMax) {
        this.xMax = coordinates.length - 1;
        this.lastC = xMax;
        //   float[] coor = {2, 4, 3, 2, -2, 20, -20, 5, -5, 10, 23, 3, -8, 8, 8, 8, 8, 8, 8, 2, -2, 3, 4, 5, -5, 0, 0, 0, 0, 0, 4, 1, 12};
        this.coordinates = coordinates;
    }


    private void setChartMeasurements(Canvas canvas, float[] coordinates) {

        canvasWidth = getWidth();
        canvasHeight = getHeight();
        width = canvasWidth / 17;
        hight = canvasHeight / 17;
        yX = 3 * width;
        yStartY = hight;
        yStopY = 17 * hight;
        xY = hight * 9;
        xStartX = yX;
        xStopX = width * 16 + width / 2;
        zeroPointY = xY;
        zeroPointX = yX;
        arrowLength = width / 4;
        xN = xMax - xMin;
        xNDraw = lastC - xMin;
        xLength = xStopX - xStartX;
        xSinglePart = (xLength - arrowLength) / xN;
        xSinglePart = (xLength - arrowLength - (xSinglePart / 4)) / xN;
        yMin = getMinY(coordinates);
        yMax = getMaxY(coordinates);//23
        textSize = width / 7 * 2;
        ySpaceVertical = textSize;
    }


    public void drawChart(Canvas canvas, int xMax, float[] coordinates) {
        drawLinesXY(canvas);
        drawArrowsXY(canvas);
        drawCoordinatesX(canvas);
        drawCoordinatesY(canvas);
        drawCoordinatesPoints(canvas, coordinates);
        drawbrokenLine(canvas);
    }

    private void drawCoordinatesY(Canvas canvas) {
        Paint paint = createPaint(textSize, Color.BLACK);
        setYStep(canvas);
        int xValue = (int) yStep;
        int xLength = (int) paint.measureText(String.valueOf(xMax));
        canvas.drawText(" 0", zeroPointX - 3 * xLength, zeroPointY, paint);
        for (int i = 1; i <= nY; i++) {
            if (xValue == 0) {
                return;
            }
            String x = String.valueOf(xValue);
            canvas.drawText("-" + x, zeroPointX - 3 * xLength, zeroPointY + (i * xSinglePart), paint);
            canvas.drawText(" " + x, zeroPointX - 3 * xLength, zeroPointY - (i * xSinglePart), paint);
            xValue += yStep;
        }

    }


    private void drawCoordinatesX(Canvas canvas) {
        Paint paint = createPaint(textSize, Color.BLACK);
        int xValue = xMin;
        int xLength = (int) paint.measureText(String.valueOf(xMax));
        for (int i = 1; i <= xN + 1; i++) {
            String x = String.valueOf(xValue);
            canvas.drawText(x, zeroPointX + (i * xSinglePart) - (xLength / 2), xY + textSize, paint);
            xValue++;
        }
    }


    private void drawCoordinatesPoints(Canvas canvas, float[] coordinates) {
        Paint paintUp = createPaint(Color.GREEN, 0.2f);
        Paint paintDown = createPaint(Color.RED, 0.2f);
        Paint paintNeutral = createPaint(Color.DKGRAY, 0.2f);
        int radius = 4;
        int xCurrentPosition = xSinglePart;
        for (int i = 0; i <= xNDraw; i++) {
            int y = (int) (coordinates[i]);
            double value = coordinates[i] / yStep;
            int cells = (int) value;
            int decimalPart = (int) (value - cells) * xSinglePart;
            if (y > 0) {
                canvas.drawCircle(zeroPointX + xCurrentPosition, zeroPointY - ((cells * xSinglePart) + decimalPart), radius, paintUp);
            } else if (y < 0) {
                canvas.drawCircle(zeroPointX + xCurrentPosition, zeroPointY - (cells * xSinglePart + decimalPart), radius, paintDown);
            } else {
                canvas.drawCircle(zeroPointX + xCurrentPosition, zeroPointY - (cells * xSinglePart + decimalPart), radius, paintNeutral);
            }
            xCurrentPosition += xSinglePart;
        }
    }


    private void drawbrokenLine(Canvas canvas) {
        Paint paintUp = createPaint(Color.GREEN, 2.0f);
        Paint paintDown = createPaint(Color.RED, 2.0f);
        Paint paintNeutral = createPaint(Color.DKGRAY, 2.0f);
        for (int i = 0; i < coordinates.length; i++) {
            System.out.println("cor: " + coordinates[i]);
        }
        int xCurrentPosition = xSinglePart;

        for (int i = 0; i < xNDraw; i++) {
            if (xCurrentPosition <= xN * xSinglePart) {
                int y = (int) (coordinates[i]);
                int nextY = (int) (coordinates[i + 1]);
                double value = coordinates[i] / yStep;
                int cells = (int) value;
                int decimalPart = (int) (value - cells) * xSinglePart;
                double value2 = coordinates[i + 1] / yStep;
                int cells2 = (int) value2;
                int decimalPart2 = (int) (value2 - cells2) * xSinglePart;
                //zeroPointX+xCurrentPosition
                //zeroPointY-((cells*xSinglePart)+decimalPart)
                int startX = zeroPointX + (xCurrentPosition);
                int stopX = zeroPointX + ((xCurrentPosition + xSinglePart));
                int startY = zeroPointY - ((cells * xSinglePart) + decimalPart);
                int stopY = zeroPointY - ((cells2 * xSinglePart) + decimalPart2);
                if (nextY > 0) {
                    System.out.println("Next y > 0 ");
                    canvas.drawLine(startX, startY, stopX, stopY, paintUp);
                } else if (nextY < 0) {
                    canvas.drawLine(startX, startY, stopX, stopY, paintDown);
                } else {
                    if (y > 0) {
                        canvas.drawLine(startX, startY, stopX, stopY, paintUp);
                    } else if (y < 0) {
                        canvas.drawLine(startX, startY, stopX, stopY, paintDown);
                    } else {
                        canvas.drawLine(startX, startY, stopX, stopY, paintNeutral);
                    }
                }
                xCurrentPosition += xSinglePart;
            }
        }
    }

    private void setYStep(Canvas canvas) {
        float yMinAbs = Math.abs(yMin);
        float yMaxAbs = Math.abs(yMax);
        if (yMinAbs > yMaxAbs) {
            yStep = (int) Math.ceil(yMinAbs / nY);
        } else {
            yStep = (int) Math.ceil(yMaxAbs / nY);
        }
    }

    private Paint createPaint(int color, Float strokeWidth) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        return paint;
    }

    private Paint createPaint(int textSize, int color) {
        Paint paint = new Paint();
        Typeface font = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
        paint.setTypeface(font);
        paint.setTextSize(textSize);
        paint.setColor(color);
        return paint;
    }


    private void drawLinesXY(Canvas canvas) {
        Paint paint = createPaint(Color.BLACK, 1.5f);
        canvas.drawLine(yX, yStartY, yX, yStopY, paint);
        canvas.drawLine(xStartX, xY, xStopX, xY, paint);
    }

    private void drawArrowsXY(Canvas canvas) {
        Paint paint = createPaint(Color.BLACK, 1.5f);
        canvas.drawLine(yX, yStartY, yX - (arrowLength / 2), yStartY + arrowLength, paint);
        canvas.drawLine(yX, yStartY, yX + (arrowLength / 2), yStartY + arrowLength, paint);
        canvas.drawLine(xStopX, xY, xStopX - arrowLength, xY + (arrowLength / 2), paint);
        canvas.drawLine(xStopX, xY, xStopX - arrowLength, xY - (arrowLength / 2), paint);
    }


    public static float getMinY(float[] coordinates) {
        float currentMin = coordinates[0];
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i] < currentMin) {
                currentMin = coordinates[i];
            }
        }
        return currentMin;
    }

    public static float getMaxY(float[] coordinates) {
        float currentMax = coordinates[0];
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i] > currentMax) {
                currentMax = coordinates[i];
            }
        }
        return currentMax;
    }


    /*   public void init() {

           float[] coordinates = {2, 4, 3, 2, -2, 20, -20, 5, -5, 10, 23, 3, -8, 8, 8, 8, 8, 8, 8, 2, -2, 3, 4, 5, -5, 0, 0, 0, 0, 0, 4, 1, 12};
           setData(coordinates);
           String[] ar = getAttr().split(",");
           for (int i = 0; i < ar.length; i++) {
               coordinates[i] = Float.parseFloat(ar[i]);
           }
           this.coordinates = coordinates;
           this.xMax = 32;
           this.xMin = 1;


    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
        this.invalidate();
    }  }*/

}
