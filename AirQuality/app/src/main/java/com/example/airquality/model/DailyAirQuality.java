package com.example.airquality.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "DailyAirQuality")
public class DailyAirQuality {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String datetime;
    
    @ColumnInfo
    private double AQI;

    @ColumnInfo
    private String rate;

    public DailyAirQuality(String datetime,double AQI, String rate) {
        this.datetime = datetime;
        this.AQI = AQI;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public double getAQI() {
        return AQI;
    }

    public void setAQI(double AQI) {
        this.AQI = AQI;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
