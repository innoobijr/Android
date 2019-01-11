package com.example.transporttracker;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PlacesRepository implements AsyncResponse {
    private PlacesDao placesDao;
    private static final String TAG = "Repo";
    private LiveData<List<GeoBean>> places;
    private LiveData<List<GeoBean>> results;
    QueryAsyncTask asyncTask;

    PlacesRepository(Application application){
        LocationDatabase db = LocationDatabase.getDatabase(application);
        placesDao = db.placesDao();
        places = placesDao.fetchAllData();
    }

    LiveData<List<GeoBean>> getPlaces() {return places; }


    public void insert (List<GeoBean> place){

        new insertGeoTask(placesDao).execute(place);
    }

    public LiveData<List<GeoBean>> query (double lng, double lat){
        asyncTask = new QueryAsyncTask(placesDao);
        asyncTask.delegate = this;
        try {
            return asyncTask.execute(lng, lat).get();

        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void processFinish(LiveData<List<GeoBean>> output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        results = output;
    }


    //public void delete (){
      //  new deleteAsyncTask(placesDao).execute();
    //}

}