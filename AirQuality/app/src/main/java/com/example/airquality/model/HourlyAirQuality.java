package com.example.airquality.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "HourlyAirQuality")
public class HourlyAirQuality {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String location;

    @ColumnInfo
    private Date datetime;

    @ColumnInfo
    private double PM25;

    @ColumnInfo
    private double PM10;

    @ColumnInfo
    private double NO2;

    @ColumnInfo
    private double CO;

    @ColumnInfo
    private double SO2;

    @ColumnInfo
    private double O3;

    @ColumnInfo
    private double AQI;

    @ColumnInfo
    private String rate;

    public HourlyAirQuality(String location, Date datetime, double PM25, double PM10, double NO2, double CO, double SO2, double O3, double AQI, String rate) {
        this.location = location;
        this.datetime = datetime;
        this.PM25 = PM25;
        this.PM10 = PM10;
        this.NO2 = NO2;
        this.CO = CO;
        this.SO2 = SO2;
        this.O3 = O3;
        this.AQI = AQI;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public double getPM25() {
        return PM25;
    }

    public void setPM25(double PM25) {
        this.PM25 = PM25;
    }

    public double getPM10() {
        return PM10;
    }

    public void setPM10(double PM10) {
        this.PM10 = PM10;
    }

    public double getNO2() {
        return NO2;
    }

    public void setNO2(double NO2) {
        this.NO2 = NO2;
    }

    public double getCO() {
        return CO;
    }

    public void setCO(double CO) {
        this.CO = CO;
    }

    public double getSO2() {
        return SO2;
    }

    public void setSO2(double SO2) {
        this.SO2 = SO2;
    }

    public double getO3() {
        return O3;
    }

    public void setO3(double o3) {
        O3 = o3;
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
