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

    @Query("SELECT * FROM HourlyAirQuality WHERE locationID=:locationID AND datetime=:datetime")
    HourlyAirQuality findByLocationIdAndDatetime(int locationID, String datetime);


    @Query("SELECT * FROM HourlyAirQuality WHERE locationID=:locationID")
    List<HourlyAirQuality> getListByLocationID(int locationID);

    @Query("SELECT * FROM HourlyAirQuality WHERE locationID=:locationID AND datetime LIKE :date || '%'")
    List<HourlyAirQuality> getListByLocationIDAndDate(int locationID, String date);

    @Insert
    void insertAll(HourlyAirQuality...hourlyAirQualities);

    @Query("DELETE FROM HourlyAirQuality WHERE datetime LIKE :date || '%'")
    void deleteByDate(String date);
}
