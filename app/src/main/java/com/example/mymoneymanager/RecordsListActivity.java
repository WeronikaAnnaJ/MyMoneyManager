package com.example.mymoneymanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecordsListActivity extends AppCompatActivity {

    private ListView list;
    private CustomAdapter adapter;
    private DataBaseManager dataBaseManager;
    private FloatingActionButton rev;
    private ImageButton chart;
    private TextView sum;
    private TextView infoEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);
        getComponents();
        setRecordsList();
        setRecordsSum();
        setListeners();
        //Content provider check
        Cursor cursorId = getContentResolver().query(CustomContentProvider.CONTENT_URI, null, DataBaseManager.ID_COLUMN_NAME + " = " + 1, null, null);
        System.out.println("Content provider 'WHERE id' result count : " + cursorId.getCount());
        Cursor cursorAll = getContentResolver().query(CustomContentProvider.CONTENT_URI, null, null, null, null);
        System.out.println("Content provider result count : " + cursorAll.getCount());

    }


    private void getComponents() {
        rev = (FloatingActionButton) findViewById(R.id.newRecordButton);
        list = (ListView) findViewById(R.id.all_record_list);
        chart = findViewById(R.id.checkChartButton);
        sum = (TextView) findViewById(R.id.sumViev);
        chart.setImageResource(R.drawable.chart_icon_color_palete);
        infoEmptyList = (TextView) findViewById(R.id.infoEmptyList);

    }


    private void setRecordsList() {
        infoEmptyList.setText("");
        dataBaseManager = new DataBaseManager(RecordsListActivity.this);
        ArrayList<RecordModel> allRecords = dataBaseManager.getAllRecords();
        if (allRecords.isEmpty()) {
            infoEmptyList.setText("Brak wpisów :( ");
        }
        adapter = new CustomAdapter(this, allRecords);
        list.setAdapter(adapter);
    }

    private void setRecordsSum() {
        dataBaseManager = new DataBaseManager(RecordsListActivity.this);
        Double sumValue = dataBaseManager.getAllRecordsAmountSum();
        sum.setText(String.format("%.2f", sumValue));
    }


    private void setListeners() {
        rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecordActivity.class));
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChartActivity.class));
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //      Toast.makeText(RecordsListActivity.this, "On item click", Toast.LENGTH_SHORT).show();
                startActivity(getRecordDataForEdit(adapterView, i));
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecordModel recordModel = (RecordModel) adapterView.getItemAtPosition(i);
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(RecordsListActivity.this, dataBaseManager, recordModel);
                customAlertDialog.show();
                //        Toast.makeText(RecordsListActivity.this, "On long  item click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }


    public Intent getRecordDataForEdit(AdapterView<?> adapterView, int position) {
        RecordModel recordModel = (RecordModel) adapterView.getItemAtPosition(position);
        int id = recordModel.getId();
        String MPK = recordModel.getMPK();
        String amount = String.valueOf(recordModel.getAmount());
        String date = recordModel.getStringDate();
        String category = recordModel.getCategory();
        String type = recordModel.getType();
        Intent intent = new Intent(RecordsListActivity.this, RecordActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("MPK", MPK);
        intent.putExtra("amount", amount);
        intent.putExtra("date", date);
        intent.putExtra("category", category);
        intent.putExtra("type", type);
        return intent;
    }


}
