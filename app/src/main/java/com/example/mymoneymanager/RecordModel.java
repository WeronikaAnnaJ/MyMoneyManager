package com.example.mymoneymanager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RecordModel {
    private Integer id;
    private String MPK;
    private double amount;
    private Date date;
    private String category;
    private String type;


    public RecordModel(Integer id, String category, String type, String MPK, double amount, Date date) {
        this.id = id;
        this.type = type;
        this.MPK = MPK;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public RecordModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMPK() {
        return MPK;
    }

    public void setMPK(String MPK) {
        this.MPK = MPK;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public String getStringDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        System.out.println("Date Format with MM/dd/yyyy : " + strDate);

        return strDate;
    }

    public void setDate(String date) {
        try {
            this.date = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            //
            e.printStackTrace();
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RecordModel{" +
                "id=" + id +
                ", MPK='" + MPK + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", category='" + category + '\'' +
                '}';
    }
}
