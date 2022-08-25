package com.example.mymoneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {
    private EditText mpk;
    private EditText amount_zl;
    private EditText amount_gr;
    private TextView date;
    private Spinner category;
    private Switch type;
    private ImageButton add_record;
    private ImageButton go_back;
    private DatePickerDialog picker;
    private RecordModel recordEdit;
    ImageView calendar;
    Intent intent;
    boolean isEdited;
    ImageButton sendButton;
    ArrayAdapter<CharSequence> adapter_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getComponents();
        setListeners();
        setCategorySpinner();
        String pattern = "dd/MM/yyyy";
        date.setText(new SimpleDateFormat(pattern).format(new Date()));
        intent = getIntent();
        if ((intent != null) && (intent.getIntExtra("id", 0) != 0)) {
            setExtraData();
            isEdited = true;
        } else {
            System.out.println("intrny - >  not null");
            isEdited = false;
        }

    }


    private void setExtraData() {
        recordEdit = new RecordModel();
        recordEdit.setId(intent.getIntExtra("id", 0));
        category.setSelection(adapter_category.getPosition(intent.getStringExtra("category")));
        String[] amount2 = intent.getStringExtra("amount").split("\\.");
        amount_zl.setText(amount2[0].replace("-", ""));
        if (amount2[1].length() == 1) {
            amount2[1] += "0";
        }
        amount_gr.setText(amount2[1]);
        mpk.setText(intent.getStringExtra("MPK"));
        date.setText(intent.getStringExtra("date"));
        boolean switchValue = intent.getStringExtra("type").equals("Wydatek") ? false : true;
        type.setChecked(switchValue);
    }

    private void getExtraData() {
        //lub z bazy danych
        recordEdit = new RecordModel();
        recordEdit.setId(intent.getIntExtra("id", 0));
        recordEdit.setMPK(intent.getStringExtra("MPK"));
        recordEdit.setAmount(intent.getDoubleExtra("amount", 0));
        recordEdit.setCategory(intent.getStringExtra("category"));
        recordEdit.setType(intent.getStringExtra("type"));
    }

    private void setCategorySpinner() {
        adapter_category = ArrayAdapter.createFromResource(this, R.array.category, R.layout.spinner_item);
        adapter_category.setDropDownViewResource(R.layout.spinner_dropdown_item);
        category.setAdapter(adapter_category);
    }

    private void getComponents() {
        category = findViewById(R.id.s_category2);
        type = findViewById(R.id.s_type2);
        mpk = findViewById(R.id.et_mpk2);
        amount_zl = findViewById(R.id.et_amaunt_zl2);
        amount_gr = findViewById(R.id.et_amaunt_gr2);
        date = findViewById(R.id.choosenDate);
        add_record = findViewById(R.id.ib_add_record2);
        go_back = findViewById(R.id.b_return);
        calendar = findViewById(R.id.calendarView);
        sendButton = (ImageButton) findViewById(R.id.sendButtonEdit);
    }


    private void setListeners() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalendar();
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalendar();
            }
        });
        add_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecord();
            }
        });
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecordsListActivity.class));
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMsgDialog sendSmsDialog = new SendMsgDialog(RecordActivity.this, getDataToSend());
                sendSmsDialog.show();
            }
        });
    }

    private String getDataToSend() {
        Boolean typeValue = type.isChecked();
        System.out.println(typeValue);
        String typeInput = type.isChecked() ? "Dochód " : "Wydatek";
        String message = "Wiadomość wygenerowana przez My Money Manager \n";
        message += "Typ transakcji : " + typeInput + "\n";
        try {
            message += "Kwota : " + getAmountInput(typeInput) + " zł " + "\n";
            ;
        } catch (Exception e) {
            message += "Kwota : " + "brak danych" + "\n";
            ;
        }
        message += "Data : " + date.getText().toString() + "\n";
        ;
        message += "Kategoria : " + category.getSelectedItem().toString() + "\n";
        message += "Szczegóły : " + mpk.getText().toString();
        return message;

    }

    private void setCalendar() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        picker = new DatePickerDialog(RecordActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }


    private double getAmountInput(String inputType) throws Exception {
        String amountZL = amount_zl.getText().toString();
        String amountGR = amount_gr.getText().toString();
        boolean isValid = (amountGR.matches("[0-9]{2}") && (amountZL.matches("[0-9]+")));
        if (!isValid) {
            throw new Exception("Money input is not valid.");
        }
        String[] types = getResources().getStringArray(R.array.type);
        double amount = Double.parseDouble(amountZL + "." + amountGR);
        if (inputType.equals(types[0])) {
            amount = (-1) * amount;
        }
        return amount;
    }


    private void addRecord() {
        String dateInput = date.getText().toString();
        String categoryInput = category.getSelectedItem().toString();
        String mpkInput = mpk.getText().toString();
        Boolean typeValue = type.isChecked();
        System.out.println(typeValue);
        String typeInput = type.isChecked() ? "Dochód " : "Wydatek";
        double amount = 0.0;
        Date dateValue = null;
        try {
            amount = getAmountInput(typeInput);
            try {
                dateValue = new SimpleDateFormat("dd/MM/yyyy").parse(dateInput);
            } catch (ParseException e) {
                date.setError("Wprowadź poprawną datę");
                return;
            }
        } catch (Exception e) {
            amount_gr.setError("Wprowadź kwotę");
            amount_zl.setError("Wprowadź kwotę");
            if (dateInput.isEmpty()) {
                date.setError("Wprowadź datę");
            }//return;
        }

        try {

            if (!isEdited) {
                RecordModel recordModel = new RecordModel(-1, categoryInput, typeInput, mpkInput, amount, dateValue);
                DataBaseManager dataBaseManager = new DataBaseManager(RecordActivity.this);
                dataBaseManager.addInput(recordModel);
                Toast.makeText(RecordActivity.this, "Dodano wpis :)", Toast.LENGTH_SHORT).show();
            } else {
                recordEdit.setMPK(mpkInput);
                recordEdit.setCategory(categoryInput);
                recordEdit.setType(typeInput);
                recordEdit.setAmount(amount);
                recordEdit.setDate(dateValue);
                DataBaseManager dataBaseManager = new DataBaseManager(RecordActivity.this);
                dataBaseManager.updateRecord(recordEdit);
                Toast.makeText(RecordActivity.this, "Wpis pomyślnie edytowano :)", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(getApplicationContext(), RecordsListActivity.class));
        } catch (Exception e) {
            Toast.makeText(RecordActivity.this, "Nie można dodać wpisu :(", Toast.LENGTH_SHORT).show();
        }
    }


}
