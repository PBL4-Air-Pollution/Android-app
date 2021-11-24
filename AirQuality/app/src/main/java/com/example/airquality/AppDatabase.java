package com.example.airquality;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.airquality.model.DailyAirQuality;
import com.example.airquality.model.DateConverter;
import com.example.airquality.model.HourlyAirQuality;
import com.example.airquality.model.Location;
import com.example.airquality.viewmodel.DailyAirQualityDAO;
import com.example.airquality.viewmodel.HourlyAirQualityDAO;
import com.example.airquality.viewmodel.LocationDAO;

import java.io.File;
import java.util.ArrayList;

@Database(entities = {DailyAirQuality.class, HourlyAirQuality.class, Location.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract HourlyAirQualityDAO hourlyAirQualityDAO();
    public abstract DailyAirQualityDAO dailyAirQualityDAO();
    public abstract LocationDAO locationDAO();

    private static AppDatabase _instance;

    public static AppDatabase Instance(Context context){
        if (_instance == null){
            if (!doesDatabaseExist(context.getApplicationContext(), "/data/data/com.example.airquality/databases/AirQualityDatabase")) {
                _instance = Room.databaseBuilder(context,
                        AppDatabase.class, "AirQualityDatabase").allowMainThreadQueries().build();
                seedLocationData();
            }
            else {
                _instance = Room.databaseBuilder(context,
                        AppDatabase.class, "AirQualityDatabase").allowMainThreadQueries().build();
            }
        }
        return _instance;
    }

    private static void seedLocationData() {
        _instance.locationDAO().insertLocations(new Location("Hòa Hiệp Bắc 1", 16.142936893046123, 108.08051682613947, "Đỉnh Hòa Vân, Hòa Hiệp Bắc, Liên Chiểu", "", false, 0, ""));
        _instance.locationDAO().insertLocations(new Location("Hòa Hiệp Bắc 2", 16.16503131490441, 108.14677811710084, "Làng Vân, Hòa Hiệp Bắc, Liên Chiểu ", "", false, 0, ""));
        _instance.locationDAO().insertLocations(new Location("Hòa Hiệp Nam", 16.092124186194372, 108.14144763862097, "Công viên nước nóng Mikazuki, Nguyễn Lương Bằng, Hoà Hiệp Nam, Liên Chiểu", "", false,0, ""));
        _instance.locationDAO().insertLocations(new Location("Hòa Ninh", 16.068638699088552, 108.07073212710776, "Hồ Hòa Trung, Hòa Ninh, Hòa Vang", "", false, 0, ""));
        _instance.locationDAO().insertLocations(new Location("Hòa Nhơn", 16.022115031551486, 108.12627122578975, "Đình Làng Phước Thuận, Hoà Nhơn, Hòa Vang", "", false, 0, ""));
        _instance.locationDAO().insertLocations(new Location("Hòa Khương", 15.956932710660778, 108.11922636604919, "Khu du lịch Phước Nhơn, Hoà Khương, Hòa Vang", "", false, 0, ""));
        _instance.locationDAO().insertLocations(new Location("Hòa Phú", 15.998025572625194, 108.05287934511186, "Suối mát Farm, Hoà Phú, Hòa Vang", "", false, 0, ""));
        _instance.locationDAO().insertLocations(new Location("Hòa Tiến", 15.984636178503463, 108.18310480357002, "Cầu Cửa Đình, Hòa Tiến, Hòa Vang", "", false, 0, ""));
        _instance.locationDAO().insertLocations(new Location("Hòa Quý", 16.003263664804894, 108.24626252760721, "Hồ Đầm Sen, Nguyễn Phước Lan, Hoà Quý, Ngũ Hành Sơn", "", false, 0, ""));
        _instance.locationDAO().insertLocations(new Location("Hòa Thuận Tây", 16.05700804619488, 108.20257470722422, "Sân bay Đà Nẵng, Nguyễn Văn Linh, Hòa Thuận Tây, Hải Châu", "", false, 0, ""));
        _instance.locationDAO().insertLocations(new Location("Thọ Quang", 16.105054301190197, 108.25494004679783, "Cơ quan Cảnh sát, Lê Đức Thọ, Thọ Quang, Sơn Trà", "", false, 0, ""));
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
