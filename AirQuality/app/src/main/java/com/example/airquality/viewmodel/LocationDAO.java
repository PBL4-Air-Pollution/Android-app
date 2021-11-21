package com.example.airquality.viewmodel;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.airquality.model.Location;

import java.util.List;

@Dao
public interface LocationDAO {
    @Query("SELECT * FROM Location")
    List<Location> getAll();

    @Query("SELECT * FROM Location WHERE marked = 1 AND name=:name")
    List<Location> getListByName(String name);
    @Query("SELECT name FROM Location WHERE marked = 1")
    List<String> getListNameByHasMark();
    @Insert
    void insertLocations(Location...locations);

    @Update
    void updateLocations(Location... locations);
}
