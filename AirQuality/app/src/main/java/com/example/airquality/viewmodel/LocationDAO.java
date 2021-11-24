package com.example.airquality.viewmodel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.airquality.model.Location;

import java.util.List;

@Dao
public interface LocationDAO {
    @Query("SELECT * FROM Location")
    List<Location> getAll();

    @Query("SELECT * FROM Location WHERE marked = 0 AND stationName=:name")
    List<Location> getListByNameNoMark(String name);

    @Query("SELECT * FROM Location WHERE marked = 1 ")
    List<Location> getListHasMark();

    @Query("SELECT stationName FROM Location WHERE marked = 0")
    List<String> getListNameHasNotMark();

    @Query("UPDATE Location SET aqi=:aqi, rated=:rate WHERE id=:id")
    void updateAqiAndRate(int id, double aqi, String rate);

    @Insert
    void insertLocations(Location... locations);

    @Update
    void updateLocations(Location... locations);

    @Delete
    void deleteLocations(Location... locations);
}
