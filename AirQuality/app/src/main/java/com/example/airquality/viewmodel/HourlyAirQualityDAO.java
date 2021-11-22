package com.example.airquality.viewmodel;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.HourlyAirQuality;

import java.util.Date;
import java.util.List;

@Dao
public interface HourlyAirQualityDAO {
    @Query("SELECT * FROM HourlyAirQuality")
    List<HourlyAirQuality> getAll();

    @Query("SELECT * FROM HourlyAirQuality WHERE id = :id")
    HourlyAirQuality getOneByID(int id);


    @Query("SELECT * FROM HourlyAirQuality WHERE location=:location")
    List<HourlyAirQuality> getListByLocation(String location);

    @Insert
    void insertAll(HourlyAirQuality...hourlyAirQualities);

    @Query("DELETE FROM HourlyAirQuality WHERE CAST(datetime as DATE) = :date")
    void deleteByDate(Date date);
}
