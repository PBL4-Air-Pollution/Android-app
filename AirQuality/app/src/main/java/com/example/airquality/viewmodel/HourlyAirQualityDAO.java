package com.example.airquality.viewmodel;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.airquality.model.HourlyAirQuality;

import java.util.Date;
import java.util.List;

@Dao
public interface HourlyAirQualityDAO {
    @Query("SELECT * FROM HourlyAirQuality")
    List<HourlyAirQuality> getAll();

    @Query("SELECT * FROM HourlyAirQuality WHERE id = :id")
    HourlyAirQuality getOneByID(int id);

    @Query("SELECT * FROM HourlyAirQuality WHERE date >= :date AND date < DATEADD(day,1,:date)")
    List<HourlyAirQuality> getAllByDate(Date date);

    @Insert
    void insertAll(HourlyAirQuality... hourlyAirQualities);

    @Query("DELETE FROM HourlyAirQuality WHERE date >= :date AND date < DATEADD(day,1,:date)")
    void deleteByDate(Date date);
}
