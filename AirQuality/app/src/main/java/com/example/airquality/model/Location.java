package com.example.airquality.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Location")
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String stationName;

    @ColumnInfo
    private double viDo;

    @ColumnInfo
    private double kinhDo;

    @ColumnInfo
    private String describe;

    @ColumnInfo
    private String label;

    @ColumnInfo
    private boolean marked;

    @ColumnInfo
    private double aqi;

    @ColumnInfo
    private String rated;

    public Location(String stationName, double viDo, double kinhDo, String describe, String label, boolean marked, double aqi, String rated) {
        this.stationName = stationName;
        this.viDo = viDo;
        this.kinhDo = kinhDo;
        this.describe = describe;
        this.label = label;
        this.marked = marked;
        this.aqi = aqi;
        this.rated = rated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public double getViDo() {
        return viDo;
    }

    public void setViDo(double viDo) {
        this.viDo = viDo;
    }

    public double getKinhDo() {
        return kinhDo;
    }

    public void setKinhDo(double kinhDo) {
        this.kinhDo = kinhDo;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
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
