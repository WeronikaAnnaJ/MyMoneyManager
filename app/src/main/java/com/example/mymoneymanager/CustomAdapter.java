package com.example.mymoneymanager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<RecordModel> {
    private final Activity activity;
    private final ArrayList<RecordModel> list;
    private final static int rowLayout = R.layout.single_record_row;
    private LinearLayout layout;

    public CustomAdapter(Activity activity, ArrayList<RecordModel> list) {
        super(activity, 0, list);
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(rowLayout, parent, false);
        }
        TextView mpk = (TextView) convertView.findViewById(R.id.MPK);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        RecordModel record = getItem(position);
        mpk.setText(record.getMPK());
        amount.setText(String.format("%.2f", record.getAmount()) + " zł");
        date.setText(record.getStringDate());
        type.setText("    " + record.getType());
        category.setText(record.getCategory());
        if ((record.getType()).equals("Wydatek")) {
            icon.setBackgroundResource(R.drawable.icon_out_color_palete);
        } else {
            icon.setBackgroundResource(R.drawable.icon_in_color_palete);
        }

        return convertView;

    }
}

