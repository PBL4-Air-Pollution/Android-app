package com.example.airquality;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.DateConverter;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

@Database(entities = {DailyAirQuality.class, HourlyAirQuality.class, Location.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract HourlyAirQualityDAO hourlyAirQualityDAO();
    public abstract DailyAirQualityDAO dailyAirQualityDAO();
    public abstract LocationDAO locationDAO();

    private static AppDatabase _instance;

    public static AppDatabase Instance(Context context){
        if (_instance == null){
            _instance = Room.databaseBuilder(context,
                    AppDatabase.class, "AirQualityDatabase").allowMainThreadQueries().build();
        }
        return _instance;
    }
}
