package com.example.airquality.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DailyAirQuality")
public class DailyAirQuality {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private int locationID;
    @ColumnInfo
    private String date;
  
    @ColumnInfo
    private double aqi;

    @ColumnInfo
    private String rated;

    public DailyAirQuality() {
    }

    public DailyAirQuality(int locationID, String date, double aqi, String rated) {
        this.locationID = locationID;
        this.date = date;
        this.aqi = aqi;
        this.rated = rated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAqi() {
        return aqi;
    }

    public void setAqi(double aqi) {
        this.aqi = aqi;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }
}
