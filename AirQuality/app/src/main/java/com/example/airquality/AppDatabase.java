package com.example.airquality;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

@Database(entities = {DailyAirQuality.class, HourlyAirQuality.class, Location.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HourlyAirQualityDAO hourlyAirQualityDAO();
    public abstract DailyAirQualityDAO dailyAirQualityDAO();
    public abstract LocationDAO locationDAO();

    private static AppDatabase _instance;

    public static AppDatabase Instance(Context context){
        if (_instance == null){
            _instance = Room.databaseBuilder(context,
                    AppDatabase.class, "AirQualityDatabase").build();
        }
        return _instance;
    }
}
