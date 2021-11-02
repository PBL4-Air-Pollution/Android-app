package com.example.airquality.viewmodel;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.airquality.model.DailyAirQuality;

import java.util.Date;
import java.util.List;

@Dao
public interface DailyAirQualityDAO {
<<<<<<< Updated upstream
    @Query("SELECT * FROM DailyAirQuality")
    List<DailyAirQuality> getAll();

    @Query("SELECT * FROM DailyAirQuality WHERE id = :id")
    DailyAirQuality getOneByID(int id);

    @Insert
    void insertAll(DailyAirQuality... dailyAirQualities);

    @Query("DELETE FROM DailyAirQuality WHERE CAST(datetime as DATE) = :date")
    void deleteByDate(Date date);
=======
//    @Query("SELECT * FROM DailyAirQuality")
//    List<DailyAirQuality> getAll();
//
//    @Query("SELECT * FROM DailyAirQuality WHERE id = :id")
//    DailyAirQuality getOneByID(int id);
//
//    @Insert
//    void insertAll(DailyAirQuality... dailyAirQualities);
//
//    @Query("DELETE FROM DailyAirQuality WHERE date = :date")
//    void deleteByDate(Date date);
>>>>>>> Stashed changes
}
