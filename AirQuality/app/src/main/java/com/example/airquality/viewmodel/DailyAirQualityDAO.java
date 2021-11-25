package com.example.airquality.viewmodel;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.airquality.model.DailyAirQuality;

import java.util.Date;
import java.util.List;

@Dao
public interface DailyAirQualityDAO {
    @Query("SELECT * FROM DailyAirQuality")
    List<DailyAirQuality> getAll();

    @Query("SELECT * FROM DailyAirQuality WHERE id = :id")
    DailyAirQuality getOneByID(int id);

    @Query("SELECT * FROM DailyAirQuality WHERE locationID=:locationID")
    List<DailyAirQuality> getListByLocationID(int locationID);
    @Query("SELECT * FROM DailyAirQuality WHERE locationID=:locationID AND date=:date")
    List<DailyAirQuality> getListByLocationIDAndDate(int locationID,String date);

    @Insert
    void insertAll(DailyAirQuality... dailyAirQualities);

    @Query("DELETE FROM DailyAirQuality WHERE date = :date")
    void deleteByDate(String date);
}
