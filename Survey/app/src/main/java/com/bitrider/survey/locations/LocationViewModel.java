package com.example.transporttracker;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {
    private LocationRepository mRepository;

    private LiveData<List<Local>> mAllLocations;

    public LocationViewModel (Application application) {
        super(application);
        mRepository = new LocationRepository(application);
        mAllLocations = mRepository.getLocations();
    }

    LiveData<List<Local>> getmLocations() {return mAllLocations; }

    public void insert(Local location) { mRepository.insert(location); }
    public void delete() { mRepository.delete(); }

}
