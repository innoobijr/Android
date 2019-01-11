package com.bitrider.survey.GeoDB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PlacesDao {

    @Insert
    void insertPlace(GeoBean place);

    @Insert
    void insertMultiple(List<GeoBean> places);

    @Insert
    void insertPlaces(GeoBean... places);

    @Query("SELECT * FROM places")
    LiveData<List<GeoBean>> fetchAllData();

    @Query("SELECT * FROM places WHERE title = :query")
    List<GeoBean> getFeed(String query);

    @Query("SELECT * FROM places")
    List<GeoBean> fetchAll();

    @Query("SELECT * FROM places WHERE lat = :lat AND lng = :lng")
    LiveData<List<GeoBean>> getBusNames(double lng, double lat);

    @Query("SELECT * FROM places WHERE lat = :lat AND lng = :lng")
    List<GeoBean> getPlaceNames(double lng, double lat);

    @Query("SELECT id, title, lat, lng FROM places")
    LiveData<List<GeoBean>> fetchLatLngData();

    @Update
    void updatePlaces(GeoBean place);

}
