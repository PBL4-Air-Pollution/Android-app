package com.example.airquality.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "HourlyAirQuality")
public class HourlyAirQuality {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private int locationID;

    @ColumnInfo
    private String datetime;

    @ColumnInfo
    private double pm25;

    @ColumnInfo
    private double pm10;

    @ColumnInfo
    private double no;

    @ColumnInfo
    private double no2;

    @ColumnInfo
    private double nox;

    @ColumnInfo
    private double nh3;

    @ColumnInfo
    private double co;

    @ColumnInfo
    private double so2;

    @ColumnInfo
    private double o3;

    @ColumnInfo
    private double benzene;

    @ColumnInfo
    private double toluene;

    @ColumnInfo
    private double xylene;

    @ColumnInfo
    private double aqi;

    @ColumnInfo
    private String rated;

    @Ignore
    public HourlyAirQuality(){
        // Cho firebase đổ dữ liệu vào class object
    }

    public HourlyAirQuality(int locationID, String datetime, double pm25, double pm10, double no, double no2, double nox, double nh3, double co, double so2, double o3, double benzene, double toluene, double xylene, double aqi, String rated) {
        this.locationID = locationID;
        this.datetime = datetime;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.no = no;
        this.no2 = no2;
        this.nox = nox;
        this.nh3 = nh3;
        this.co = co;
        this.so2 = so2;
        this.o3 = o3;
        this.benzene = benzene;
        this.toluene = toluene;
        this.xylene = xylene;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getPm10() {
        return pm10;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }

    public double getNo() {
        return no;
    }

    public void setNo(double no) {
        this.no = no;
    }

    public double getNo2() {
        return no2;
    }

    public void setNo2(double no2) {
        this.no2 = no2;
    }

    public double getNox() {
        return nox;
    }

    public void setNox(double nox) {
        this.nox = nox;
    }

    public double getNh3() {
        return nh3;
    }

    public void setNh3(double nh3) {
        this.nh3 = nh3;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }

    public double getSo2() {
        return so2;
    }

    public void setSo2(double so2) {
        this.so2 = so2;
    }

    public double getO3() {
        return o3;
    }

    public void setO3(double o3) {
        this.o3 = o3;
    }

    public double getBenzene() {
        return benzene;
    }

    public void setBenzene(double benzene) {
        this.benzene = benzene;
    }

    public double getToluene() {
        return toluene;
    }

    public void setToluene(double toluene) {
        this.toluene = toluene;
    }

    public double getXylene() {
        return xylene;
    }

    public void setXylene(double xylene) {
        this.xylene = xylene;
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
