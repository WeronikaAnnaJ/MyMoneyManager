package com.example.mymoneymanager;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class CustomAlertDialog extends Dialog {

    private Activity activity;
    private Button yes, no;
    private RecordModel recordModel;
    private DataBaseManager dataBaseManager;

    public CustomAlertDialog(Activity activity, DataBaseManager dataBaseManager, RecordModel recordModel) {
        super(activity);
        this.activity = activity;
        this.recordModel = recordModel;
        this.dataBaseManager = dataBaseManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        getComponents();
        setListeners();
    }

    private void getComponents() {
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
    }


    private void setListeners() {
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecord();
                activity.startActivity(new Intent(activity.getApplicationContext(), RecordsListActivity.class));
                dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void deleteRecord() {
        dataBaseManager.deleteRecord(recordModel);
        Toast.makeText(activity, "Pomyślnie sunięto wpis :)", Toast.LENGTH_SHORT).show();
    }
}