package com.example.transporttracker;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class LocationRepository {
    private LocationDao locationDao;
    private LiveData<List<Local>> locations;

    LocationRepository(Application application){
        LocationDatabase db = LocationDatabase.getDatabase(application);
        locationDao = db.locationDao();
        locations = locationDao.fetchAllData();
    }

    LiveData<List<Local>> getLocations() {return locations; };

    public void insert (Local location){
        new insertAsyncTask(locationDao).execute(location);
    }

    public void delete (){
        new deleteAsyncTask(locationDao).execute();
    }

}
