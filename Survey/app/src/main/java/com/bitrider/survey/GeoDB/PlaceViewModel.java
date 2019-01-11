package com.example.transporttracker;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class PlaceViewModel extends AndroidViewModel {
    private PlacesRepository mRepository;

    private LiveData<List<GeoBean>> mAllLocations;
    private LiveData<List<GeoBean>> result;

    public PlaceViewModel (Application application) {
        super(application);
        mRepository = new PlacesRepository(application);
        mAllLocations = mRepository.getPlaces();
    }

    LiveData<List<GeoBean>> getmLocations() {return mAllLocations; }
    LiveData<List<GeoBean>> getResult() {return result; }


    public LiveData<List<GeoBean>> query(double lat, double lng) {
        return mRepository.query(lat, lng);
    }

    public void insert(List<GeoBean> bean) { mRepository.insert(bean); }
    //public void delete() { mRepository.delete(); }

}


