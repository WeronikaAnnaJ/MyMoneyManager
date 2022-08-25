package com.example.mymoneymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Calendar;

import javax.xml.transform.sax.TemplatesHandler;

public class ChartActivity extends AppCompatActivity {

    private ImageButton generateB;
    private ImageButton returnB;
    private EditText yearInput;
    private Spinner monthSpinner;
    private TextView infoText;
    private ArrayAdapter<CharSequence> adapter_month;
    private ListChartCustomView listChartCustomView;
    private DataBaseManager dataBaseManager;
    private ChartDataModel chartDataModel;
    private String monthValue;
    private String yearValue;
    private int monthValueDigit;
    private TextView infoChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        setComponents();
        setMonthSpinner();
        setValues();
        setListeners();
        generateChart();
    }

    public void generateChart() {
        infoChart.setText("");
        getValues();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        if ((currentYear > Integer.parseInt(yearValue)) || ((currentYear == Integer.parseInt(yearValue)) && (currentMonth < monthValueDigit))) {
            infoChart.setText("Brak danych :(");
            listChartCustomView.setVisibility(View.INVISIBLE);
        } else {
            dataBaseManager = new DataBaseManager(ChartActivity.this);
            chartDataModel = new ChartDataModel(monthValueDigit, Integer.parseInt(yearValue), dataBaseManager);
            listChartCustomView = this.findViewById(R.id.v);
            listChartCustomView.setVisibility(View.VISIBLE);
            listChartCustomView.setData(chartDataModel.getCoordinates(), chartDataModel.getLastDay());
            listChartCustomView.invalidate();
            listChartCustomView.requestLayout();
        }
    }

    public void setListeners() {
        generateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateChart();
            }
        });
        returnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecordsListActivity.class));
            }
        });
    }

    private void getValues() {
        monthValue = monthSpinner.getSelectedItem().toString();
        monthValueDigit = Integer.parseInt(monthValue.replaceAll("\\D+", ""));
        yearValue = yearInput.getText().toString();
    }

    private void setValues() {
        Resources res = getResources();
        String months[] = res.getStringArray(R.array.months);
        String item = months[0];
        String actualMonth = String.valueOf(ChartDataModel.getActualMonth());
        for (String currentMonth : months) {
            if (currentMonth.startsWith(actualMonth)) {
                item = currentMonth;
                break;
            }
        }
        monthSpinner.setSelection(adapter_month.getPosition(item));
        yearInput.setText(String.valueOf(ChartDataModel.getActualYear()));
        String categoryInput = monthSpinner.getSelectedItem().toString();
        String mpkInput = yearInput.getText().toString();
        //check if it is ok
    }

    private void setComponents() {
        generateB = (ImageButton) this.findViewById(R.id.generateChartButton);
        returnB = (ImageButton) this.findViewById(R.id.backButton);
        yearInput = (EditText) this.findViewById(R.id.yearInput);
        infoText = (TextView) this.findViewById(R.id.textView);
        monthSpinner = (Spinner) this.findViewById(R.id.monthsSpinner);
        infoChart = (TextView) this.findViewById(R.id.infoChart);
    }

    private void setMonthSpinner() {
        adapter_month = ArrayAdapter.createFromResource(this, R.array.months, R.layout.spinner_item);
        adapter_month.setDropDownViewResource(R.layout.spinner_dropdown_item);
        monthSpinner.setAdapter(adapter_month);
    }
}