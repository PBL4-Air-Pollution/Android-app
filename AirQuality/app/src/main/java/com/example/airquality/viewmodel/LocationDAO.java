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

    @Query("SELECT * FROM Location WHERE id=:id")
    Location getByID(int id);

    @Query("SELECT * FROM Location WHERE marked = 0")
    List<Location> getListHasNotMark( );

    @Query("SELECT * FROM Location WHERE marked = 1")
    List<Location> getListHasMark();

    @Query("SELECT * FROM Location WHERE favourite = 1")
    Location getFavouriteLocation();

    @Query("UPDATE Location SET aqi=:aqi, rated=:rate WHERE id=:id")
    void updateAqiAndRate(int id, double aqi, String rate);

    @Query("UPDATE Location SET favourite = 0")
    void clearFavourite();

    @Query("UPDATE Location SET favourite = 1 WHERE id=:id")
    void setFavourite(int id);

    @Insert
    void insertLocations(Location... locations);

    @Update
    void updateLocations(Location... locations);

    @Delete
    void deleteLocations(Location... locations);
}
