package com.example.transporttracker;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    void insertLocation(Local location);

    @Query("DELETE FROM location WHERE id NOT IN (SELECT MAX(id) id FROM location)")
    void deleteAllButLast();

    @Query("SELECT * FROM location")
    LiveData<List<Local>> fetchAllData();

    @Query("SELECT * FROM location")
    List<Local> fetchRegAllData();

    @Query ("SELECT * FROM location WHERE id NOT IN(SELECT MAX(id) id FROM location)")
    LiveData<List<Local>> selectAllButLast();

    @Update
    void updateLocation(Local location);



}
